package courses.domain.tables

import courses.domain.models.User
import slick.jdbc.H2Profile.api._
import slick.lifted.{ProvenShape, TableQuery}

trait UserTable {

  class UserTable(tag: Tag) extends Table[User](tag, "Users") {
    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def username: Rep[String] = column[String]("username")

    def address: Rep[Option[String]] = column[Option[String]]("address")

    def email: Rep[String] = column[String]("email")

    def * : ProvenShape[User] = (id, username, address, email) <> (User.tupled, User.unapply)
  }

  val users = new TableQuery(tag => new UserTable(tag))
  val insertAction = users returning users.map(_.id) into ((user, id) => user.copy(id = id))
  val getByIdAction = (id: Long) => users.filter(_.id === id).result.headOption
  val getByUsernameAction = (username: String) => users.filter(_.username === username).result.headOption

}
