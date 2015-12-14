import akka.actor.{Props, ActorRefFactory}

trait DataApiRoutes extends WiseCrackRouter {
  this: PerRequestCreator =>
  val routes = sensorDataRoutes
}

object MainRouter {
  def props = Props(new MainRouter)
}

class MainRouter extends PerRequestCreator with DataApiRoutes with RejectionHandlers
{
  override val actorRefFactory: ActorRefFactory = context
  def receive = runRoute(routes)
}