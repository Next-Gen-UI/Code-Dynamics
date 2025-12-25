// https://www.hackerrank.com/challenges/captain-prime/problem

package ad_hoc.captain_prime

import java.util.Scanner

object Solution {
  def toDigits(p: Int): List[Int] = {
    var current = p
    (0 until 7).map(_ => {
      val res = current % 10
      current /= 10
      res
    })
      .reverse
      .dropWhile(_ == 0)
      .toList
  }

  def fromDigits(seq: Seq[Int]): Int = {
    def innerFromDigits(seq: Seq[Int]): Int = seq match {
      case Seq(v) => v
      case _ => 10 * innerFromDigits(seq.tail) + seq.head
    }

    innerFromDigits(seq.reverse)
  }

  @scala.annotation.tailrec
  def isRight(n: Int): Boolean = n == 0 || n % 10 != 0 && BigInt(n).isProbablePrime(5) && isRight(n / 10)

  def isLeft(n: Int): Boolean = n == 0 || {
    @scala.annotation.tailrec
    def innerIsLeft(digits: List[Int]): Boolean = digits.isEmpty ||
      BigInt(fromDigits(digits)).isProbablePrime(5) && innerIsLeft(digits.tail)

    val digits = toDigits(n)
    !digits.contains(0) && innerIsLeft(digits)
  }

  def solve(n: Int): String = (isLeft(n), isRight(n)) match {
    case (false, false) => "DEAD"
    case (false, true) => "RIGHT"
    case (true, false) => "LEFT"
    case (true, true) => "CENTRAL"
  }

  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val t = sc.nextInt
    (0 until t).foreach(_ => {
      val n = sc.nextInt
      println(solve(n))
    })
  }
}