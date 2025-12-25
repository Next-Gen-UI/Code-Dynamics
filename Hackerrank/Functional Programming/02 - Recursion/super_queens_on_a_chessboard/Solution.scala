// https://www.hackerrank.com/challenges/super-queens-on-a-chessboard/problem

package recursion.super_queens_on_a_chessboard

import java.util.Scanner

object Solution {

  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val n = sc.nextInt

    sc.close()

    println(solve(n))
  }

  def solve(n: Int): Int = {
    def inner(restCount: Int, acc: List[Coord]): Int = if (restCount == 0) 1 else {
      (0 until n).map(y => {
        val coord = Coord(n - restCount, y)
        if (isCorrect(coord, acc))
          inner(restCount - 1, coord :: acc)
        else 0
      }).sum
    }

    inner(n, Nil)
  }

  def isCorrect(coord: Coord, queens: List[Coord]): Boolean =
    queens.forall(
      quinn => {
        val dx = coord.x - quinn.x
        val dy = math.abs(coord.y - quinn.y)

        coord.y != quinn.y &&
          dy != dx &&
          !(dx == 2 && dy == 1 || dx == 1 && dy == 2)
      }
    )

  case class Coord(x: Int, y: Int)

}