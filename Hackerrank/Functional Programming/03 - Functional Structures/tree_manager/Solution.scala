// https://www.hackerrank.com/challenges/tree-manager/problem

package functional_structures.tree_manager

import java.util.Scanner

trait Tree {
  def value: Int

  def value_=(v: Int): Unit

  def children: Array[Tree]

  def children_=(v: Array[Tree]): Unit

  def parent: Tree
}

object Tree {
  val MaxChildCount = 10

  def createChildren: Array[Tree] = Array.ofDim[Tree](Tree.MaxChildCount)
}

class Node(var value: Int, val parent: Tree, var children: Array[Tree]) extends Tree

case object Empty extends Tree {
  override def value: Int = exception

  override def value_=(v: Int): Unit = exception

  override def children: Array[Tree] = exception

  override def children_=(v: Array[Tree]): Unit = exception

  private def exception = throw new Exception("The operation is not supported.")

  override def parent: Tree = exception
}

object Solution {
  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val q = sc.nextInt
    sc.nextLine

    val root = new Node(0, Empty, Tree.createChildren)
    var current: Tree = root

    (0 until q).foreach(_ => {
      val tokens = sc.nextLine.split(" ")

      tokens.head match {
        case "change" => current.value = tokens(1).toInt
        case "print" => println(current.value)
        case "visit" => tokens(1) match {
          case "left" =>
            val index = current.parent.children.indexOf(current)
            current = current.parent.children(index - 1)
          case "right" =>
            val index = current.parent.children.indexOf(current)
            current = current.parent.children(index + 1)
          case "parent" =>
            current = current.parent
          case "child" =>
            current = current.children(tokens(2).toInt - 1)
        }
        case "insert" =>
          def insertSibling(pos: Int): Unit = {
            val node = new Node(tokens(2).toInt, current.parent, Tree.createChildren)
            val index = current.parent.children.indexOf(current) + pos
            current.parent.children = (current.parent.children.take(index) :+ node) ++ current.parent.children.drop(index)
          }

          tokens(1) match {
            case "left" =>
              insertSibling(0)
            case "right" =>
              insertSibling(1)
            case "child" =>
              val node = new Node(tokens(2).toInt, current, Tree.createChildren)
              current.children = node +: current.children
          }
        case "delete" =>
          val temp = current
          current = current.parent
          current.children = current.children.filter(_ != temp) :+ Empty
      }
    })

    sc.close()
  }
}