import scala.concurrent._
import duration._
import ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}

// failure

val success = Future( 2 / 1 )
val failure = Future( 1 / 0 )

Await.ready(failure, 1.second)

failure.value

failure.failed

// Await.result throws the exception rather than returning Future.
// Await.result(failure, 1.second)

def timeIt[A](fn: => A): A = {
  val start = System.currentTimeMillis()
  val a = fn
  println(s"${System.currentTimeMillis() - start} ms")
  a
}


val f1 = Future { Thread.sleep(500); 1 }
val f2 = Future { Thread.sleep(500); 2 }

val f3 = for {
  a <- f1
  b <- f2
} yield a + b

timeIt {
  Await.result(f3, 2.seconds)
}

val f4 = for {
  a <- Future { Thread.sleep(500); 1 }
  b <- Future { Thread.sleep(500); 2 }
} yield a + b

timeIt {
  Await.result(f4, 2.seconds)
}

val fs = Future.successful(10)
val ff = Future.failed(new IllegalArgumentException("nope!"))

// recover failure, returns Future of Success or Failure
val fr = ff.recover {
  case _: IllegalArgumentException => 22
}

Await.result(fr, 1.second)

// recover failure with recoverWith
val ff2 = Future.failed(new IllegalStateException("Again, nope!"))
// returns value
val fr2 = ff2.recoverWith {
  case _: IllegalArgumentException => Future.successful(22)
}

Await.result(fr, 1.second)

val fa: Future[Any] = Future(10)

// Convert Future[Any] to Future[Int]
val fi = fa.collect {
  case i: Int => i
}

val ffi = fi.filter(_ > 11)

Await.ready(fi, 1.second).value
Await.ready(ffi, 1.second).value

// Handling success and failure in single function.
// transform takes a success function and failure function. What to do if success or what to do if failed.
val ffit = ffi.transform(i => i * 5, {ex =>
  println(ex.getMessage)
  throw new RuntimeException("it failed to filter", ex)
})

// provide fallback for recovery
ffit.fallbackTo(Future.successful(0))

val f6 = Future(2)
// andThen can be used to close files if there was a future where file was opened and some work was done.
// Regardless of success or failure, the file will be closed in andThen
val f7 = f6.andThen {
  case Success(i) if i % 2 == 0 => println(s"it's even")
}

// This is like event handlers
f7.onComplete {
  case Success(i) => println(s"It worked, and the answer is $i")
  case Failure(ex) => println(s"It failed: ${ex.getMessage}")
}

Await.result(f7, 1.second)

// foreach only works for success cases
f7.foreach(i => println(s"Got an $i"))
f7.failed.foreach(ex => println(ex.getMessage))

// Dealing with unknown number of Futures, imagine the length of nums is unknown
val nums = List(1,2,3,4,5)
def square(i: Int): Future[Int] = Future(i * i)

val futs: List[Future[Int]] = nums.map(square)
val futList: Future[List[Int]] = Future.sequence(futs)
Await.ready(futList, 1.second)

// traverse is equivalent of map + sequence on the future.
val futList2 = Future.traverse(nums)(square)
Await.ready(futList2, 1.second)

val ft1 = Future { Thread.sleep(10); 10 }
val ft2 = Future { Thread.sleep(5); 5 }
val ft3 = Future { Thread.sleep(20); 20 }
val sft = List(ft1, ft2, ft3)
// first completed future will return as result. Other futures are not cancelled but the results are thrown away.
Await.ready(Future.firstCompletedOf(sft), 5.second)

Await.ready(Future.foldLeft(sft)(0)(_ + _), 5.second)

// reduceLeft errors out if it's given empty sequence of future just like reduceLeft on Seq
Await.ready(Future.reduceLeft(sft)(_ + _), 5.second)


Await.ready(Future.fromTry(Try(1 / 0)), 5.second)