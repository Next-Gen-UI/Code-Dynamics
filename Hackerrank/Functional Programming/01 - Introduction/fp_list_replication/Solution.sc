// https://www.hackerrank.com/challenges/fp-list-replication/problem

def f(num: Int, arr: List[Int]): List[Int] = arr.flatMap(v => (0 until num).map(_ => v))