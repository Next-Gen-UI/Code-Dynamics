// https://www.hackerrank.com/challenges/missing-numbers-fp/problem

package ad_hoc.missing_numbers_fp

import java.util.Scanner

object Solution {
  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    sc.nextLine
    val a = sc.nextLine.split(' ').map(_.toInt)
    sc.nextLine
    val b = sc.nextLine.split(' ').map(_.toInt)

    sc.close()

    val min = b.min
    val maxDiff = 100
    val aValues = Array.ofDim[Int](maxDiff)
    a.foreach(v => aValues(v - min) += 1)

    case class Acc(res: List[Int] = Nil)
    println(b.foldLeft(Acc())((acc, v) => {
      val index = v - min
      aValues(index) -= 1
      Acc(if (aValues(index) == -1) v :: acc.res else acc.res)
    }).res.sorted.mkString(" "))
  }
}