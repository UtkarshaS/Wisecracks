import java.util.UUID

import Responses.{GenericMessageObj, ResponseObj}
import akka.actor.{Actor, ActorLogging}
import com.amazonaws.util.json.JSONArray
import com.fasterxml.jackson.databind.exc.{InvalidFormatException, UnrecognizedPropertyException}
import com.fasterxml.jackson.databind.{JsonMappingException, ObjectMapper}
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.typesafe.scalalogging.slf4j.LazyLogging
import play.api.libs.json.JsValue
import spray.http.StatusCodes._
import spray.http._
import spray.httpx.SprayJsonSupport
import spray.httpx.marshalling.Marshaller
import spray.httpx.unmarshalling.Unmarshaller
import spray.routing.{HttpService, MalformedRequestContentRejection, RejectionHandler, ValidationRejection}


/**
 * Created by utkarshashetye on 12/13/15.
 */

  trait Domain extends Actor with ActorLogging with HttpService with SprayJsonSupport with JacksonJsonSupport {

    implicit def jacksonJsonMarshaller[T <: AnyRef] =
      Marshaller.delegate[T, String](ContentTypes.`application/json`)(mapper.writeValueAsString(_))

  protected def uuid = UUID.randomUUID.toString

  }

  trait JacksonJsonSupport {

    val jacksonModules = Seq(DefaultScalaModule)

    val mapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModules(jacksonModules:_*)
    mapper.registerModule(new JodaModule())

    implicit def jacksonJsonUnmarshaller[T : Manifest] =
      Unmarshaller[T](MediaTypes.`application/json`) {
        case x: HttpEntity.NonEmpty =>
          val jsonSource = x.asString(defaultCharset = HttpCharsets.`UTF-8`)
          mapper.readValue[T](jsonSource)
      }
  }
trait RejectionHandlers extends LazyLogging {
  this: Domain =>

  implicit def validationRejectionHandler = RejectionHandler {
    case ValidationRejection(errMsg,_) :: _ =>
      logger.info(s"received validation error: $errMsg")
      complete(StatusCodes.Unauthorized,errMsg)
    case spray.routing.AuthorizationFailedRejection :: _ =>
      //todo - this string shouldn't be here
      logger.info("received authentication error")
      complete(StatusCodes.Unauthorized, "User is not authorized to this resource")
    case MalformedRequestContentRejection(msg, causeOpt) :: _ =>
      complete {
        causeOpt.map { cause =>
          cause match {
            case e: InvalidFormatException => BadRequest -> GenericMessageObj(s"Invalid data: ${e.getValue}")
            case e: UnrecognizedPropertyException => BadRequest -> GenericMessageObj(s"Unrecognized property: ${e.getPropertyName}")
            case e: JsonMappingException =>
              if(cause.getCause == null || cause.getCause.getMessage == null){
                BadRequest -> GenericMessageObj(cause.getMessage)
              } else if (!cause.getCause.getMessage.isEmpty) {
                BadRequest -> GenericMessageObj(cause.getCause.getMessage)
              } else {
                BadRequest -> GenericMessageObj(s"Invalid data format")
              }
            case _ =>  BadRequest -> GenericMessageObj(s"An unknown error occurred.")
          }
        }
      }
    case spray.routing.MissingHeaderRejection(headerName) :: _ =>
      complete(BadRequest -> GenericMessageObj("%s header is missing.".format(headerName)))
  }
}


object Responses {
  // Generic response objects
    trait ResponseObj
    case object EmptyResponse extends ResponseObj
    case class RedirectResponse(uri: String, redirection: Redirection)
    case class RestResponse(statusCode: StatusCode, responseObj: ResponseObj = EmptyResponse)
    case class RawJsonRestResponse(statusCode: StatusCode, jsValue: JsValue)

  // Custom response objects to be serialized
    case class GenericMessageObj(message: String) extends ResponseObj
  }


  /////_______________________________ JSON format deserializable case classes _______________________________________
  case class CreateForumJson(userId: String = "",
                            title: String ="",
                           radius: Int = 0,
                           lat: Double =0,
                           lng: Double = 0,
                           categoryId: Int =0,
                           createdDate:String = "") extends data.Forum

  case class GetForumJson(userId: String,
                          lat: Double,
                          lng: Double)

  case class GetPostJson(forumid: Int)

  //case class CreateForumMessage(message: String ="Forum created successfully") extends ResponseObj
  case class Forums(userId: String,
                    question: String,
                    radius: Int,
                    lat: Double,
                    lng: Double,
                    categoryId: Int,
                    created:String)

  case class CheckUserRequestJson(userId: String,
                                  fName: String,
                                  lName: String,
                                  email: String)

  case class ReplyToForumJson(userId: String, comment: String, forumId: Int)

  case class EndpointARNJson(registrationId: String)

  /////____________________________________ Report Format classes _______________________________________
  case class GetForumsReport(forumlist: JSONArray) extends ResponseObj

  case class CreateForumReport(userId: String, category: Int, msg: String) extends ResponseObj

  case class GetPostsReport() extends ResponseObj

  case class CheckUserReport(message: String) extends ResponseObj

  case class AddARNReport(message: String) extends ResponseObj

  case class ReplyToForumReport(message: String) extends ResponseObj