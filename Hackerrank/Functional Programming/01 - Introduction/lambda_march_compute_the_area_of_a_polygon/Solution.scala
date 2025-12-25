// https://www.hackerrank.com/challenges/lambda-march-compute-the-area-of-a-polygon/problem

package introduction.lambda_march_compute_the_area_of_a_polygon

import java.util.Scanner

object Solution {
  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)
    val n = sc.nextInt

    case class Coord(x: Double, y: Double)

    val coords = (0 until n).map(_ => Coord(sc.nextInt, sc.nextInt))

    val square = math.abs((coords.last +: coords).sliding(2)
      .map(list => (list.head, list.last))
      .map { case (c0, c1) => c0.x * c1.y - c0.y * c1.x }
      .sum / 2)

    println(square)
  }
}