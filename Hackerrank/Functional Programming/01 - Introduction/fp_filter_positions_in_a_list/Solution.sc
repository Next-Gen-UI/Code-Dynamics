// https://www.hackerrank.com/challenges/fp-filter-positions-in-a-list/problem

def f(arr: List[Int]): List[Int] = arr match {
  case _ :: x :: xs => x :: f(xs)
  case _ => Nil
}