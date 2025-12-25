// https://www.hackerrank.com/challenges/lambda-march-concave-polygon/problem

package recursion.lambda_march_concave_polygon

import java.util.Scanner

import scala.collection.mutable

object Orientation extends Enumeration {
  type Orientation = Value
  val ColLinear, Clockwise, Counterclockwise = Value
}

object Solution {

  import Orientation.Orientation

  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)
    val n = sc.nextInt

    case class Point(x: Int, y: Int)

    val initialPoints = (0 until n).map(_ => Point(sc.nextInt, sc.nextInt))

    val bottommostIndex = initialPoints.indices.reduce((accIndex, pIndex) => {
      val acc = initialPoints(accIndex)
      val p = initialPoints(pIndex)
      if (acc.y < p.y || acc.y == p.y && acc.x < p.x) accIndex else pIndex
    })

    val basePoint = initialPoints(bottommostIndex)

    def polarAngle(point: Point) = math.atan2(point.y - basePoint.y, point.x - basePoint.x)

    def distance2(p0: Point, p1: Point) = {
      val dx = p1.x - p0.x
      val dy = p1.y - p0.y

      dx * dx + dy * dy
    }

    val points = initialPoints.indices.filter(_ != bottommostIndex).map(initialPoints(_))
      .sortWith((p0, p1) => {
        val polar0 = polarAngle(p0)
        val polar1 = polarAngle(p1)

        polar0 < polar1 || polar0 == polar1 && distance2(p0, basePoint) < distance2(p1, basePoint)
      })
      .toList

    val filteredPoints = basePoint :: points

    val orderedCount = 3
    val stack = mutable.Stack[Point]()
    stack.pushAll(filteredPoints.take(orderedCount))

    def orientation(p0: Point, p1: Point, p2: Point): Orientation = {
      val cross = (p1.y - p0.y) * (p2.x - p1.x) - (p1.x - p0.x) * (p2.y - p1.y)

      if (cross == 0) Orientation.ColLinear else if (cross > 0) Orientation.Clockwise else Orientation.Counterclockwise
    }

    filteredPoints.drop(orderedCount)
      .foreach(p => {
        while (orientation(stack.tail.head, stack.head, p) != Orientation.Counterclockwise)
          stack.pop()
        stack.push(p)
      })

    println(if (stack.size == n) "NO" else "YES")
  }
}