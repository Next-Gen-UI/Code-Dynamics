// https://www.hackerrank.com/challenges/huge-gcd-fp/problem

package ad_hoc.huge_gcd_fp

import java.util.Scanner

object Solution {

  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val n = sc.nextInt
    val as = (0 until n).map(_ => sc.nextInt)
    val m = sc.nextInt
    val bs = (0 until m).map(_ => sc.nextInt)

    sc.close()

    val maxValue = 10000
    val primeNumbers: IndexedSeq[Int] = (2 to maxValue).filter(v => BigInt(v).isProbablePrime(10))

    @scala.annotation.tailrec
    def toPrimes(value: Int, acc: Map[Int, Int] = Map(), index: Int = 0): Map[Int, Int] = if (index < primeNumbers.length) {
      val prime = primeNumbers(index)
      if (value % prime == 0) toPrimes(value / prime, {
        acc + (prime -> (acc.getOrElse(prime, 0) + 1))
      }, index)
      else toPrimes(value, acc, index + 1)
    }
    else acc

    def seqToPrimes(seq: Seq[Int]): Map[Int, Int] = seq.foldLeft(Map[Int, Int]())((acc, v) => toPrimes(v, acc))

    val a = seqToPrimes(as)
    val b = seqToPrimes(bs)

    val gcd = a.collect { case (key, value) if b.contains(key) => key -> math.min(value, b(key)) }

    val modulo = 1000000007
    println(gcd.foldLeft(1)((acc, pair) => {
      (BigInt(pair._1).modPow(pair._2, modulo) * acc % modulo).toInt
    }))
  }
}