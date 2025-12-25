// https://www.hackerrank.com/challenges/prefix-compression/problem

package recursion.prefix_compression

import java.util.Scanner

object Solution {
  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val s0 = sc.nextLine
    val s1 = sc.nextLine

    sc.close()

    val prefixLen = s0.zip(s1).takeWhile { case (c0, c1) => c0 == c1 }.length

    printf(s"$prefixLen ${s0.take(prefixLen)}\n")
    printf(s"${s0.length - prefixLen} ${s0.drop(prefixLen)}\n")
    printf(s"${s1.length - prefixLen} ${s1.drop(prefixLen)}\n")
  }
}