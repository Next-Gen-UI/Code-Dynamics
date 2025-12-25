// https://www.hackerrank.com/challenges/different-ways-fp/problem

package memoization_and_dp.different_ways_fp

import java.util.Scanner

import scala.collection.mutable

object Solution {
  private val data = mutable.Map[(Int, Int), Int]()

  private val modulo = 100000007

  def solve(n: Int, k: Int): Int = {
    def inner(n: Int, k: Int, acc: Int): Int = data.getOrElseUpdate((n, k),
      if (k == 0 || k == n) 1 else (solve(n - 1, k - 1) + solve(n - 1, k)) % modulo
    )

    inner(n, k, 0)
  }

  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val t = sc.nextInt
    println((0 until t).map(_ => solve(sc.nextInt, sc.nextInt)).mkString("\n"))
  }
}