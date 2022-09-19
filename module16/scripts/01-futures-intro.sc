import scala.concurrent._
import duration._
import ExecutionContext.Implicits.global

val f1 = Future { Thread.sleep(5000); 10}
val f2 = f1.map(_ * 10)

f1.value
f1.isCompleted
f2.value
f2.isCompleted


Thread.sleep(1000)

f1.value
f1.isCompleted
f2.value
f2.isCompleted

val f3 = Future { 1 / 0 }
Thread.sleep(10)
f3.value

val a = 1
val b = 2
val c = 3
val s = "The answer is"

val sum = a + b + c
s"$s $sum"

// Turning above synchronous code into asynchronous

val fa = Future(1)
val fb = Future { Thread.sleep(5000); 2 }
val fc = Future(3)
val fd = Future { Thread.sleep(500); "The answer is"}

val fRes = for {
  a <- fa
  b <- fb
  c <- fc
  s <- fd
} yield {
  val sum = a + b + c
  s"$s $sum"
}

fRes.isCompleted
fRes.value

val resultStr = Await.result(fRes, 10.seconds)

Thread.sleep(500)
fRes.isCompleted
fRes.value

Thread.sleep(500)
fRes.isCompleted
fRes.value

// Await.result will throw exception if it encountered one.
// so, it's useful at the outer code, main method.
// Await.ready is more useful for the intermediate stages.
val fFailing = Future { Thread.sleep(2000); (1 / 0);}
val resultStr = Await.ready (fFailing, 2.seconds)
resultStr.failed
resultStr.value

val fFailing2 = Future { Thread.sleep(2000); (1 / 0);}
val resultStr = Await.result (fFailing2, 2.seconds)