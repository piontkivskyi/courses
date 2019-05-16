package utils

import scala.annotation.tailrec
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

object Retrier {

  /**
    * Try to run given function again if result is not equal to acceptResult
    *
    * @param block
    * @param acceptResult
    * @param retries
    * @tparam A
    * @return
    */
  @tailrec
  final def retry[A](
                      block: () => A,
                      acceptResult: A => Boolean,
                      retries: List[FiniteDuration]
                    ): A = {
      val result: A = block()
      if (acceptResult(result) || retries.isEmpty) {
        result
      } else {
        Thread.sleep(retries.head.toMillis)
        retry[A](
          block = block,
          acceptResult = acceptResult,
          retries = retries.tail
        )
      }
    }

  /**
    *
    * @param block
    * @param acceptResult
    * @param retries
    * @param ec
    * @tparam A
    * @return
    */
  final def retryAsync[A](
                    block: () => Future[A],
                    acceptResult: A => Boolean,
                    retries: List[FiniteDuration]
                    )(implicit ec: ExecutionContext): Future[A] = {
    block().flatMap(result => {
      if (acceptResult(result) || retries.isEmpty) {
        Future(result)
      } else {
        Thread.sleep(retries.head.toMillis)
        retryAsync(block, acceptResult, retries.tail)
      }
    })
  }
}
