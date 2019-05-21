package courses.domain.mappers

import courses.domain.models.User

class UserToJsonMapper extends Mapper[User] {
  override def map(user: User): String = {
    val id: Long = user.id
    val username: String = user.username
    val address: String = user.address getOrElse "Not present"
    val email: String = user.email

    s"{'id':$id,'username':'$username','email':'$email','address':'$address'}"
  }
}

object UserToJsonMapper {

  implicit class ObjectToJsonString[T](val s: T) {
    def toJson(implicit mapper: Mapper[T]): String = mapper.map(s)
  }

  implicit val userToJsonMapper: Mapper[User] = new UserToJsonMapper
}
