// https://www.hackerrank.com/challenges/order-exercises/problem

package functional_structures.order_exercises

import java.util.Scanner

import scala.collection.immutable.Queue

object Solution {
  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    sc.nextInt
    val k = sc.nextInt
    sc.nextLine
    val a = sc.nextLine.split(" ").map(_.toInt)

    sc.close()

    case class Acc(list: List[Int])
    val sentinel = Int.MaxValue / 2
    val temp = a.foldLeft(Acc(List(-sentinel, sentinel)))((acc, v) => acc.list match {
      case Nil if v < 0 => acc
      case Nil => Acc(v :: Nil)
      case list@x :: xs => if ((v < 0) == (x < 0)) Acc(x + v :: xs) else Acc(v :: list)
    }).list

    val compressed = if (temp.head < 0) temp.tail else temp

    println(solve(compressed)
      .sortBy(-_)
      .take(k)
      .mkString("\n")
    )
  }

  def solve(seq: List[Int]): List[Int] = {
    case class Accumulator(totalSum: Int, queue: Queue[Int], answer: List[Int])

    @scala.annotation.tailrec
    def inner(seq: List[Int], acc: Accumulator): List[Int] = {
      seq match {
        case Nil =>
          acc.answer
        case xPos :: xs if acc.queue.isEmpty =>
          //empty queue
          inner(xs, Accumulator(xPos, Queue(xPos), acc.answer))
        case xNeg :: xPos :: xs =>
          val (nextSeq, nextAcc) = {
            val sNeg = acc.totalSum + xNeg
            if (sNeg <= 0) {
              val (firstPos, tempQueue) = acc.queue.dequeue

              if (tempQueue.isEmpty) {
                //All items in queue are processed.
                (xs, Accumulator(xPos, Queue(xPos), firstPos :: acc.answer))
              } else {
                if (acc.totalSum >= firstPos) {
                  //Glue items in queue.
                  (xs, Accumulator(xPos, Queue(xPos), acc.totalSum :: acc.answer))
                } else {
                  //Separate first item in queue.
                  val (_, nextQueue) = tempQueue.dequeue
                  (nextQueue.toList ++ seq, Accumulator(0, Queue(), firstPos :: acc.answer))
                }
              }
            } else {
              val sPos = sNeg + xPos
              if (sPos >= acc.queue.head) {
                //Glue
                (xs, Accumulator(sPos, Queue(sPos), acc.answer))
              } else {
                //Uncertain
                (xs, Accumulator(sPos, acc.queue.enqueue(xNeg).enqueue(xPos), acc.answer))
              }
            }
          }

          inner(nextSeq, nextAcc)
      }
    }

    inner(seq, Accumulator(0, Queue(), Nil))
  }
}