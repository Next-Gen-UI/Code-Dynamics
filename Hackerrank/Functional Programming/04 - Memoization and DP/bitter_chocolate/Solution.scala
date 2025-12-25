// https://www.hackerrank.com/challenges/bitter-chocolate/problem

package memoization_and_dp.bitter_chocolate

import java.util.Scanner

import scala.collection.mutable

object Solution {

  private val data = mutable.Map[State, Boolean]()

  def solve(state: State): Boolean = data.getOrElseUpdate(state, state == State(0, 0, 0) ||
    state.a > 0 && (0 until state.a).exists(x => !solve(eat(Coord(x, 0), state))) ||
    state.b > 0 && (0 until state.b).exists(x => !solve(eat(Coord(x, 1), state))) ||
    state.c > 0 && (0 until state.c).exists(x => !solve(eat(Coord(x, 2), state)))
  )

  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val t = sc.nextInt
    println((0 until t).map(_ => State(sc.nextInt, sc.nextInt, sc.nextInt))
      .map(s => if (solve(s)) "WIN" else "LOSE").mkString("\n"))
  }

  private def eat(coord: Coord, state: State): State = coord.y match {
    case 2 => State(state.a, state.b, coord.x)
    case 1 => State(state.a, coord.x, math.min(state.c, coord.x))
    case _ => State(coord.x, math.min(state.b, coord.x), math.min(state.c, coord.x))
  }

  case class State(a: Int, b: Int, c: Int)

  case class Coord(x: Int, y: Int)

}