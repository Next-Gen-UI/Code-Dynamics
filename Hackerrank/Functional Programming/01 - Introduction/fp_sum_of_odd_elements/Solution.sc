// https://www.hackerrank.com/challenges/fp-sum-of-odd-elements/problem

def f(arr: List[Int]): Int = arr.filter(_ % 2 != 0).sum