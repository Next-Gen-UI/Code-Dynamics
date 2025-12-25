// https://www.hackerrank.com/challenges/password-cracker-fp/problem

package memoization_and_dp.password_cracker_fp

import java.util.Scanner

import scala.collection.mutable

object Solution {
  private val data = mutable.Map[String, List[String]]()

  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val t = sc.nextInt
    (0 until t).foreach(_ => {
      sc.nextInt
      sc.nextLine
      val passwords = sc.nextLine.split(" ")
      val loginAttempt = sc.nextLine

      println(solve(loginAttempt, passwords.toIndexedSeq))
      data.clear()
    })

    sc.close()
  }

  def solve(loginAttempt: String, passwords: Seq[String]): String = {

    def inner(loginAttempt: String, acc: List[String]): List[String] = data.getOrElseUpdate(loginAttempt,
      if (loginAttempt.isEmpty) acc
      else {
        passwords.foldLeft(List[String]())((list, password) => if (list.isEmpty) {
          val isPrefix = loginAttempt.startsWith(password)

          val rest = if (isPrefix) inner(loginAttempt.substring(password.length), password :: acc) else Nil

          if (isPrefix && rest.nonEmpty) rest else Nil
        } else list)
      })

    val answer = inner(loginAttempt, Nil).reverse
    if (answer.isEmpty) "WRONG PASSWORD" else answer.mkString(" ")
  }
}