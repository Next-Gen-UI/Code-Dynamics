// https://www.hackerrank.com/challenges/pentagonal-numbers/problem

package memoization_and_dp.pentagonal_numbers

import java.util.Scanner

import scala.collection.mutable

object Solution {
  private val data = mutable.Map[Int, Long]()

  def p(n: Int): Long = data.getOrElseUpdate(n, if (n == 1) 1 else p(n - 1) + 3 * n - 2)

  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val t = sc.nextInt
    (0 until t).foreach(_ => {
      val n = sc.nextInt
      println(p(n))
    })

    sc.close()
  }
}