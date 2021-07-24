implicit class TimesDo(val i: Int) extends AnyVal {
  def times(fn: => Unit): Unit = {
    for (_ <- 1 to i) fn
  }
}

5 times {
  println("hello")
}


implicit class Repeat(val s: String) extends AnyVal {
  def repeated(fn: String => Unit): String = {
    fn(s * 4)
    s * 4
  }
}

"helllo" repeated println