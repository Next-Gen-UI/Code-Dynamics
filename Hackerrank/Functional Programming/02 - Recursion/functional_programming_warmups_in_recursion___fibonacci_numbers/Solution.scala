// https://www.hackerrank.com/challenges/functional-programming-warmups-in-recursion---fibonacci-numbers/problem

package recursion.functional_programming_warmups_in_recursion___fibonacci_numbers

object Solution {

  def fibonacci(x: Int): Int = x match {
    case 1 => 0
    case 2 => 1
    case _ => fibonacci(x - 1) + fibonacci(x - 2)
  }

  def main(args: Array[String]): Unit = {
    /** This will handle the input and output **/
    println(fibonacci(readInt()))
  }

  // readInt() is deprecated in Scala 13, but it is called by HackerRank's predefined code.
  // So it is added to fix the issue.
  def readInt(): Int = scala.io.StdIn.readInt()
}