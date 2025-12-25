// https://www.hackerrank.com/challenges/fp-filter-array/problem

def f(delim: Int, arr: List[Int]): List[Int] = arr match {
  case Nil => Nil
  case x :: xs => if (x < delim) x :: f(delim, xs) else f(delim, xs)
}