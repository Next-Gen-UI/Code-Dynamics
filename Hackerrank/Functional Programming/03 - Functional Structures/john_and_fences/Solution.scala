// https://www.hackerrank.com/challenges/john-and-fences/problem

package functional_structures.john_and_fences

import java.util.Scanner

import scala.collection.immutable.TreeMap

object Solution {
  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val n = sc.nextInt
    val h = (0 until n).map(_ => sc.nextInt)

    sc.close()

    case class Fence(height: Int, pos: Int)

    val fences = h.indices.map(i => Fence(h(i), i)).sortBy(_.height).toList

    case class Accumulator(tree: TreeMap[Int, Int], bestSquare: Int)
    println(
      fences.foldLeft(Accumulator(TreeMap(0 -> n), 0))((acc, x) => {
        val (left, right) = acc.tree.rangeTo(x.pos).last
        Accumulator(acc.tree - left + (left -> x.pos) + (x.pos + 1 -> right), math.max(acc.bestSquare, (right - left) * x.height))
      }).bestSquare
    )
  }
}