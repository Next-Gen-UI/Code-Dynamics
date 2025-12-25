// https://www.hackerrank.com/challenges/swap-nodes/problem

package functional_structures.swap_nodes

import java.util.Scanner

trait Tree {
  def value: Int

  def swap(k: Int, depth: Int = 1): Tree

  def inOrder: Seq[Tree]
}

object Tree {
  private val emptyValue = -1

  def parse(nodes: IndexedSeq[(Int, Int)]): Tree = {
    def inner(index: Int): Tree = if (index == emptyValue) Empty else {
      val (leftIndex, rightIndex) = nodes(index - 1)
      new Node(index, inner(leftIndex), inner(rightIndex))
    }

    inner(1)
  }
}

class Node(val value: Int, left: Tree, right: Tree) extends Tree {
  override def swap(k: Int, depth: Int): Tree = {
    val (l, r) = if (depth % k == 0) (right, left) else (left, right)
    new Node(value, l.swap(k, depth + 1), r.swap(k, depth + 1))
  }

  override def inOrder: Seq[Tree] = (left.inOrder :+ this) ++ right.inOrder
}

object Empty extends Tree {
  override def value: Int = throw new Exception("Value of empty tree")

  override def swap(k: Int, depth: Int): Tree = this

  override def inOrder: Seq[Tree] = Vector()
}

case class Rule(n: Int)

object Solution {
  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val n = sc.nextInt

    val nodes = (0 until n).map(_ => (sc.nextInt, sc.nextInt))

    val root = Tree.parse(nodes)

    val t = sc.nextInt
    val swaps = (0 until t).map(_ => sc.nextInt)

    sc.close()

    swaps.foldLeft(root)((acc, query) => {
      val tree = acc.swap(query)
      println(tree.inOrder.map(_.value).mkString(" "))
      tree
    })
  }
}