package repositories

import java.util.concurrent.atomic.AtomicLong

import cats.Id
import models.IotDevice

import scala.concurrent.Future

trait IotDeviceRepository[F[_]] {
  def registerDevice(userId: Long, serialNumber: String): F[IotDevice]
  def getById(id: Long): F[Option[IotDevice]]
  def getBySn(sn: String): F[Option[IotDevice]]
  def getByUser(userId: Long): F[Seq[IotDevice]]
}

class IotDeviceRepositoryInMemory extends IotDeviceRepository[Id] {

  private val devicesBucket: Map[Long, IotDevice] = Map()
  private val idGenerator: AtomicLong = new AtomicLong(0L)

  def registerDevice(userId: Long, serialNumber: String): Id[IotDevice] = {
    val id: Long = idGenerator.incrementAndGet()
    val device = IotDevice(id, userId, serialNumber)
    devicesBucket + (id -> device)
    device
  }
  def getById(id: Long): Id[Option[IotDevice]] = devicesBucket.get(id)
  def getBySn(sn: String): Id[Option[IotDevice]] = devicesBucket.values.toList.find(_.sn == sn)
  def getByUser(userId: Long): Id[Seq[IotDevice]] = devicesBucket.values.toList.filter(_.userId == userId)
}

class IotDeviceRepositoryFuture extends IotDeviceRepository[Future] {

  private val devicesBucket: Map[Long, IotDevice] = Map()
  private val idGenerator: AtomicLong = new AtomicLong(0L)

  def registerDevice(userId: Long, serialNumber: String): Future[IotDevice] = Future {
    val id: Long = idGenerator.incrementAndGet()
    val device = IotDevice(id, userId, serialNumber)
    devicesBucket + (id -> device)
    device
  }
  def getById(id: Long): Future[Option[IotDevice]] = Future { devicesBucket.get(id) }
  def getBySn(sn: String): Future[Option[IotDevice]] = Future {devicesBucket.values.find(_.sn == sn) }
  def getByUser(userId: Long): Future[Seq[IotDevice]] = Future { devicesBucket.values.toList.filter(_.userId == userId) }
}