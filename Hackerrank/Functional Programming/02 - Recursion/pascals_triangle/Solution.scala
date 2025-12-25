// https://www.hackerrank.com/challenges/pascals-triangle/problem

package recursion.pascals_triangle

import java.util.Scanner

object Solution {
  def c(n: Int, k: Int): Int = if (k == 0 || k == n) 1 else c(n - 1, k - 1) + c(n - 1, k)

  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)
    val t = sc.nextInt
    sc.close()

    (0 until t).foreach(n => {
      (0 to n).foreach(k => {
        print(s"${c(n, k)} ")
      })
      println()
    })
  }
}