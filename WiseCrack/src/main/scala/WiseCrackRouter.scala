import PostDataCollector._
import spray.http.StatusCodes
import spray.routing.Route

/**
 * Created by utkarshashetye on 12/10/15.
 */
trait WiseCrackRouter extends Domain {

  this: PerRequestCreator =>

  def handleCreateForumRequest(createForumInput: CreateForumJson): Route = {
    routeMessages(PostDataCollector.props, CreateNewForum(createForumInput), s"CreateForumRequest-$uuid")
  }

  def handleGetForumRequest(getForumJson: GetForumJson): Route = {
    routeMessages(PostDataCollector.props, GetForums(getForumJson), s"GetForumRequest-$uuid")
  }

  def handleGetPostRequest(getPostJson: GetPostJson): Route = {
    routeMessages(PostDataCollector.props, GetPosts(getPostJson), s"GetPostRequest-$uuid")
  }

  def handleForumReplyRequest(replyToForumJson: ReplyToForumJson): Route = {
    routeMessages(PostDataCollector.props, ReplyToForum(replyToForumJson), s"ReplyToForum-$uuid")
  }

  def handleCheckUserRequest(checkUserRequestJson: CheckUserRequestJson):Route = {
    routeMessages(PostDataCollector.props, CheckUserRequest(checkUserRequestJson),s"CheckPostRequest-$uuid")
  }

  def handleAddEndpointARN(endpointARNJson: EndpointARNJson): Route ={
    routeMessages(PostDataCollector.props, AddEndpointARN(endpointARNJson), s"EndpointARN-$uuid")
  }

  val sensorDataRoutes = {
    post {
      pathPrefix("create_forum") { //working
        pathEndOrSingleSlash {
          entity(as[CreateForumJson]) { createForumInput =>
            handleCreateForumRequest(createForumInput)
          }
        }
      } ~
      pathPrefix("reply_to_forum") {
        pathEndOrSingleSlash {
          entity(as[ReplyToForumJson]) { replyForumInput =>
            handleForumReplyRequest(replyForumInput)
          }
        }
      } ~
      pathPrefix("check_user"){  //working
        pathEndOrSingleSlash{
          entity(as[CheckUserRequestJson]) { checkUserRequestJson =>
            handleCheckUserRequest(checkUserRequestJson)
          }
        }
      } ~
      pathPrefix("add_endpointArn"){  //working
        pathEndOrSingleSlash{
          entity(as[EndpointARNJson]){ endpointARNJson =>
            handleAddEndpointARN(endpointARNJson)
          }
        }
      }
    } ~
    get {
      pathPrefix("get_forums") {
        pathEndOrSingleSlash {
          entity(as[GetForumJson]) { getForumInput =>
            handleGetForumRequest(getForumInput)
          }
        }
      } ~
      pathPrefix("get_posts") {
        pathEndOrSingleSlash {
          entity(as[GetPostJson]) { getPostInput =>
            handleGetPostRequest(getPostInput)
          }
        }
      } ~
      pathPrefix("health") {  //Working
        pathEndOrSingleSlash {
          complete(StatusCodes.OK, "OK")
        }
      }
      //~
//      pathPrefix("get_forum_and_replies"){
//        pathEndOrSingleSlash{
//
//        }
//      }~
//      pathPrefix("check_get_request"){
//        pathEndOrSingleSlash{
//
//        }
//      }
    }
  }
}