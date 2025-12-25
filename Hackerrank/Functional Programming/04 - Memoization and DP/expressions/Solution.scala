// https://www.hackerrank.com/challenges/expressions/problem

package memoization_and_dp.expressions

import java.util.Scanner

import scala.collection.mutable

object Solution {

  private val data = mutable.Map[(Long, List[Int]), Acc]()

  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val n = sc.nextInt
    val numbers = (0 until n).map(_ => sc.nextInt).toList

    println(solve(numbers))

    sc.close()
  }

  def solve(numbers: List[Int]): String = {
    val operations = List('+', '-', '*')

    val modulo = 101

    def inner(numbers: List[Int], acc: Acc): Acc = data.getOrElseUpdate((acc.result % modulo, numbers),
      numbers match {
        case Nil =>
          if (acc.result % modulo == 0)
            Acc(acc.result, acc.operations, found = true)
          else acc
        case v :: vs =>
          operations.foldLeft(Acc(0, Nil))((innerAcc, op) => if (innerAcc.found) innerAcc else {
            val nextResult = op match {
              case '+' => acc.result + v
              case '-' => acc.result - v
              case '*' => acc.result * v
            }
            inner(vs, Acc(nextResult, op :: acc.operations))
          })
      }
    )

    val answer = inner(numbers.tail, Acc(numbers.head, Nil)).operations.reverse
    numbers.tail.zip(answer).foldLeft(new StringBuilder(numbers.head.toString))((acc, v) => acc ++= s"${v._2}${v._1}").toString
  }

  case class Acc(result: Long, operations: List[Char], found: Boolean = false)

}