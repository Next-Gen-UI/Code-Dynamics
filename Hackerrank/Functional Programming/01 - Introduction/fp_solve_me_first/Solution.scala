// https://www.hackerrank.com/challenges/fp-solve-me-first/problem

package introduction.fp_solve_me_first

object Solution {
  def main(args: Array[String]): Unit = {
    println(io.Source.stdin.getLines().take(2).map(_.toInt).sum)
  }
}