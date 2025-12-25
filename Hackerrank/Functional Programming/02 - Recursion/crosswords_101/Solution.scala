// https://www.hackerrank.com/challenges/crosswords-101/problem

package recursion.crosswords_101

import java.util.Scanner

object Solution {
  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val size = 10

    val field = (0 until size).map(_ => sc.nextLine.toIndexedSeq)
    val words = sc.nextLine.split(';')
      .groupBy(_.length)
      .map { case (len, list) => len -> list.toSet }

    sc.close()

    val plus = '+'

    def isWall(x: Int, y: Int) = x < 0 || x >= size || y < 0 || y >= size || field(y)(x) == plus

    case class Coord(x: Int, y: Int)
    case class Cross(coord: Coord, letterIndex: Int)
    case class Item(len: Int, head: Coord, isHorizontal: Boolean, crosses: List[Cross])
    object Item {
      def of(x: Int, y: Int, isHorizontal: Boolean): Item = {
        def inner(xx: Int, yy: Int): Item = if (isWall(xx, yy)) Item(0, Coord(x, y), isHorizontal, Nil) else {
          val (dx, dy) = if (isHorizontal) (1, 0) else (0, 1)

          val isCross = !isWall(xx + dy, yy + dx) || !isWall(xx - dy, yy - dx)
          val preItem = inner(xx + dx, yy + dy)

          Item(preItem.len + 1, Coord(x, y), isHorizontal, if (isCross) Cross(Coord(xx, yy), if (isHorizontal) xx - x else yy - y) :: preItem.crosses else preItem.crosses)
        }

        inner(x, y)
      }
    }

    val items: List[Item] = field.indices.flatMap(y => {
      field(y).indices.flatMap(x => {
        (if (isWall(x - 1, y) && !isWall(x, y) && !isWall(x + 1, y)) List(Item.of(x, y, isHorizontal = true)) else List[Item]()) ++
          (if (isWall(x, y - 1) && !isWall(x, y) && !isWall(x, y + 1)) List(Item.of(x, y, isHorizontal = false)) else List[Item]())
      })
    }).toList

    case class Pair(item: Item, word: String)

    def solve(items: List[Item], words: Map[Int, Set[String]], acc: List[Pair]): List[Pair] = items match {
      case Nil =>
        val isCorrect = acc.flatMap(pair => pair.item.crosses.map(cross => cross.coord -> pair.word(cross.letterIndex)))
          .groupBy { case (coord, _) => coord }
          .map { case (_, list) => list.map { case (_, c) => c }.toSet.size == 1 }
          .reduce(_ && _)

        if (isCorrect) acc else Nil
      case item :: rest =>
        words(item.len).map(word => {
          solve(rest, words.updated(item.len, words(item.len) - word), Pair(item, word) :: acc)
        })
          .reduce((a, b) => if (a.isEmpty) b else a)
    }

    val answer = Array.fill(size, size)(plus)
    solve(items, words, Nil)
      .foreach { case Pair(item, word) =>
        val (dx, dy) = if (item.isHorizontal) (1, 0) else (0, 1)
        word.indices.foreach(i => answer(item.head.y + dy * i)(item.head.x + dx * i) = word(i))
      }

    println(answer.map(_.mkString("")).mkString("\n"))
  }
}