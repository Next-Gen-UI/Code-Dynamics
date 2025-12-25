// https://www.hackerrank.com/challenges/mango/problem

package ad_hoc.mango

import java.util.Scanner

object Solution {

  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val n = sc.nextInt
    val m = sc.nextLong
    val a = (0 until n).map(_ => sc.nextInt)
    val h = (0 until n).map(_ => sc.nextInt)

    val friends = a.indices.map(i => Friend(a(i), h(i)))

    sc.close()

    println(solve(friends, m))
  }

  def solve(friends: IndexedSeq[Friend], m: Long): Int = {
    @scala.annotation.tailrec
    def inner(min: Int, max: Int): Int = if (min <= max) {
      val middle = (min + max) / 2

      val sortedFriends = friends.sortBy(_.appetite(middle))
      val totalAppetite = sortedFriends.take(middle)
        .map(_.appetite(middle))
        .sum

      val (nextMin, nextMax) = if (totalAppetite <= m) (middle + 1, max) else (min, middle - 1)
      inner(nextMin, nextMax)
    } else max

    inner(0, friends.length)
  }

  case class Friend(a: Long, h: Long) {
    def appetite(cnt: Int): Long = a + (cnt - 1) * h
  }

}