// https://www.hackerrank.com/challenges/kundu-and-bubble-wrap/problem

package ad_hoc.kundu_and_bubble_wrap

import java.util.Scanner

object Solution {
  val count = 100000000000000000L

  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val m = sc.nextInt
    val n = sc.nextInt

    sc.close()

    val q = m * n

    println((1 to q).map(v => calc(v, q)).sum)
  }

  def calc(v: Int, q: Int): Double = {
    val pNo = (q - v).toDouble / q
    (1 - math.pow(pNo, count)) / (1 - pNo)
  }
}