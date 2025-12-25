// https://www.hackerrank.com/challenges/prison-transport/problem

package functional_structures.prison_transport

import java.util.Scanner

object Solution {
  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val n = sc.nextInt
    val m = sc.nextInt
    val pairs = (0 until m).map(_ => (sc.nextInt, sc.nextInt))

    sc.close()

    case class Prisoner(id: Int, links: Set[Int])

    val links: Map[Int, Prisoner] = (pairs ++ pairs.map { case (x, y) => (y, x) })
      .groupBy(_._1)
      .map { case (key, list) => key -> Prisoner(key, list.map(_._2).toSet) }

    def split(links: Map[Int, Prisoner]): List[Int] = {
      def removePrisoner(links: Map[Int, Prisoner], prisoner: Prisoner): Map[Int, Prisoner] =
        prisoner.links.foldLeft(links - prisoner.id)((links, id) => links.updated(id, Prisoner(id, links(id).links - prisoner.id)))

      case class Accumulator(links: Map[Int, Prisoner], sum: Int)
      def extractChain(links: Map[Int, Prisoner], prisoner: Prisoner, acc: Int = 0): Accumulator = {
        prisoner.links.foldLeft(Accumulator(removePrisoner(links, prisoner), 1))((acc, id) => {
          if (acc.links.contains(id)) {
            val nextAcc = extractChain(acc.links, acc.links(id))
            Accumulator(nextAcc.links, acc.sum + nextAcc.sum)
          } else acc
        })
      }

      @scala.annotation.tailrec
      def findLinks(links: Map[Int, Prisoner], sums: List[Int]): List[Int] = if (links.isEmpty) sums else {
        val prisoner = links.values.head
        val nextAcc = extractChain(links, prisoner)
        findLinks(nextAcc.links, nextAcc.sum :: sums)
      }

      findLinks(links, Nil)
    }

    val chains = split(links)
    val singleCount = n - chains.sum

    println(chains.map(v => math.ceil(math.sqrt(v)).toInt).sum + singleCount)
  }
}