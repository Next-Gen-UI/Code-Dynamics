// https://www.hackerrank.com/challenges/lists-and-gcd/problem

package functional_structures.lists_and_gcd

import java.util.Scanner

object Solution {

  def main(args: Array[String]): Unit = {
    case class Item(p: Int, n: Int) {
      override def toString: String = s"$p $n"
    }

    val sc = new Scanner(System.in)

    val Q = sc.nextInt
    sc.nextLine
    val items = (0 until Q).flatMap(_ => sc.nextLine.split(' ').map(_.toInt).grouped(2).map(arr => Item(arr.head, arr.last)))
      .groupBy(_.p)
      .map { case (p, list) => (p, list.length, list.map(_.n).min) }
      .collect { case (p, Q, n) => Item(p, n) }
      .toSeq
      .sortBy(_.p)

    println(items.mkString(" "))
    sc.close()
  }
}