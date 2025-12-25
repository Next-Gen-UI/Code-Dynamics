// https://www.hackerrank.com/challenges/reverse-factorization/problem

package memoization_and_dp.reverse_factorization

import java.util.Scanner

import scala.collection.mutable

object Solution {
  private val data = mutable.Map[(Int, List[Int]), List[Int]]()

  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val n = sc.nextInt
    val k = sc.nextInt
    val a = (0 until k).map(_ => sc.nextInt).sortBy(-_).toList

    println(solve(n, a).mkString(" "))
  }

  def solve(n: Int, a: List[Int]): List[Int] = {
    def inner(n: Int, a: List[Int], acc: List[Int]): List[Int] = data.getOrElseUpdate((n, a), if (n == 1) acc
    else a match {
      case Nil => Nil
      case v :: vs =>
        val first = if (n % v == 0) inner(n / v, a, v :: acc) else Nil
        val second = inner(n, vs, acc)
        if (first.isEmpty || second.nonEmpty && second.size < first.size) second else first
    })

    val answer = inner(n, a, Nil)
    if (answer.isEmpty) List(-1) else answer.foldLeft(List(1))((acc, v) => v * acc.head :: acc).reverse
  }
}