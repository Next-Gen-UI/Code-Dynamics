// https://www.hackerrank.com/challenges/fibonacci-fp/problem

package memoization_and_dp.fibonacci_fp

import java.util.Scanner

object Solution {
  val modulo = 100000007

  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val t = sc.nextInt

    val max = 10001
    val empty = -1
    val data = Array.fill(max)(empty)

    def fib(n: Int): Int = if (data(n) != empty) data(n) else {
      val res = (n match {
        case 0 => 0
        case 1 => 1
        case _ => fib(n - 2) + fib(n - 1)
      }) % modulo
      data(n) = res
      res
    }

    (0 until t).foreach(_ => {
      val n = sc.nextInt
      println(fib(n))
    })

    sc.close()
  }
}