// https://www.hackerrank.com/challenges/string-reductions/problem

package recursion.string_reductions

import java.util.Scanner

object Solution {
  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val s = sc.nextLine

    sc.close()

    def solve(s: String): String = {
      @scala.annotation.tailrec
      def inner(s: List[Char], used: Set[Char], acc: List[Char]): String = s match {
        case Nil => acc.reverse.mkString("")
        case c :: cs => if (used.contains(c)) inner(cs, used, acc) else inner(cs, used + c, c :: acc)
      }

      inner(s.toList, Set(), Nil)
    }

    println(solve(s))
  }
}