// https://www.hackerrank.com/challenges/super-digit/problem

package recursion.super_digit

import java.util.Scanner

object Solution {
  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val n = sc.next
    val k = sc.nextInt

    sc.close()

    val nDigit = superOnce(n)
    println(superDigit((nDigit.toLong * k).toString))
  }

  def superDigit(s: Seq[Char]): Int = {
    val sum = superOnce(s)
    if (sum < 10) sum else superOnce(sum.toString)
  }

  def superOnce(s: Seq[Char]): Int = s.foldLeft(0)((acc, c) => acc + c - '0')
}