// https://www.hackerrank.com/challenges/boleyn-salary/problem

package persistent_structures.boleyn_salary

import java.util.Scanner

import scala.collection.immutable.TreeMap

object Solution {
  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val n = sc.nextInt
    val q = sc.nextInt

    val links = (0 until n - 1).map(_ => (sc.nextInt, sc.nextInt))
      .groupBy(_._2)
      .map { case (key, list) => key -> list.map(_._1) }
    val salaries = (0 until n).map(_ => sc.nextInt)

    case class Node(index: Int, count: Int, children: Seq[Node], data: TreeMap[Int, Int])

    val nodes = Array.ofDim[Node](n + 1)

    def create(index: Int): Node = {
      val children = links.get(index) match {
        case None => Nil
        case Some(v) => v.map(create)
      }

      val sortedChildren = children.sortBy(-_.count)
      val data = if (children.isEmpty) TreeMap[Int, Int]() else
        sortedChildren.tail.foldLeft(sortedChildren.head.data + (salaries(sortedChildren.head.index - 1) -> sortedChildren.head.index))(
          (acc, c) => acc ++ c.data + (salaries(c.index - 1) -> c.index)
        )

      val res = Node(index, children.map(_.count).sum, children, data)
      nodes(index) = res
      res
    }

    create(1)

    var answer = 0
    (0 until q).foreach(_ => {
      val v = sc.nextInt
      val k = sc.nextInt

      val id = answer + v

      answer = nodes(id).data.drop(k - 1).values.head
      println(answer)
    })
  }
}