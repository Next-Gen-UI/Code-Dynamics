// https://www.hackerrank.com/challenges/remove-duplicates/problem

package ad_hoc.remove_duplicates

import java.util.Scanner

object Solution {
  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val s = sc.nextLine

    sc.close()

    case class Acc(chars: Set[Char] = Set(), res: List[Char] = Nil)
    println(s.foldLeft(Acc())((acc: Acc, c) => {
      Acc(acc.chars + c, if (acc.chars.contains(c)) acc.res else c :: acc.res)
    }).res.reverse.mkString(""))
  }
}