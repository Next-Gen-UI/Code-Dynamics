// https://www.hackerrank.com/challenges/sherlock-and-the-maze/problem

package memoization_and_dp.sherlock_and_the_maze

import java.util.Scanner

import scala.collection.mutable

object Solution {
  private val data = mutable.Map[(Int, Int, Int, Boolean), Int]()

  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val t = sc.nextInt
    (0 until t).foreach(_ => {
      val n = sc.nextInt
      val m = sc.nextInt
      val k = sc.nextInt

      println(solve(n, m, k))
    })

    sc.close()
  }

  def solve(m: Int, n: Int, k: Int): Int = {
    val modulo = 1000000007

    def inner(m: Int, n: Int, k: Int, isRight: Boolean): Int = {
      val (m1, n1, isRight1) = if (m < n) (m, n, isRight) else (n, m, !isRight)

      data.getOrElseUpdate((m1, n1, k, isRight1), {
        if (k < 0 || m1 < 1 || n1 < 1) 0
        else if (m1 == 1 && n1 == 1) 1
        else (inner(m1 - 1, n1, if (isRight1) k else k - 1, isRight = true) +
          inner(m1, n1 - 1, if (!isRight1) k else k - 1, isRight = false)) % modulo
      })
    }

    if (m == 1 && n == 1) 1 else (inner(m - 1, n, k, isRight = true) + inner(m, n - 1, k, isRight = false)) % modulo
  }
}