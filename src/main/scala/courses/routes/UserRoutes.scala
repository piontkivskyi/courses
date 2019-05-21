package courses.routes

import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.RejectionHandler
import courses.domain.mappers.UserToJsonMapper._
import courses.domain.repositories.UsersRepository
import courses.domain.services.services.UsersService
import courses.validators.Validator._

import scala.concurrent.{ExecutionContext, Future}

object UserRoutes {

  implicit val ec: ExecutionContext = ExecutionContext.global

  import cats.implicits._

  val service = new UsersService[Future](new UsersRepository)

  // implicit error handler
  implicit def myRejectionHandler =
    RejectionHandler.newBuilder()
      .handle { case e: Exception =>
        complete((InternalServerError, "That wasn't valid! " + e.getMessage))
      }
      .result()

  val routes =
    pathPrefix("user") {
      get {
        parameter("id".as[Long]) {
          id => complete(
            service.getById(id).map {
              case Some(user) => HttpResponse(OK, entity = user.toJson)
              case None => HttpResponse(NotFound, entity = "Not Found")
            }
          )
        }
      } ~
      post {
        formField("username".as[String], "address".as[String].?, "email".as[String]) {
          (username: String, address: Option[String], email: String) => complete(
            username.validate(nonEmpty and onlyAlphanumericCharacters) match {
              case Right(_) => {
                service.registerUser(username, address, email).map {
                  case Right(user) => HttpResponse(OK, entity = user.toJson)
                  case Left(err) => HttpResponse(BadRequest, entity = err)
                }
              }
              case Left(err) => HttpResponse(BadRequest, entity = err)
            }
          )
        }
      }
    }
}
