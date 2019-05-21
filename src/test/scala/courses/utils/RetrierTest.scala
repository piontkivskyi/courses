package courses.utils

import scala.concurrent.duration._
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class RetrierTest extends FlatSpec with ScalaFutures with Matchers {

  "Retrier " should "run given function if accepted result is not acceptable" in {
    var n = 0

    val result: Int = Retrier.retry[Int](
      block = () => {
        n = n + 1
        1 + n
      },
      acceptResult = res => res % 2 == 0,
      retries = List(0.second, 1.second, 5.second)
    )

    assert(2 == result)
  }

  it should "return any value if tries limit is exceeded" in {
    val result: Int = Retrier.retry[Int](
      block = () => 3,
      acceptResult = res => res % 2 == 0,
      retries = List(0.second, 1.second)
    )

    assert(result == 3)
  }

  it should "do not broke while list of retries is empty" in {
    val result: Int = Retrier.retry[Int](
      block = () => 3,
      acceptResult = res => res % 2 == 0,
      retries = List()
    )

    assert(result == 3)
  }

  it should "thread sleep is working properly" in {
    val startTime = System.currentTimeMillis

    Retrier.retry[Int](
      block = () => 3,
      acceptResult = res => res % 2 == 0,
      retries = List(1.second, 2.second)
    )

    val endTime = System.currentTimeMillis

    // function execution might take some time, so >=
    assert(endTime - startTime >= 3.second.toMillis)
  }

  it should "return default result async" in {
    whenReady(
      Retrier.retryAsync[Int](
        () => Future { 1 },
        res => res != 1,
        List(0.second, 1.second)
      ),
      timeout(2.seconds)) { result => assert(result == 1) }
  }

  it should "return default value if retries list is empty" in {
    whenReady(
      Retrier.retryAsync[Int](
        () => Future { 1 },
        res => res != 1,
        List()
      ),
      timeout(1.seconds)) { result => assert(result == 1) }
  }
}
