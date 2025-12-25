// https://www.hackerrank.com/challenges/fighting-armies/problem

package functional_structures.fighting_armies

import java.io.{BufferedReader, IOException, InputStreamReader}
import java.util.StringTokenizer

import scala.collection.immutable.TreeMap

class FastReader() {
  val br: BufferedReader = new BufferedReader(new InputStreamReader(System.in))
  var st: StringTokenizer = _

  def nextInt: Int = next.toInt

  def next: String = {
    while (st == null || !st.hasMoreElements) try
      st = new StringTokenizer(br.readLine)
    catch {
      case e: IOException =>
        e.printStackTrace()
    }
    st.nextToken
  }

  def nextLong: Long = next.toLong

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

trait Army {
  def strongest: Int

  def values: TreeMap[Int, Int]

  def add(c: Int): Army = new AddedArmy(this, c)

  def remove(): Army = new RemovedArmy(this)

  def merge(that: Army): Army =
    if (this.isEmpty) that
    else if (that.isEmpty) this
    else new MergedArmy(this, that)

  def isEmpty: Boolean = false
}

case object Empty extends Army {
  override def isEmpty: Boolean = true

  override def strongest: Int = throw new Exception("Strongest of empty army")

  override def add(c: Int): Army = new SingleArmy(c)

  override def remove(): Army = throw new Exception("Remove from empty army")

  override def merge(that: Army): Army = that

  override def values: TreeMap[Int, Int] = throw new Exception("Values of empty army")
}

class SingleArmy(val strongest: Int) extends Army {
  override lazy val values: TreeMap[Int, Int] = TreeMap(strongest -> 1)

  override def remove(): Army = Empty
}

class AddedArmy(army: Army, c: Int) extends Army {
  override lazy val strongest: Int = math.max(army.strongest, c)

  override lazy val values: TreeMap[Int, Int] = army.values + (c -> (army.values.getOrElse(c, 0) + 1))
}

class RemovedArmy(army: Army) extends Army {
  override lazy val strongest: Int = values.lastKey

  override lazy val values: TreeMap[Int, Int] = {
    val (key, value) = army.values.last
    if (value == 1) {
      army.values - key
    } else {
      army.values + (key -> (value - 1))
    }
  }
}

class MergedArmy(left: Army, right: Army) extends Army {
  override lazy val strongest: Int = math.max(left.strongest, right.strongest)

  override lazy val values: TreeMap[Int, Int] = {
    left.values.foldLeft(right.values)((acc, v) => {
      val nextValue = acc.getOrElse(v._1, 0) + v._2
      acc + (v._1 -> nextValue)
    })
  }
}

object Solution {
  def main(args: Array[String]): Unit = {
    val sc = new FastReader

    val n = sc.nextInt
    val q = sc.nextInt

    val armies = Array.fill[Army](n)(Empty)

    var index = 0
    val answers = Array.ofDim[Int](q)

    (0 until q).foreach(_ => {
      val tokens = sc.nextLine.split(" ").map(_.toInt)

      val event = tokens.head
      val first = tokens(1) - 1
      lazy val second = tokens(2)

      event match {
        case 1 => //Find
          answers(index) = armies(first).strongest
          index += 1
        case 2 => //Kill
          armies(first) = armies(first).remove()
        case 3 => //Recruit
          armies(first) = armies(first).add(second)
        case 4 => //Merge
          armies(first) = armies(first).merge(armies(second - 1))
          armies(second - 1) = Empty
      }
    })

    println(answers.take(index).mkString("\n"))
  }
}