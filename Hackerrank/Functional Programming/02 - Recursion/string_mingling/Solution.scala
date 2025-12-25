// https://www.hackerrank.com/challenges/string-mingling/problem

package recursion.string_mingling

import java.util.Scanner

object Solution {
  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)
    val p = sc.nextLine()
    val q = sc.nextLine()

    println(p.zip(q).map { case (a, b) => a.toString + b }.reduceLeft(_ + _))
  }
}