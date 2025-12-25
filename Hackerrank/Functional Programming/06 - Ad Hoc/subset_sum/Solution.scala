// https://www.hackerrank.com/challenges/subset-sum/problem

package ad_hoc.subset_sum

import java.util.Scanner

import scala.collection.Searching._

object Solution {
  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    sc.nextLine
    val a = sc.nextLine.split(' ').map(_.toLong).sortBy(-_)

    var sum = 0L
    val sums = a.map(v => {
      sum += v
      sum
    })

    val t = sc.nextInt
    (0 until t).foreach(_ => {
      val s = sc.nextLong

      val count = (sums.search(s) match {
        case InsertionPoint(i) => i
        case Found(i) => i
      }) + 1
      println(if (count <= a.length) count else -1)
    })
  }
}