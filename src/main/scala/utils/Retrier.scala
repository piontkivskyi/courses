package utils

import scala.annotation.tailrec
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
}
