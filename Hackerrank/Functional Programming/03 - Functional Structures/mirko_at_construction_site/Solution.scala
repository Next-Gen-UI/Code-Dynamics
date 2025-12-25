// https://www.hackerrank.com/challenges/mirko-at-construction-site/problem

package functional_structures.mirko_at_construction_site

import java.util.Scanner

object Solution {
  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    sc.nextInt
    val q = sc.nextInt
    sc.nextLine
    val initials = sc.nextLine.split(" ").map(_.toInt)
    val steps = sc.nextLine.split(" ").map(_.toInt)

    case class Query(index: Int, time: Int)
    val queries = (0 until q).map(Query(_, sc.nextInt)).sortBy(_.time)

    sc.close()

    case class Building(index: Int, initial: Long, step: Long) extends Ordered[Building] {
      override def compare(that: Building): Int = {
        if (this.step < that.step) -1
        else if (this.step > that.step) 1
        else if (this.initial < that.initial) -1
        else if (this.initial > that.initial) 1
        else this.index.compareTo(that.index)
      }

      def height(time: Long): Long = initial + step * time
    }

    val buildings: List[Building] = initials.indices.map(i => Building(i + 1, initials(i), steps(i)))
      .groupBy(_.step)
      .map { case (_, list) => list.maxBy(_.initial) }
      .toList
      .sorted

    def intersection(b0: Building, b1: Building) = (b1.initial.toDouble - b0.initial) / (b0.step.toDouble - b1.step)

    @scala.annotation.tailrec
    def prepare(buildings: List[Building], acc: List[Building]): List[Building] = buildings match {
      case Nil => acc
      case v :: Nil => v :: acc
      case v0 :: v1 :: vs =>
        val highest = acc.head
        val inter0 = intersection(highest, v0)
        val inter1 = intersection(highest, v1)

        if (inter0 < inter1 || inter0 == inter1 && v0.index > v1.index)
          prepare(v1 :: vs, v0 :: acc)
        else if (acc.tail.isEmpty)
          prepare(v1 :: vs, acc)
        else
          prepare(acc.head :: v1 :: vs, acc.tail)
    }

    val highBuildings = prepare(buildings.tail, List(buildings.head)).reverse

    case class Pair(queryIndex: Int, highestIndex: Int)
    case class Acc(buildings: List[Building], highestIndices: List[Pair])
    val answer = queries.foldLeft(Acc(highBuildings, Nil))((acc, q) => {
      @scala.annotation.tailrec
      def findHighest(buildings: List[Building]): Acc = buildings match {
        case b :: Nil => Acc(acc.buildings, Pair(q.index, b.index) :: acc.highestIndices)
        case b0 :: b1 :: bs =>
          val inter = intersection(b0, b1)
          if (inter > q.time || inter == q.time && b0.index > b1.index)
            Acc(buildings, Pair(q.index, b0.index) :: acc.highestIndices)
          else
            findHighest(b1 :: bs)
        case Nil => throw new Exception("Impossible")
      }

      findHighest(acc.buildings)
    })

    println(answer.highestIndices
      .sortBy(_.queryIndex)
      .map(_.highestIndex)
      .mkString("\n")
    )
  }
}