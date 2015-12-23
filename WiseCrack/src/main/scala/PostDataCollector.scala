import java.sql.Date
import java.util.ArrayList

import PostDataCollector._
import Responses.RestResponse
import akka.actor.{Actor, ActorLogging, Props}
import com.amazonaws.util.json.JSONArray
import data.{Forum, SqlPersistence}
import services.SNSMobilePushHelper
import spray.http.StatusCodes._


/**
 * Created by utkarshashetye on 12/13/15.
 */
object PostDataCollector {
  def props = Props(new PostDataCollector)

  val sql = new SqlPersistence()

  case class CreateNewForum(createForumJson: CreateForumJson) extends ControllerMessage

  case class GetForums(getForumJson: GetForumJson) extends ControllerMessage

  case class GetPosts(getPostJson: GetPostJson) extends ControllerMessage

  case class ReplyToForum(replyToForumJson: ReplyToForumJson) extends ControllerMessage

  case class CheckUserRequest(checkUserRequestJson: CheckUserRequestJson) extends ControllerMessage

  case class AddEndpointARN(endpointARNJson: EndpointARNJson) extends ControllerMessage
}

class PostDataCollector extends Actor with ActorLogging{

  val snshelper:SNSMobilePushHelper = new SNSMobilePushHelper()

  def receive: Receive = {
    case CreateNewForum(createForumInput) =>
      //Function to call endpoint ARN from user table using user id
      val current_endpoint_ARN = "current_endpoint_ARN"
      val current_topic_ARN = snshelper.createTopicForForum(createForumInput.title, current_endpoint_ARN)
      //DB Function call to save current topic arn along with forum data
      //Insert topic arn in the forum object
      val f: data.Forum = new data.Forum(0, createForumInput.lat, createForumInput.lng, 20, createForumInput.title, createForumInput.userId, "topic arn", createForumInput.title, createForumInput.categoryId, new Date(0) )
      PostDataCollector.sql.insertForum(f)
      sendReportforCreateForum(createForumInput)

    case GetForums(getForumInput) =>
      val forums:ArrayList[Forum] = PostDataCollector.sql.getForums(getForumInput.userId)
      val forumByAreaResult = PostDataCollector.sql.getForumsInArea(getForumInput.lat, getForumInput.lng)
      forums.addAll(forumByAreaResult)
      val s = forums.size()

      log.debug(s"Forumlist - $forums")
      val jsobj: JSONArray = new JSONArray(forums)

      sendReportforGetForums(jsobj)

    case GetPosts(getPostInput) =>
      log.debug(s"GetPosts")

    case CheckUserRequest(checkUserRequest) =>
      log.debug("In Check User Request case")
      //Function call to DB to check user request case
      sendReportforCheckUser("User exists or created")

    case AddEndpointARN(endpointARN) =>
      log.debug("In Add End Point ARN case ")

      snshelper.addRegIdtoSNS(endpointARN.registrationId)
      //Function call to user table in DB to add Endpoint ARN
      sendReportforEndPointARN("Successfully added endpointARN to the DB")

    case ReplyToForum(replyToForumJson) =>
      log.debug("In Reply to forum case")
      //Function call to DB to store post entry
      //Get topic and endpoint ARN from DB as string
      val current_topic_ARN = "current_topic_ARN"
      val current_endpoint_ARN = "current_endpoint_ARN"
      //Get comment with forum id in it
      val comment_with_forum_id = "comment_with_forum_id"

      snshelper.notifyAboutComment(current_topic_ARN, comment_with_forum_id)
      log.debug("Successfully sent notification about comment")

      snshelper.subscribeEndpointToTopic(current_topic_ARN, current_endpoint_ARN)
      log.debug("Successfully subscribed user to forum")

      sendReportReplyToForum("Successfully stored post entry to database and subscribed")
  }

  private def sendReportforCreateForum(data: CreateForumJson) = {
    log.debug(context.parent.toString())
      context.parent ! RestResponse(Accepted, CreateForumReport(data.userId, data.categoryId, "New Forum Created successfully"))
  }

  private def sendReportforGetForums(data: JSONArray) = {
    log.debug(context.parent.toString())
    context.parent ! RestResponse(Accepted, GetForumsReport(data))
  }

  private def sendReportforGetPosts(data: JSONArray) = {
    log.debug(context.parent.toString())
    context.parent ! RestResponse(Accepted, GetPostsReport())
  }

  private def sendReportforCheckUser(message: String) = {
    log.debug(context.parent.toString())
    context.parent ! RestResponse(Accepted, CheckUserReport(message))
  }

  private def sendReportforEndPointARN(message: String) = {
    log.debug(context.parent.toString())
    context.parent ! RestResponse(Accepted, AddARNReport(message))
  }

  private def sendReportReplyToForum(message: String) = {
    log.debug(context.parent.toString())
    context.parent ! RestResponse(Accepted, ReplyToForumReport(message))
  }
}