// https://www.hackerrank.com/challenges/filter-elements/problem

package recursion.filter_elements

import java.util.Scanner

object Solution {
  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val t = sc.nextInt

    (0 until t).foreach(_ => {
      val n = sc.nextInt
      val k = sc.nextInt

      val seq = (0 until n).map(_ => sc.nextInt).toList

      val numbers = seq.groupBy(identity)
        .collect { case (key, list) if list.size >= k => key }
        .toSet

      println((if (numbers.isEmpty) Seq(-1)
      else {
        extract(seq, numbers)
      }).mkString(" "))
    })

    sc.close()
  }

  def extract(seq: List[Int], numbers: Set[Int]): List[Int] = {
    @scala.annotation.tailrec
    def inner(seq: List[Int], numbers: Set[Int], acc: List[Int]): List[Int] = seq match {
      case Nil => acc.reverse
      case x :: xs =>
        if (numbers.contains(x)) inner(xs, numbers - x, x :: acc)
        else inner(xs, numbers, acc)
    }

    inner(seq, numbers, Nil)
  }
}