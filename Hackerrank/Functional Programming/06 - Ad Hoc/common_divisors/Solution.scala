// https://www.hackerrank.com/challenges/common-divisors/problem

package ad_hoc.common_divisors

import java.util.Scanner

object Solution {
  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val t = sc.nextInt
    (0 until t).foreach(_ => {
      val a = sc.nextInt
      val b = sc.nextInt

      def divisors(n: Int): Set[Int] = {
        def inner(v: Int): Set[Int] = if (v < 1) Set(n)
        else if (n % v == 0) inner(v - 1) + v + n / v
        else inner(v - 1)

        inner(math.sqrt(n).toInt)
      }

      println(divisors(a).intersect(divisors(b)).size)
    })
  }
}