// https://www.hackerrank.com/challenges/fp-reverse-a-list/problem

def f(list: List[Int]): List[Int] = list.foldLeft(Nil: List[Int])((acc, v) => v :: acc)