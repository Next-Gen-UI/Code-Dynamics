// https://www.hackerrank.com/challenges/messy-medians/problem

package persistent_structures.messy_medians

import java.io.{BufferedReader, IOException, InputStreamReader}
import java.util.StringTokenizer

import scala.collection.immutable.TreeMap

class FastReader() {
  val br: BufferedReader = new BufferedReader(new InputStreamReader(System.in))
  var st: StringTokenizer = _

  def nextInt: Int = next.toInt

  def nextLong: Long = next.toLong

  def nextDouble: Double = next.toDouble

  def next: String = {
    while (st == null || !st.hasMoreElements)
      try
        st = new StringTokenizer(br.readLine)
      catch {
        case e: IOException =>
          e.printStackTrace()
      }
    st.nextToken
  }

  def nextLine: String = {
    var str = ""
    try
      str = br.readLine
    catch {
      case e: IOException =>
        e.printStackTrace()
    }
    str
  }
}

trait PartialTree {
  def extractMin: (Int, PartialTree)

  def extractMax: (Int, PartialTree)

  def add(v: Int): PartialTree
}

trait Tree extends PartialTree {
  def median: Int

  def add(v: Int): Tree
}

class PartialTreeImpl(val data: TreeMap[Int, Int]) extends PartialTree {
  override def extractMin: (Int, PartialTree) = {
    val (min, count) = data.head
    (min, new PartialTreeImpl(if (count == 1) data.tail else data + (min -> (count - 1))))
  }

  override def extractMax: (Int, PartialTree) = {
    val (max, count) = data.last
    (max, new PartialTreeImpl(if (count == 1) data.init else data + (max -> (count - 1))))
  }

  override def add(v: Int): PartialTree = new PartialTreeImpl(data + (v -> (data.getOrElse(v, 0) + 1)))
}

object PartialTreeImpl {
  def apply(tree: PartialTree, value: Int): PartialTree = tree match {
    case pti: PartialTreeImpl => new PartialTreeImpl(pti.data + (value -> (pti.data.getOrElse(value, 0) + 1)))
    case Empty => new PartialTreeImpl(TreeMap(value -> 1))
  }
}

case object Empty extends Tree {
  override def median: Int = throw new Exception("Median of empty")

  override def add(value: Int): Tree = new Balanced(value, Empty, Empty)

  override def extractMin: (Int, Tree) = throw new Exception("Extract min from empty")

  override def extractMax: (Int, Tree) = throw new Exception("Extract max from empty")
}

class Balanced(val median: Int, left: PartialTree, right: PartialTree) extends Tree {
  override lazy val extractMin: (Int, PartialTree) = left match {
    case Empty => (median, right)
    case _ =>
      val (m, l) = left.extractMin
      (m, new Unbalanced(median, l, right))
  }
  override lazy val extractMax: (Int, PartialTree) = right match {
    case Empty => (median, left)
    case _ =>
      val (m, r) = right.extractMax
      val (mLeft, l) = left.extractMax
      (m, new Unbalanced(mLeft, l, PartialTreeImpl(r, median)))
  }

  override def add(value: Int): Tree = if (value < median) {
    val (m, l) = PartialTreeImpl(left, value).extractMax
    new Unbalanced(m, l, PartialTreeImpl(right, median))
  } else {
    new Unbalanced(median, left, PartialTreeImpl(right, value))
  }
}

class Unbalanced(val median: Int, left: PartialTree, right: PartialTree) extends Tree {
  override lazy val extractMin: (Int, PartialTree) = left match {
    case Empty => (median, right)
    case _ =>
      val (m, l) = PartialTreeImpl(left, median).extractMin
      val (mRight, r) = right.extractMin
      (m, new Balanced(mRight, l, r))
  }
  override lazy val extractMax: (Int, PartialTree) = right match {
    case Empty => (median, left)
    case _ =>
      val (m, r) = right.extractMax
      (m, new Balanced(median, left, r))
  }

  override def add(value: Int): Tree = if (value < median)
    new Balanced(median, PartialTreeImpl(left, value), right)
  else {
    val (m, r) = PartialTreeImpl(right, value).extractMin
    new Balanced(m, PartialTreeImpl(left, median), r)
  }
}

object Solution {
  def main(args: Array[String]): Unit = {
    val sc = new FastReader

    val t = sc.nextInt
    val roots = Array.ofDim[Tree](t)
    var root: Tree = Empty

    (0 until t).foreach(i => {
      val v = sc.nextInt
      if (v > 0) {
        root = root.add(v)
      } else {
        root = roots(i + v)
      }

      roots(i) = root
    })

    println(roots.map(_.median).mkString("\n"))
  }
}