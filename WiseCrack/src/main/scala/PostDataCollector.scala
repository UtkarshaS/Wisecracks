import Responses.RestResponse
import PostDataCollector.CreateNewForum
import akka.actor.{Actor, ActorLogging, Props}
import spray.http.StatusCodes._

/**
 * Created by utkarshashetye on 12/13/15.
 */
object PostDataCollector {
  def props = Props(new PostDataCollector)

  case class CreateNewForum(createForumJson: CreateForumJson) extends ControllerMessage
}

class PostDataCollector extends Actor with ActorLogging{

  def receive: Receive = {
    case CreateNewForum(createForumInput) =>
      sendReport(createForumInput)
  }

  private def sendReport(data: CreateForumJson) = {
    context.parent ! RestResponse(Accepted,
      SensorDataReport(data.userId, data.categoryid, CreateForumMessage("received")))
  }
}