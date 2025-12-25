// https://www.hackerrank.com/challenges/fp-list-length/problem

def f(arr: List[Int]): Int = {
  @scala.annotation.tailrec
  def inner(arr: List[Int], acc: Int = 0): Int = arr match {
    case Nil => acc
    case _ :: xs => inner(xs, acc + 1)
  }

  inner(arr)
}