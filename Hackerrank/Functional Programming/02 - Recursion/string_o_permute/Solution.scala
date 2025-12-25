// https://www.hackerrank.com/challenges/string-o-permute/problem

package recursion.string_o_permute

import java.util.Scanner

object Solution {
  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)
    val t = sc.nextInt
    sc.nextLine

    (0 until t).foreach(_ => {
      val s = sc.nextLine()
      println(s.grouped(2).map(i => i(1).toString + i(0)).mkString(""))
    })
  }
}