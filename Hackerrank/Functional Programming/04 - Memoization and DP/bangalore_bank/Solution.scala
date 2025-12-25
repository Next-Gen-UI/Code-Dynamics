// https://www.hackerrank.com/challenges/bangalore-bank/problem

package memoization_and_dp.bangalore_bank

import java.util.Scanner

import scala.collection.mutable

object Solution {

  private val data = mutable.Map[(Int, Int, Int), Int]()

  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val n = sc.nextInt
    val digits = (0 until n).map(_ => sc.nextInt)

    println(solve(digits))
  }

  def solve(digits: IndexedSeq[Int]): Int = {
    def press(pos: Int, digit: Int): Int = {
      def fixZero(v: Int) = if (v == 0) 10 else v

      math.abs(fixZero(pos) - fixZero(digit)) + 1
    }

    def inner(left: Int, right: Int, index: Int, acc: Int): Int = data.getOrElseUpdate((left, right, index),
      if (index >= digits.length) 0 else {
        val digit = digits(index)

        math.min(
          inner(digit, right, index + 1, press(left, digit)),
          inner(left, digit, index + 1, press(right, digit))
        )
      }
    ) + acc

    (0 to 9).map(d => inner(digits.head, d, 0, 0)).min
  }
}