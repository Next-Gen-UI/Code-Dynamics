// https://www.hackerrank.com/challenges/stocks-prediction/problem

package functional_structures.stocks_prediction

import java.io._
import java.util.StringTokenizer

import scala.collection.mutable

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

object Solution {
  def main(args: Array[String]): Unit = {
    val sc = new FastReader

    val n = sc.nextInt
    val arr = (0 until n).map(_ => sc.nextInt)

    case class Query(index: Int, d: Int, up: Int)
    val m = sc.nextInt
    val queries = (0 until m).map(i => {
      val d = sc.nextInt
      Query(i, d, arr(d) + sc.nextInt)
    })

    def updateMap(map: mutable.Map[Int, Int], left: Int, right: Int): Unit =
      if (left <= right)
        map += (left -> right)
      else
        map -= left

    case class Item(value: Int, positions: IndexedSeq[Int]) extends Ordered[Item] {
      override def compare(that: Item): Int = this.value.compareTo(that.value)
    }

    val tempMap = mutable.Map[Int, List[Int]]()
    arr.indices.foreach(i => {
      val v = arr(i)
      tempMap += v -> (i :: tempMap.getOrElse(v, Nil))
    })

    val sortedArray = tempMap.map { case (v, list) => Item(v, list.toIndexedSeq) }.toIndexedSeq.sorted

    val minBoundMap: mutable.TreeMap[Int, Int] = mutable.TreeMap[Int, Int](0 -> (n - 1))

    case class Bound(left: Int, right: Int)

    val minBounds = Array.ofDim[Bound](n)

    def update(map: mutable.TreeMap[Int, Int], pos: Int): Unit = {
      val (left, right) = map.rangeTo(pos).last
      updateMap(map, left, pos - 1)
      updateMap(map, pos + 1, right)
    }

    sortedArray.flatMap(item => {
      val res = item.positions.map(pos => {
        val (left, right) = minBoundMap.rangeTo(pos).last
        pos -> Bound(left, right)
      })

      res.indices.foreach(i => update(minBoundMap, item.positions(i)))
      res
    })
      .foreach { case (index, bound) => minBounds(index) = bound }

    val maxBoundMap = mutable.TreeMap[Int, Int](0 -> (n - 1))

    var srcIndex = sortedArray.length - 1

    val maxBounds = Array.ofDim[Int](queries.length)
    queries
      .sortBy(-_.up)
      .map(query => {

        while (srcIndex >= 0 && sortedArray(srcIndex).value > query.up) {
          sortedArray(srcIndex).positions
            .foreach(update(maxBoundMap, _))
          srcIndex -= 1
        }

        val (left, right) = maxBoundMap.rangeTo(query.d).last
        val minBound = minBounds(query.d)

        query.index -> (math.min(minBound.right, right) - math.max(minBound.left, left) + 1)
      })
      .foreach { case (index, answer) => maxBounds(index) = answer }

    println(maxBounds.mkString("\n"))
  }
}