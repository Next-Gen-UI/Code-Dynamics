// https://www.hackerrank.com/challenges/number-of-binary-search-tree/problem

package memoization_and_dp.number_of_binary_search_tree

import java.util.Scanner

import scala.collection.mutable

object Solution {
  val modulo = 100000007

  private val data = mutable.Map[Int, Long]()

  def solve(n: Int): Long = data.getOrElseUpdate(n, if (n <= 1) 1 else
    (0 until n).map(i => solve(i) * solve(n - i - 1) % modulo).sum % modulo)

  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)
    val t = sc.nextInt
    println((0 until t).map(_ => sc.nextInt).map(solve).mkString("\n"))
  }
}