// https://www.hackerrank.com/challenges/game-of-kyles/problem

package ad_hoc.game_of_kyles

import java.util.Scanner

import scala.collection.mutable

object Solution {
  private val data = mutable.Map[Int, Int]()

  def mex(states: Set[Int]): Int = LazyList.from(0).find(i => !states.contains(i)).get

  def number(v: Int): Int = data.getOrElseUpdate(v, {
    mex((0 until v).map(i => number(i) ^ number(v - i - 1)).toSet ++
      (0 to v - 2).map(i => number(i) ^ number(v - i - 2)).toSet)
  })

  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val t = sc.nextInt
    (0 until t).foreach(_ => {
      sc.nextInt
      sc.nextLine
      val s = sc.nextLine

      val pins = {
        case class Acc(pins: List[Int] = List(0))
        s.foldLeft(Acc())((acc, c) => {
          if (c == 'I') {
            Acc((acc.pins.head + 1) :: acc.pins.tail)
          } else {
            Acc(0 :: acc.pins)
          }
        }).pins
          .filter(_ != 0)
      }

      println(if (pins.map(number).reduce(_ ^ _) == 0) "LOSE" else "WIN")
    })
  }
}