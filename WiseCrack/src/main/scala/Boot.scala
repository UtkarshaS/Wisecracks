import akka.actor.ActorSystem
import akka.io.IO

import spray.can.Http
/**
 * Created by utkarshashetye on 12/13/15.
 */
  object Boot extends App {

    val config = Config()

    implicit val system = ActorSystem("data_api", config)

    // set up routers
    //val kinesisSender = system.actorOf(SendToKinesis.props, "kinesis-sender")
    //val bufferManagerActor = system.actorOf(BufferManager.props(kinesisSender), "buffer-manager")
    val mainRouter = system.actorOf(MainRouter.props, name = "main-router")

    system.registerOnTermination {
      system.log.info("Actor per request shutdown.")
    }

    IO(Http) ! Http.Bind(mainRouter, config.getString("server.io.ip"), port = config.getInt("server.io.port"))
  }