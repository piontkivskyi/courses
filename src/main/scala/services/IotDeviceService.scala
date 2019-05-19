package services

import cats.Monad
import cats.implicits._
import models.IotDevice
import repositories.{IotDeviceRepository, UserRepository}

class IotDeviceService[F[_]](repository: IotDeviceRepository[F],
                             userRepository: UserRepository[F])
                            (implicit monad: Monad[F]) {

  // the register should fail with Left if the user doesn't exist or the sn already exists.
  def registerDevice(userId: Long, sn: String): F[Either[String, IotDevice]] = {
    userRepository.getById(userId).flatMap({
      case Some(user) =>
        monad.pure(Left(s"User $user already exists"))
      case None => {
        repository.getBySn(sn).flatMap({
          case Some(iotDevice) => monad.pure(Left(s"IotDevice $iotDevice already exists"))
          case None => repository.registerDevice(userId, sn).map(Right(_))
        })
      }
    })
  }
}
