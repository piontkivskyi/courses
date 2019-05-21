package courses

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

import scala.io.StdIn
import courses.config._
import courses.routes.UserRoutes

object CoursesApp extends App with CoursesConfig {
  implicit val system = ActorSystem(applicationName)
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val bindingFuture = Http().bindAndHandle(UserRoutes.routes, httpHost, httpPort)

  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done
}
