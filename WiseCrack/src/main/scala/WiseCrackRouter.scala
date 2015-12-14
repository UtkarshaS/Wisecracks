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

    val sensorDataRoutes = {
      post {
        pathPrefix("create_forum"){
          pathEndOrSingleSlash {
            entity(as[CreateForumJson]) { createForumInput =>
              handleSensorDataRequest(createForumInput)
            }
          }
        }
      }
    }
  }
