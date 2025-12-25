// https://www.hackerrank.com/challenges/kmp-fp/problem

package functional_structures.kmp_fp

import java.util.Scanner

object Solution {
  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val t = sc.nextInt
    sc.nextLine

    (0 until t).foreach(_ => {
      val s = sc.nextLine
      val w = sc.nextLine

      println(if (search(w, s).isEmpty) "NO" else "YES")
    })

    sc.close()
  }

  def search(pat: String, txt: String): List[Int] = {
    val lps = buildLps(pat)

    @scala.annotation.tailrec
    def inner(i: Int, j: Int, acc: List[Int]): List[Int] = if (i < txt.length) {
      val (nextI, nextJ) = if (pat(j) == txt(i)) {
        (i + 1, j + 1)
      } else {
        if (j == 0) (i + 1, 0) else (i, lps(j - 1))
      }

      val found = nextJ == pat.length
      inner(nextI, if (found) lps(nextJ - 1) else nextJ, if (found) nextI - nextJ :: acc else acc)
    } else acc

    inner(0, 0, Nil)
  }

  def buildLps(w: String): IndexedSeq[Int] = {
    val n = w.length
    val lps = Array.ofDim[Int](n)

    lps(0) = 0

    @scala.annotation.tailrec
    def inner(i: Int, len: Int): Unit = if (i < n) {
      val (nextI, nextLen) = if (w(i) == w(len)) {
        (i + 1, len + 1)
      } else {
        if (len == 0) (i + 1, 0) else (i, lps(len - 1))
      }

      lps(i) = nextLen
      inner(nextI, nextLen)
    }

    inner(1, 0)

    lps.toIndexedSeq
  }
}