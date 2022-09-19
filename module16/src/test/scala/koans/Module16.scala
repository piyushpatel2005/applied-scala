package koans

import org.scalatest.{FunSpec, Matchers, SeveredStackTraces}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class Module16 extends FunSpec with Matchers with SeveredStackTraces {

  describe ("Calculating Pi") {
    // Below is a simple implementation of a random draw method to calculate PI.
    // At present, it only uses one core. Alter the implementation to use Futures
    // and test that it does indeed use all cores once you have.

    class CalcPi {
      // Leave this method as is, i.e. synchronous
      def draw(iterations: Int): Future[Double] = Future{
        val random = XorRandom.random()
        var ct = 0
        var sum = 0.0

        while (ct < iterations) {
          ct += 1
          val x = random.nextDouble()
          val y = random.nextDouble()
          if (x * x + y * y < 1.0) sum += 1
        }
        sum / iterations
      }

      // Make this method return a Future[Vector[Double]] instead so that the implementation
      // can take advantage of multiple cores
      def draws(n: Int, iterations: Int): Future[Vector[Double]] = {
        Future.traverse((1 to n).toVector)(_ => draw(iterations))
      }

      // This method should call the above and combine the Futures in a way that does
      // not block or await them, returning a Future[Double] itself. We will await the
      // result in the test below.
      def calc(n: Int, iterations: Int): Future[Double] = {
        for {
          xs <- draws(n, iterations)
        } yield {
          xs.sum / xs.length
        }
      }
    }

    // Also update the test below to call the new version that returns a Double. You will need to await
    // the result here (or find some other way, e.g. AsyncFunSpec if you are feeling ambitious). Make sure
    // that all of your cores are working when you call this.
    it ("should calculate PI") {
      val calcPi = new CalcPi
      val pi = Await.result(calcPi.calc(500, 10000000), 1.minute) * 4

      println(pi)
      pi should be (3.141 +- 0.001)
    }

  }

}
