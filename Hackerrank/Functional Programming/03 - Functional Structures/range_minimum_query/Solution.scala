// https://www.hackerrank.com/challenges/range-minimum-query/problem

package functional_structures.range_minimum_query

import java.util.Scanner

case class Node(var value: Int)

class SegmentTree(len: Int) {
  val nodes: Array[Node] = Array.fill[Node](4 * len)(Node(Int.MaxValue))

  def add(index: Int, value: Int, v: Int = 1, left: Int = 0, right: Int = len - 1): Unit = {
    if (left != right) {
      val middle = (left + right) >> 1
      if (index <= middle) add(index, value, 2 * v, left, middle)
      else add(index, value, 2 * v + 1, middle + 1, right)

      nodes(v).value = combine(nodes(2 * v).value, nodes(2 * v + 1).value)
    }
    else {
      val node = nodes(v)
      node.value = value
    }
  }

  def query(leftBound: Int, rightBound: Int, v: Int = 1, left: Int = 0, right: Int = len - 1): Int = {
    if (leftBound == left && rightBound == right) {
      val node = nodes(v)
      node.value
    }
    else {
      val middle = (left + right) >> 1
      if (rightBound <= middle) query(leftBound, rightBound, 2 * v, left, middle)
      else if (leftBound > middle) query(leftBound, rightBound, 2 * v + 1, middle + 1, right)
      else combine(
        query(leftBound, middle, 2 * v, left, middle),
        query(middle + 1, rightBound, 2 * v + 1, middle + 1, right)
      )
    }
  }

  def combine(leftValue: Int, rightValue: Int): Int = math.min(leftValue, rightValue)
}

object Solution {
  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val n = sc.nextInt
    val m = sc.nextInt
    val arr = (0 until n).map(_ => sc.nextInt)

    val tree = new SegmentTree(arr.length)
    arr.indices.foreach(i => tree.add(i, arr(i)))

    (0 until m).foreach(_ => {
      val left = sc.nextInt
      val right = sc.nextInt

      println(tree.query(left, right))
    })

    sc.close()
  }
}