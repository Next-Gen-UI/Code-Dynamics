// https://www.hackerrank.com/challenges/sequence-full-of-colors/problem

package recursion.sequence_full_of_colors

import java.util.Scanner

object Solution {

  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val t = sc.nextInt
    sc.nextLine

    val colors = "RGYB"

    (0 until t).foreach(_ => {
      val s = sc.nextLine

      val counts = colors.map(c => c -> s.count(_ == c)).toMap

      println(if (counts('R') == counts('G') && counts('Y') == counts('B') && prefix(s.toList, 'R', 'G') && prefix(s.toList, 'R', 'G')) "True" else "False")
    })

    sc.close()
  }

  def prefix(s: List[Char], c0: Char, c1: Char): Boolean = {
    @scala.annotation.tailrec
    def inner(s: List[Char], delta: Int): Boolean = s match {
      case Nil => true
      case x :: xs =>
        val nextDelta = delta + (x match {
          case `c0` => -1
          case `c1` => 1
          case _ => 0
        })

        math.abs(nextDelta) <= 1 && inner(xs, nextDelta)
    }

    inner(s, 0)
  }
}