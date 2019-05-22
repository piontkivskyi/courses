package courses.domain.repositories

import courses.config.DatabaseConfig
import courses.domain.models.User
import courses.domain.tables.UserTable
import courses.utils.Retrier

import scala.concurrent.{ExecutionContext, Future}

trait UserRepository[F[_]] {
  def register(username: String, address: Option[String], email: String): F[User]
  def getById(id: Long): F[Option[User]]
  def getByUsername(username: String): F[Option[User]]
}

class UsersRepository(implicit ec: ExecutionContext) extends UserRepository[Future] with UserTable with DatabaseConfig {

  import scala.concurrent.duration._

  def register(username: String, address: Option[String], email: String): Future[User] = {
    val action = insertAction += User(0, username, address, email)
    // can't use retrier, because of type mismatch
//    Retrier.retryAsync[User](db.run(action), res => res != null, List(1.second))
    db.run(action)
  }

  def getById(id: Long): Future[Option[User]] = db.run(getByIdAction(id))

  def getByUsername(username: String): Future[Option[User]] = db.run(getByUsernameAction(username))
}
