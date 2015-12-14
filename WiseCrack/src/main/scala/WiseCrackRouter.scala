import PostDataCollector.CreateNewForum
import spray.routing.Route


/**
 * Created by utkarshashetye on 12/10/15.
 */
trait WiseCrackRouter extends Domain{

    this: PerRequestCreator =>

    def handleSensorDataRequest(createForumInput: CreateForumJson): Route = {
      routeMessages(PostDataCollector.props, CreateNewForum(createForumInput), s"PostSensorDataController-$uuid")
    }

    def handleForumReplyRequest(replyToForumInput: ReplyToForumJson): Route = {
      routeMessagesforReplies(PostDataCollector.props, ReplyToForumJson(replyToForumInput), s"PostSensorDataController-$uuid")//rephrase
    }

    val sensorDataRoutes = {
      post {
        pathPrefix("create_forum"){
          pathEndOrSingleSlash {
            entity(as[CreateForumJson]) { createForumInput =>
              handleSensorDataRequest(createForumInput)
            }
          }
        }~
        pathPrefix("reply_to_forum"){
          pathEndOrSingleSlash{
            entity(as[ReplyToForumJson]){ replyForumInput =>
                handleForumReplyRequest(replyForumInput)
            }
          }
        }
      }
    }
  }
