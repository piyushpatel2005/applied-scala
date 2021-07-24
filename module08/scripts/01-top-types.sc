val s: String = "hello"

val sa: Any = s
val sar: AnyRef = s

// Does not compile
// val sav: AnyVal = s

val i: Int = 10

val ia: Any = i
val iav: AnyVal = i

// Does not compile
// val iar: AnyRef = i

ia.isInstanceOf[Int]
ia.asInstanceOf[Int]
// ia.asInstanceOf[String]

sa.isInstanceOf[String]
sa.asInstanceOf[String]
// sa.asInstanceOf[Int]

case class Person (firstName: String, lastName: String)

object Person {
//  def apply(first: String, second: String): Person = {
//    println(s"Creating person $first $second")
//    this(first, second)
//  }

  def apply(name: String): Person = {
    this(name.split(" ")(0), name.split(" ")(1))
  }
}

val person = Person("Py Patel")
person.##

1.2 formatted "%d"