// https://www.hackerrank.com/challenges/klotski/problem

package memoization_and_dp.klotski

import java.util.Scanner

import scala.collection.mutable

object Solution {

  private val data = mutable.Map[State, Acc]()
  private val dirs = List(Coord(-1, 0), Coord(1, 0), Coord(0, -1), Coord(0, 1))

  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val m = sc.nextInt
    val n = sc.nextInt
    sc.nextLine

    case class Cell(x: Int, y: Int, c: String)

    val field = (0 until m).flatMap(y => sc.nextLine.split(" ").zipWithIndex
      .map { case (s, x) => Cell(x, y, s) }
    )

    val targetChar = sc.nextLine

    val targetCoord = {
      val (targetY, targetX) = (sc.nextInt, sc.nextInt)
      Coord(targetX, targetY)
    }

    val tempList = field
      .filter(_.c.exists(_ != '.'))
      .groupBy(_.c)
      .toSeq
      .map { case (c, list) =>
        val leftX = list.map(_.x).min
        val topY = list.map(_.y).min

        val coords = list.map(cell => Coord(cell.x - leftX, cell.y - topY))

        def toId(coords: Seq[Coord]): Int = coords.map(coord => 1 << (3 * coord.y + coord.x)).sum

        (Coord(leftX, topY), Block(c, coords.toList, toId(coords)))
      }

    val tempBlocks = tempList.map(_._2).toIndexedSeq
    val symbols: mutable.Map[Coord, String] = mutable.Map[Coord, String]() ++ tempBlocks.indices.map(index => tempList(index)._1 -> tempBlocks(index).symbol)
    val blocks: Map[Int, Set[Coord]] = tempList.map(_._2).groupBy(_.id).map { case (id, list) => id -> list.head.coords.toSet }
    val initialCoord = tempList(tempBlocks.indexWhere(_.symbol == targetChar))._1
    val initialState = State(Coord(-1, -1), initialCoord,
      tempList.map { case (coord, block) => block.id -> coord }
        .groupBy(_._1)
        .map { case (id, list) => id -> list.map(_._2).toSet }
    )

    val answer = solve(blocks, initialState, initialCoord, targetCoord, n, m)

    print(answer.length)
    println(
      answer
        .map(
          mv => {
            val symbol = symbols(mv.prevCoord)
            symbols -= mv.prevCoord
            symbols += mv.nextCoord -> symbol
            s"\n$symbol (${mv.prevCoord.y},${mv.prevCoord.x}) (${mv.nextCoord.y},${mv.nextCoord.x})"
          })
        .mkString("")
    )
  }

  def solve(blocks: Map[Int, Set[Coord]], initialState: State, initialCoord: Coord, targetCoord: Coord, xSize: Int, ySize: Int): List[Movement] = {
    def worldCoord(coords: Set[Coord], topLeft: Coord): Set[Coord] = coords.map(coord => Coord(coord.x + topLeft.x, coord.y + topLeft.y))

    def isCorrect(id: Int, topLeft: Coord, field: Set[Coord]): Boolean =
      worldCoord(blocks(id), topLeft)
        .forall(coord => coord.x >= 0 && coord.x < xSize && coord.y >= 0 && coord.y < ySize && !field.contains(coord))

    data += initialState -> Acc(
      initialState.topLefts.flatMap { case (id, topLefts) => topLefts.flatMap(topLeft => worldCoord(blocks(id), topLeft)) }.toSet,
      Item(0, Nil))
    val queue = mutable.PriorityQueue[State](initialState)

    var bestMovements: List[Movement] = Nil
    var bestStep = Int.MaxValue

    while (queue.nonEmpty) {
      val state = queue.dequeue()

      val nextPairs = state.topLefts.flatMap { case (id, topLefts) => topLefts.flatMap(topLeft => {
        dirs.map(dir => {
          val nextTopLeft = Coord(topLeft.x + dir.x, topLeft.y + dir.y)
          val nextState = State(nextTopLeft,
            if (topLeft == state.currentCoord) nextTopLeft else state.currentCoord,
            state.topLefts + (id -> (state.topLefts(id) - topLeft + nextTopLeft))
          )

          val acc = data(state)
          val tempField = acc.field -- worldCoord(blocks(id), topLeft)
          val nextField = tempField ++ worldCoord(blocks(id), nextTopLeft)

          if (isCorrect(id, nextTopLeft, tempField)) {
            val isSame = topLeft == state.lastCoord
            val nextStep = if (isSame) acc.item.step else acc.item.step + 1
            val nextMovement = Movement(id, topLeft, nextTopLeft)
            val nextAcc = Acc(nextField,
              Item(nextStep,
                if (isSame) Movement(id, acc.item.movements.head.prevCoord, nextMovement.nextCoord) :: acc.item.movements.tail
                else nextMovement :: acc.item.movements
              )
            )

            nextState -> nextAcc
          }
        })
      })
      }
        .collect { case (nextState: State, nextAcc: Acc) => nextState -> nextAcc }

      nextPairs.foreach { case (nextState, nextAcc) =>
        val existingAccOpt = data.get(nextState)

        if (existingAccOpt.isEmpty || nextAcc.item.step < existingAccOpt.get.item.step) {
          if (nextAcc.item.step < bestStep) {
            if (nextState.currentCoord == targetCoord) {
              bestMovements = nextAcc.item.movements
              bestStep = nextAcc.item.step
            }

            data(nextState) = nextAcc
            queue += nextState
          }
        }
      }
    }

    bestMovements.reverse
  }

  case class Coord(x: Int, y: Int)

  case class Block(symbol: String, coords: List[Coord], id: Int) {
    def worldCoord(topLeft: Coord): List[Coord] = coords.map(coord => Coord(coord.x + topLeft.x, coord.y + topLeft.y))
  }

  case class State(lastCoord: Coord, currentCoord: Coord, topLefts: Map[Int, Set[Coord]]) extends Ordered[State] {
    lazy val _hashCode: Int = topLefts.hashCode()

    override def hashCode(): Int = 31 * lastCoord.hashCode() + _hashCode

    override def compare(that: State): Int = data(that).item.step.compareTo(data(this).item.step)
  }

  case class Movement(id: Int, prevCoord: Coord, nextCoord: Coord)

  case class Item(step: Int, movements: List[Movement])

  case class Acc(field: Set[Coord], item: Item)

}