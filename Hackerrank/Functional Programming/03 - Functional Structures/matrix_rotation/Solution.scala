// https://www.hackerrank.com/challenges/matrix-rotation/problem

package functional_structures.matrix_rotation

import java.util.Scanner

object Solution {

  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val M = sc.nextInt
    val N = sc.nextInt
    val r = sc.nextInt
    val matrix = (0 until M).map(_ => (0 until N).map(_ => sc.nextInt))

    sc.close()

    println((0 until M).map(y => {
      (0 until N).map(x => {
        def normalize(v: Int, size: Int) = if (v < size / 2) v else size - 1 - v

        val normX = normalize(x, N)
        val normY = normalize(y, M)

        val LoopIndex = math.min(normX, normY)

        def sideSize(size: Int) = size - 2 * LoopIndex

        val loopN = sideSize(N)
        val loopM = sideSize(M)
        val loopLength = 2 * (loopN - 1 + loopM - 1)

        val offset = r % loopLength

        def singleShift(v: Int, lower: Int, upper: Int, dir: Int, offset: Int): (Int, Int) = {
          val nextV = v + offset * dir
          if (nextV < upper) {
            if (nextV >= lower) (nextV, 0)
            else (lower, offset - (v - lower))
          } else (upper - 1, offset - (upper - 1 - v))
        }

        @scala.annotation.tailrec
        def shift(x: Int, y: Int, offset: Int): (Int, Int) = {
          val (nextX, nextY, nextOffset) = (x, y) match {
            case _ if y == LoopIndex && x < N - LoopIndex - 1 =>
              val (nextX, nextOffset) = singleShift(x, LoopIndex, N - LoopIndex, 1, offset)
              (nextX, y, nextOffset)
            case _ if x == N - LoopIndex - 1 && y < M - LoopIndex - 1 =>
              val (nextY, nextOffset) = singleShift(y, LoopIndex, M - LoopIndex, 1, offset)
              (x, nextY, nextOffset)
            case _ if y == M - LoopIndex - 1 && x > LoopIndex =>
              val (nextX, nextOffset) = singleShift(x, LoopIndex, N - LoopIndex, -1, offset)
              (nextX, y, nextOffset)
            case _ if x == LoopIndex && y > LoopIndex =>
              val (nextY, nextOffset) = singleShift(y, LoopIndex, M - LoopIndex, -1, offset)
              (x, nextY, nextOffset)
          }

          if (nextOffset == 0) (nextX, nextY) else shift(nextX, nextY, nextOffset)
        }

        val (srcX, srcY) = shift(x, y, offset)
        matrix(srcY)(srcX)
      }).mkString(" ")
    }).mkString("\n"))
  }
}