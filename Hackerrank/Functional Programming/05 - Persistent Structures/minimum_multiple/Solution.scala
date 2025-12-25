// https://www.hackerrank.com/challenges/minimum-multiple/problem

package persistent_structures.minimum_multiple

import java.io.{BufferedReader, IOException, InputStreamReader}
import java.util.StringTokenizer

import scala.collection.mutable
import scala.reflect.ClassTag

class FastReader() {
  val br: BufferedReader = new BufferedReader(new InputStreamReader(System.in))
  var st: StringTokenizer = _

  def nextInt: Int = next.toInt

  def nextLong: Long = next.toLong

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

  def nextDouble: Double = next.toDouble

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

abstract class SegmentTree[Node, Value](seq: IndexedSeq[Value])(implicit tag: ClassTag[Node]) {
  private val len: Int = seq.length

  val nodes: Array[Node] = {
    val defaultNode = emptyNode
    Array.fill[Node](4 * len)(defaultNode)
  }

  def build(v: Int = 1, left: Int = 0, right: Int = len - 1): Unit = {
    nodes(v) = if (left == right)
      valueToNode(v, seq(left))
    else {
      val middle = (left + right) >> 1
      build(2 * v, left, middle)
      build(2 * v + 1, middle + 1, right)
      combine(nodes(2 * v), nodes(2 * v + 1))
    }
  }

  def valueToNode: (Int, Value) => Node

  def add(index: Int, value: Value): Unit = traverse(index, i => nodes(i) = valueToNode(i, value))

  private def traverse(index: Int, f: Int => Any): Unit = {
    def inner(v: Int = 1, left: Int = 0, right: Int = len - 1): Unit = if (left == right) f(v)
    else {
      val middle = (left + right) >> 1
      if (index <= middle) inner(2 * v, left, middle)
      else inner(2 * v + 1, middle + 1, right)

      nodes(v) = combine(nodes(2 * v), nodes(2 * v + 1))
    }

    inner()
  }

  def remove(index: Int, value: Value): Unit = traverse(index, nodes(_) = emptyNode)

  def emptyNode: Node

  def combine(leftNode: Node, rightNode: Node): Node

  def query[B](leftBound: Int, rightBound: Int, f: (Int, Int, Node) => B, comb: (B, B) => B): B = {
    def inner(leftBound: Int, rightBound: Int, v: Int = 1, left: Int = 0, right: Int = len - 1): B =
      if (leftBound == left && rightBound == right) {
        f(leftBound, rightBound, nodes(v))
      } else {
        val middle = (left + right) >> 1

        if (rightBound <= middle)
          inner(leftBound, rightBound, 2 * v, left, middle)
        else if (leftBound > middle)
          inner(leftBound, rightBound, 2 * v + 1, middle + 1, right)
        else comb(
          inner(leftBound, middle, 2 * v, left, middle),
          inner(middle + 1, rightBound, 2 * v + 1, middle + 1, right)
        )
      }

    inner(leftBound, rightBound)
  }

  build()
}

case class Code(seq: IndexedSeq[Int]) {
  def toInt: Int = seq.indices.foldLeft(1)((acc, i) => {
    var n = seq(i)
    var res: Long = acc
    val prime = Code.primeNumbers(i)
    while (n > 0) {
      res = (res * prime) % Code.modulo
      n -= 1
    }
    res.toInt
  })
}

object Code {
  val maxValue = 100
  val primeNumbers: IndexedSeq[Int] = (2 to maxValue).filter(v => BigInt(v).isProbablePrime(10))
  private val modulo = 1000000007

  def apply(value: Int): Code = {
    @scala.annotation.tailrec
    def toPrimes(value: Int, index: Int, acc: List[Int]): List[Int] = if (index < primeNumbers.length) {
      val prime = primeNumbers(index)
      if (value % prime == 0) toPrimes(value / prime, index, (acc.head + 1) :: acc.tail)
      else toPrimes(value, index + 1, 0 :: acc)
    }
    else acc

    new Code(toPrimes(value, 0, 0 :: Nil).tail.reverse.toIndexedSeq)
  }
}

class MaxTree(seq: IndexedSeq[Int]) extends SegmentTree[Code, Int](seq) {
  override lazy val emptyNode: Code = Code(1)

  override def valueToNode: (Int, Int) => Code = (i, v) => {
    val code = Code(v)
    val node = nodes(i)
    Code(code.seq.indices.map(i => code.seq(i) + node.seq(i)))
  }

  def query(leftBound: Int, rightBound: Int): Int = super.query(leftBound, rightBound,
    (_, _, code) => code, combine).toInt

  override def combine(leftNode: Code, rightNode: Code): Code =
    Code(leftNode.seq.indices.map(i => math.max(leftNode.seq(i), rightNode.seq(i))))
}

object Solution {
  def main(args: Array[String]): Unit = {
    val sc = new FastReader

    val n = sc.nextInt
    val values = (0 until n).map(_ => sc.nextInt)

    val tree = new MaxTree(values)

    val q = sc.nextInt
    val answer = new mutable.Queue[Int]()

    (0 until q).foreach(_ => {
      val tokens = sc.nextLine.split(" ")
      val op = tokens.head.head
      val left = tokens(1).toInt
      val right = tokens(2).toInt

      op match {
        case 'Q' => answer.enqueue(tree.query(left, right))
        case 'U' => tree.add(left, right)
      }
    })

    println(answer.mkString("\n"))
  }
}