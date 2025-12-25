// https://www.hackerrank.com/challenges/eval-ex/problem

package introduction.eval_ex

object Solution {
  def main(args: Array[String]): Unit = {
    val sc = new java.util.Scanner(System.in)
    val n = sc.nextInt()
    val values = (0 until n).map(_ => sc.nextDouble())

    def e(x: Double) = {
      @scala.annotation.tailrec
      def inner(power: Double = 1, fact: Int = 1, index: Int = 0, acc: Double = 0): Double =
        if (index < 10) inner(power * x, fact * (index + 1), index + 1, acc + power / fact) else acc

      inner()
    }

    println(values.map(e).mkString("\n"))
  }
}