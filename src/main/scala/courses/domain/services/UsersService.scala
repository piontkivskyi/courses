package courses.domain.services

package services

import cats.Monad
import cats.implicits._
import courses.domain.models.User
import courses.domain.repositories.UserRepository

class UsersService[F[_]](repository: UserRepository[F])
                       (implicit monad: Monad[F]) {

  def registerUser(username: String, address: Option[String], email: String): F[Either[String, User]] = {
    repository.getByUsername(username).flatMap({
      case Some(user) =>
        monad.pure(Left(s"User $user already exists"))
      case None =>
        repository.register(username, address, email).map(Right(_))
    })
  }

  def getById(id: Long): F[Option[User]] = repository.getById(id)
}
