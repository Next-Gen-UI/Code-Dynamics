// https://www.hackerrank.com/challenges/the-tree-of-life/problem

package recursion.the_tree_of_life

import java.util.Scanner

import scala.annotation.tailrec

trait Tree {
  def value: Boolean

  def applyRule(rule: Int, parentValue: Boolean = false): Tree

  @tailrec
  final def changeState(rule: Int, stepCount: Int): Tree = if (stepCount == 0) this else applyRule(rule).changeState(rule, stepCount - 1)

  def atPath(path: List[Char]): Boolean

  protected def nextValue(rule: Int, parentValue: Boolean, leftValue: Boolean, rightValue: Boolean): Boolean = {
    def toBit(value: Boolean, index: Int) = (if (value) 1 else 0) << index

    val bit = toBit(parentValue, 3) | toBit(leftValue, 2) | toBit(value, 1) | toBit(rightValue, 0)
    (rule & (1 << bit)) != 0
  }
}

object Tree {
  def parse(s: List[Char]): Tree = {
    def inner(s: List[Char]): (Tree, List[Char]) = s match {
      case Nil => (Empty, Nil)
      case c :: cs => c match {
        case '.' => (Leaf(false), cs)
        case 'X' => (Leaf(true), cs)
        case '(' =>
          val (left, afterLeft) = inner(cs)
          val (root, afterRoot) = inner(afterLeft)
          val (right, afterRight) = inner(afterRoot)
          (Node(root.value, left, right), afterRight)
        case _ => inner(cs)
      }
    }

    inner(s)._1
  }
}

case class Node(value: Boolean, left: Tree, right: Tree) extends Tree {
  override def applyRule(rule: Int, parentValue: Boolean): Tree = Node(
    nextValue(rule, parentValue, left.value, right.value),
    left.applyRule(rule, value),
    right.applyRule(rule, value)
  )

  override def atPath(path: List[Char]): Boolean = path match {
    case ']' :: _ => value
    case c :: cs => (if (c == '<') left else right).atPath(cs)
    case Nil => throw new Exception("Wrong path")
  }
}

case class Leaf(value: Boolean) extends Tree {
  override def applyRule(rule: Int, parentValue: Boolean): Tree = Leaf(
    nextValue(rule, parentValue, leftValue = false, rightValue = false)
  )

  override def atPath(path: List[Char]): Boolean = path match {
    case ']' :: _ => value
    case _ => throw new Exception("Wrong path")
  }
}

object Empty extends Tree {
  override def value: Boolean = throw new Exception("Value of empty tree")

  override def applyRule(rule: Int, parentValue: Boolean): Tree = this

  override def atPath(path: List[Char]): Boolean = throw new Exception("atPath of empty tree")
}

case class Rule(n: Int)

object Solution {
  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val rule = sc.nextInt
    sc.nextLine
    val s = sc.nextLine

    val root = Tree.parse(s.toList)

    val n = sc.nextInt

    case class Query(initialIndex: Int, time: Int, path: List[Char])
    @scala.annotation.tailrec
    def readQueries(rest: Int, time: Int, acc: List[Query]): List[Query] = if (rest == 0) acc
    else {
      val stepCount = sc.nextInt
      val path = sc.nextLine.toList.drop(2)

      val nextTime = time + stepCount
      readQueries(rest - 1, nextTime, Query(n - rest, nextTime, path) :: acc)
    }

    val queries = readQueries(n, 0, Nil).sortBy(_.time)

    sc.close()

    case class Answer(initialIndex: Int, value: Boolean)
    case class Accumulator(tree: Tree, time: Int, answers: List[Answer])
    val accumulator = queries.foldLeft(Accumulator(root, 0, Nil))((acc, query) => {
      val tree = acc.tree.changeState(rule, query.time - acc.time)
      Accumulator(tree, query.time, Answer(query.initialIndex, tree.atPath(query.path)) :: acc.answers)
    })

    println(accumulator.answers.sortBy(_.initialIndex).map(v => if (v.value) 'X' else '.').mkString("\n"))
  }
}