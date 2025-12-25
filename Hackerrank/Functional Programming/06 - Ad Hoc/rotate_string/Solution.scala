// https://www.hackerrank.com/challenges/rotate-string/problem

package ad_hoc.rotate_string

import java.util.Scanner

object Solution {

  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val t = sc.nextInt
    sc.nextLine

    (0 until t).foreach(_ => {
      val s = sc.nextLine

      case class Acc(s: String, answer: List[String])
      println((0 until s.length).foldLeft(Acc(s, Nil))((acc, _) => {
        val nextV = acc.s.tail + acc.s.head
        Acc(nextV, nextV :: acc.answer)
      }).answer.reverse.mkString(" "))
    })
  }
}