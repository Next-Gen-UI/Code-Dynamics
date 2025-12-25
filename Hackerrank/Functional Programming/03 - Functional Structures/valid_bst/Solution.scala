// https://www.hackerrank.com/challenges/valid-bst/problem

package functional_structures.valid_bst

import java.util.Scanner

trait Tree {
  def value: Int
}

object Tree {
  def parse(values: List[Int]): Boolean = {
    def inner(values: List[Int], lowerBound: Int = Int.MinValue, upperBound: Int = Int.MaxValue): (Tree, List[Int]) = values match {
      case v :: vs if v > lowerBound && v < upperBound =>
        val (left, leftRest) = inner(vs, lowerBound, v)
        val (right, rightRest) = inner(leftRest, v, upperBound)
        (new Node(v, left, right), rightRest)
      case _ => (Empty, values)
    }

    inner(values)._2 == Nil
  }
}

class Node(val value: Int, left: Tree, right: Tree) extends Tree

object Empty extends Tree {
  override def value: Int = throw new Exception("Value of empty tree")
}

object Solution {
  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val t = sc.nextInt
    (0 until t).foreach(_ => {
      val n = sc.nextInt

      val values = (0 until n).map(_ => sc.nextInt).toList

      println(if (Tree.parse(values)) "YES" else "NO")
    })

    sc.close()
  }
}