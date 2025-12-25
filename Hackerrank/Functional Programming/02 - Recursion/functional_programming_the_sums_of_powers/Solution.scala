// https://www.hackerrank.com/challenges/functional-programming-the-sums-of-powers/problem

package recursion.functional_programming_the_sums_of_powers

import java.util.Scanner

object Solution {

  def solve(x: Int, numbers: List[Int]): Int = if (x < 0) 0 else if (x == 0) 1 else {
    numbers match {
      case Nil => 0
      case c :: cs => solve(x - c, cs) + solve(x, cs)
    }
  }

  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val x = sc.nextInt
    val n = sc.nextInt

    sc.close()

    val numbers = LazyList.from(1).map(i => BigInt(i).pow(n)).takeWhile(_ <= x).map(_.toInt).toList

    println(solve(x, numbers))
  }
}