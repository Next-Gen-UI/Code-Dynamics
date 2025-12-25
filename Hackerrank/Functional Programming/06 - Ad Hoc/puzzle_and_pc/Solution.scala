// https://www.hackerrank.com/challenges/puzzle-and-pc/problem

package ad_hoc.puzzle_and_pc

import java.util.Scanner

object Solution {

  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val m = sc.nextInt
    val r = sc.nextInt - 1
    val c = sc.nextInt - 1

    sc.close()

    val n = BigInt(2).pow(m).toInt
    println(solve(n, Coord(r, c)).mkString("\n"))
  }

  def solve(n: Int, empty: Coord): List[Trimino] = {
    def inner(n: Int, empty: Coord, origin: Coord = Coord(0, 0), acc: List[Trimino] = Nil): List[Trimino] = if (n == 2) {
      ((empty match {
        case Coord(0, 0) => Trimino(Coord(0, 1), Coord(1, 0), Coord(1, 1))
        case Coord(0, 1) => Trimino(Coord(0, 0), Coord(1, 0), Coord(1, 1))
        case Coord(1, 0) => Trimino(Coord(0, 1), Coord(0, 0), Coord(1, 1))
        case Coord(1, 1) => Trimino(Coord(0, 1), Coord(1, 0), Coord(0, 0))
        case _ => throw new Exception("Impossible")
      }) + origin) :: acc
    } else {
      val nextN = n / 2
      val inLeft = empty.c < nextN
      val inTop = empty.r < nextN

      val topLeftAcc = inner(nextN, if (inLeft && inTop) empty else Coord(nextN - 1, nextN - 1), origin, acc)

      val localTopRightOrigin = Coord(0, nextN)
      val topRightOrigin = origin + localTopRightOrigin
      val topRightAcc = inner(nextN, if (!inLeft && inTop) empty - localTopRightOrigin else Coord(nextN - 1, 0), topRightOrigin, topLeftAcc)

      val localBottomLeftOrigin = Coord(nextN, 0)
      val bottomLeftOrigin = origin + localBottomLeftOrigin
      val bottomLeftAcc = inner(nextN, if (inLeft && !inTop) empty - localBottomLeftOrigin else Coord(0, nextN - 1), bottomLeftOrigin, topRightAcc)

      val localBottomRightOrigin = Coord(nextN, nextN)
      val bottomRightOrigin = origin + localBottomRightOrigin
      val bottomRightAcc = inner(nextN, if (!inLeft && !inTop) empty - localBottomRightOrigin else Coord(0, 0), bottomRightOrigin, bottomLeftAcc)

      val trimino = ((inLeft, inTop) match {
        case (true, true) => Trimino(Coord(nextN - 1, nextN), Coord(nextN, nextN - 1), Coord(nextN, nextN))
        case (false, true) => Trimino(Coord(nextN - 1, nextN - 1), Coord(nextN, nextN - 1), Coord(nextN, nextN))
        case (true, false) => Trimino(Coord(nextN - 1, nextN), Coord(nextN - 1, nextN - 1), Coord(nextN, nextN))
        case (false, false) => Trimino(Coord(nextN - 1, nextN), Coord(nextN, nextN - 1), Coord(nextN - 1, nextN - 1))
      }) + origin

      trimino :: bottomRightAcc
    }

    inner(n, empty)
  }

  case class Coord(r: Int, c: Int) {
    def +(that: Coord): Coord = Coord(r + that.r, c + that.c)

    def -(that: Coord): Coord = Coord(r - that.r, c - that.c)

    override def toString: String = s"${r + 1} ${c + 1}"
  }

  case class Trimino(coord0: Coord, coord1: Coord, coord2: Coord) {
    def +(coord: Coord): Trimino = Trimino(coord0 + coord, coord1 + coord, coord2 + coord)

    override def toString: String = s"$coord0 $coord1 $coord2"
  }

}