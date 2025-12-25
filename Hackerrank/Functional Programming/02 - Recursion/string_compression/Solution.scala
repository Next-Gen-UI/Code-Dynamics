// https://www.hackerrank.com/challenges/string-compression/problem

package recursion.string_compression

import scala.io.StdIn

object Solution {
  def main(args: Array[String]): Unit = {
    case class Item(c: Char, count: Int)

    println(StdIn.readLine
      .foldLeft(List[Item]()) {
        case (Item(c, count) :: cs, next) if c == next => Item(c, count + 1) :: cs
        case (list, next) => Item(next, 1) :: list
      }
      .map {
        case Item(c, 1) => c.toString
        case Item(c, n) => c.toString + n
      }
      .reduce((a, b) => b + a))
  }
}