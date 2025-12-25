// https://www.hackerrank.com/challenges/lambda-march-compute-the-perimeter-of-a-polygon/problem

package introduction.lambda_march_compute_the_perimeter_of_a_polygon

import java.util.Scanner

object Solution {
  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)
    val n = sc.nextInt

    val coords = (0 until n).map(_ => (sc.nextInt, sc.nextInt))
    println((coords.last +: coords).sliding(2)
      .map(seq => {
        val v1 = seq.head
        val v2 = seq.last
        math.sqrt(math.pow(v1._1 - v2._1, 2) + math.pow(v1._2 - v2._2, 2))
      }).sum)
  }
}