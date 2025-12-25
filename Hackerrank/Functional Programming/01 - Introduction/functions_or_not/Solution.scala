// https://www.hackerrank.com/challenges/functions-or-not/problem

package introduction.functions_or_not

import java.util.Scanner

object Solution {
  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)
    val t = sc.nextInt

    (0 until t).foreach(_ => {
      val n = sc.nextInt

      println(if ((0 until n).map(_ => (sc.nextInt, sc.nextInt)).groupBy(_._1)
        .exists { case (_, list) => list.map(_._2).toSet.size > 1 }) "NO" else "YES")
    })
  }
}