import Responses.{RawJsonRestResponse, RedirectResponse, EmptyResponse, RestResponse}
import PerRequest.{WithActorRef, WithProps}
import akka.actor.SupervisorStrategy.Stop
import akka.actor._
import spray.http.ContentTypes
import spray.http.StatusCodes._
import spray.httpx.marshalling.Marshaller
import spray.routing.{RequestContext, Route}
import play.api.libs.json.Json
import scala.concurrent.duration._

trait ControllerMessage

trait PerRequest extends Actor with JacksonJsonSupport with ActorLogging {

  import context._

  def requestContext: RequestContext
  def target: ActorRef
  def message: ControllerMessage

  setReceiveTimeout(5.seconds)
  target ! message

  def receive = {
    case res: RawJsonRestResponse =>
      perRequestComplete(res)
    case res: RestResponse =>
      perRequestComplete(res)
    case res: RedirectResponse =>
      perRequestRedirect(res)
    case rt: ReceiveTimeout =>
      log.warning("Got ReceiveTimeout")
      perRequestComplete(RestResponse(GatewayTimeout, EmptyResponse))
    case notHandled: AnyRef =>
      log.error(s"Unhandled message in PerRequest.receive: $notHandled")
      perRequestComplete(RestResponse(InternalServerError, EmptyResponse))
  }

  def perRequestComplete(restResponse: RestResponse) = {

    implicit def jacksonJsonMarshaller[T <: AnyRef] =
      Marshaller.delegate[T, String](ContentTypes.`application/json`)(mapper.writeValueAsString(_))

    restResponse.statusCode match {
      case NoContent => requestContext.complete { NoContent }
      case _ => requestContext.complete { restResponse.statusCode -> restResponse.responseObj }
    }
    stop(self)
  }

  def perRequestComplete(restResponse: RawJsonRestResponse) = {
    implicit def jacksonJsonStringMarshaller[T <: AnyRef] =
      Marshaller.delegate[T, String](ContentTypes.`application/json`)(_.toString)

    restResponse.statusCode match {
      case NoContent => requestContext.complete { NoContent }
      case _ =>
        val jsString = Json.stringify(restResponse.jsValue)
        requestContext.complete { restResponse.statusCode -> Json.stringify(restResponse.jsValue)}
    }
    stop(self)
  }

  def perRequestRedirect(redirectResponse: RedirectResponse) = {
    requestContext.redirect(redirectResponse.uri, redirectResponse.redirection)
    stop(self)
  }

  override val supervisorStrategy = OneForOneStrategy() {
    case e =>
      perRequestComplete(RestResponse(InternalServerError))
      Stop
  }
}

object PerRequest {
  case class WithActorRef(requestContext: RequestContext, target: ActorRef, message: ControllerMessage) extends PerRequest

  case class WithProps(requestContext: RequestContext, props: Props, message: ControllerMessage) extends PerRequest {
    lazy val target = context.actorOf(props)
  }
}

trait PerRequestCreator  {
  this: Actor with ActorLogging =>

  def perRequest(r: RequestContext, target: ActorRef, message: ControllerMessage, actorName: String) =
    context.actorOf(Props(new WithActorRef(r, target, message)), actorName)

  def perRequest(r: RequestContext, props: Props, message: ControllerMessage, actorName: String) =
    context.actorOf(Props(new WithProps(r, props, message)), actorName)

  def routeMessages(controller: Props, message: ControllerMessage, name: String): Route = {
    log.debug(s"Creating actor - $name")
    ctx => perRequest(ctx, controller, message, name)
  }
}
