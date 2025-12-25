// https://www.hackerrank.com/challenges/jumping-bunnies/problem

package ad_hoc.jumping_bunnies

import java.util.Scanner

object Solution {
  @scala.annotation.tailrec
  def gcd(a: Long, b: Long): Long = if (b == 0) a else gcd(b, a % b)

  def lcm(a: Long, b: Long): Long = a / gcd(a, b) * b

  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val t = sc.nextInt
    println((0 until t).map(_ => sc.nextLong).reduce(lcm))
  }
}