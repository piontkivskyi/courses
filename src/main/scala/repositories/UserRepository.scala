package repositories

import java.util.concurrent.atomic.AtomicLong

import cats.Id
import models.User

import scala.concurrent.Future

trait UserRepository[F[_]] {
  def registerUser(username: String): F[User]
  def getById(id: Long): F[Option[User]]
  def getByUsername(username: String): F[Option[User]]
}

class UserRepositoryInMemory extends UserRepository[Id] {

  private val usersBucket: Map[Long, User] = Map()
  private val idGenerator: AtomicLong = new AtomicLong(0L)

  def registerUser(username: String): Id[User] = {
    val id = idGenerator.incrementAndGet()
    val user = User(id, username)
    // add new value to users storage
    usersBucket + (id -> user)
    user
  }

  def getById(id: Long): Id[Option[User]] = usersBucket.get(id)
  def getByUsername(username: String): Id[Option[User]] = usersBucket.values.find(_.username == username)
}

class UserRepositoryFuture extends UserRepository[Future] {

  private val usersBucket: Map[Long, User] = Map()
  private val idGenerator: AtomicLong = new AtomicLong(0L)

  def registerUser(username: String): Future[User] = Future {
    val id = idGenerator.incrementAndGet()
    val user = User(id, username)
    // add new value to users storage
    usersBucket + (id -> user)
    user
  }

  def getById(id: Long): Future[Option[User]] = Future { usersBucket.get(id) }
  def getByUsername(username: String): Future[Option[User]] = Future { usersBucket.values.find(_.username == username) }
}
