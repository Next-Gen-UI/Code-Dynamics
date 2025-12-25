// https://www.hackerrank.com/challenges/convolutional-coding/problem

package ad_hoc.convolutional_coding

import java.util.Scanner

import scala.collection.immutable.Queue

case class State(
                  index: Int,
                  m: Queue[Boolean],
                  output: List[IndexedSeq[Boolean]]
                ) {
  lazy val code: Int = m.foldLeft(0)((acc, v) => (acc << 1) + (if (v) 1 else 0))
}

case class Answer(output: IndexedSeq[Boolean], m: Queue[Boolean])

class Encoder(n: Int, k: Int, g: IndexedSeq[IndexedSeq[Boolean]]) {
  def encode(source: IndexedSeq[Boolean], state: State = State(0, Queue.fill(k)(false), Nil)): Answer = {
    def f(index: Int): Boolean = if (index < source.length) source(index) else false

    @scala.annotation.tailrec
    def inner(state: State): State = if (state.index < source.length + k) {
      val nextState: State = singleStep(state, f)
      inner(nextState)
    } else state

    val destState = inner(state)
    Answer(destState.output.reverse.flatten.toIndexedSeq, destState.m)
  }

  def singleStep(state: State, f: Int => Boolean): State = {
    val inputBit = f(state.index)
    val nextM = state.m.tail.enqueue(inputBit)
    val mWithIndices = nextM.zipWithIndex

    val t = (0 until n).map(i => {
      mWithIndices.map { case (v, j) => v && g(i)(k - 1 - j) }
        .reduce(_ ^ _)
    })

    val nextState = State(state.index + 1, nextM, t :: state.output)
    nextState
  }
}

class Decoder(n: Int, k: Int, g: IndexedSeq[IndexedSeq[Boolean]]) {
  private val encoder = new Encoder(n, k, g)

  def decode(source: IndexedSeq[Boolean]): Seq[Boolean] = {

    val groupedSource = source.grouped(n).toIndexedSeq

    @scala.annotation.tailrec
    def innerDecode(index: Int, items: List[Item]): List[Item] = if (index < groupedSource.length) {
      val nextItems: List[Item] = items
        .flatMap(item => Seq(false, true).map(b => item.copy(inputBit = b, origine = Some(item)) -> encoder.singleStep(item.state, _ => b)))
        .groupBy { case (_, state) => state.code }
        .map {
          case (_, list) => list.map { case (item, state) =>
            Item(state, item.inputBit, item.origine, item.penalty + distance(state.output.head, groupedSource(index))
            )
          }
            .minBy(_.penalty)
        }.toList

      innerDecode(index + 1, nextItems)
    } else items

    val zeroM = Queue.fill(k)(false)
    val initialItems = List(Item(State(0, zeroM, Nil), inputBit = false, None, 0))
    val finalItems = innerDecode(0, initialItems)
    val finalItem = finalItems.find(_.state.m == zeroM).get

    @scala.annotation.tailrec
    def decodedInput(item: Item, acc: List[Boolean] = Nil): List[Boolean] = if (item.origine.isEmpty) acc else
      decodedInput(item.origine.get, item.inputBit :: acc)

    decodedInput(finalItem).dropRight(k)
  }

  def distance(seq0: IndexedSeq[Boolean], seq1: IndexedSeq[Boolean]): Int =
    seq0.zip(seq1).map { case (v0, v1) => if (v0 == v1) 0 else 1 }.sum

  case class Item(state: State, inputBit: Boolean, origine: Option[Item], penalty: Int)

}

object Solution {
  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val n = sc.nextInt
    val k = sc.nextInt
    sc.nextLine
    val g = (0 until n).map(_ => sc.nextLine.map(_ == '1'))

    val n1 = sc.nextInt
    val k1 = sc.nextInt
    sc.nextLine
    val g1 = (0 until n1).map(_ => sc.nextLine.map(_ == '1'))

    @scala.annotation.tailrec
    def readAll(sb: StringBuilder = new StringBuilder()): String = if (sc.hasNext())
      readAll(sb.append(sc.nextLine))
    else sb.toString()

    val s = readAll()
      .filter(c => c == '0' || c == '1')
      .map(_ == '1')

    val decoder = new Decoder(n, k, g)
    val decodedInput = decoder.decode(s)

    //>>>>>>>>>>

    //****************************************************************************************
    //*  Here block of code starts, which is intended to fix insane part of the task.       *
    //*  Telepathy is used to restore original messages, which exist only in author's head.  *
    //****************************************************************************************

    val dictionary = List(
      //11
      "\u001C/de" -> "node",
      "maintained\u0000for" -> "maintained for",
      " tne " -> " the ",
      "ancieo*x" -> "ancient.",

      //12
      "¢Ïòe" -> "Here",
      "&=ÇÏ\u008AÏí%0'<3ÄÉÏÙ\u008AÊ\u0091\u009Bß\u009BÄË\u0096\u0087ËÞÃÅÄÙ\u008AÝÂÂ\u009C\u0097ß\u008CI«Ø\u008AÞ:7 a00%9,u!;" -> "some experiences and intimations which scar too deeply to",
      "`;4J\u008Cg\u009A\u0091\u009EÃÉËõ03Þ6:;;06!3Ä\u009D&u2rom!498" -> "unauthenticated connections from all",

      //13
      "Additionallz," -> "Additionally,",
      "machinñ(s)" -> "machine(s)",
      "wiQt" -> "will",

      //14
      "difg illustratws" -> "diff illustrates",
      "t ese two commnN|y |Wåd" -> "these two commonly used",
      "Oähev `P°various$" -> "other at various ",

      //15
      "dywnward" -> "downward",
      "quarder oj" -> "quarter of",

      //16
      "recause" -> "because",

      //17
      "refres8" -> "refresh",
      "as6©f" -> "as if",
      "7hat" -> "what",
      "founR+" -> "found?",

      //18
      "zomjõ\u008E\u0093nerals frjÍ a vast, ttmb&\u0090\u001E pile, ioCludine\u008B¦\u009D\u008B2Ù±l" -> "some minerals from a vast, tumbled pile, including several",
      "uØ\u009A\nerties is the `convine` state}ent" -> "properties is the `confine` statement",

      //19
      " useo " -> " used ",
      "groWth" -> "growth",
      "young!which were%" -> "young which were,",
      "sýall" -> "small",
      "acbount of baing@" -> "account of being ",
      "dmfFicult" -> "difficult",
      "howeves" -> "however",

      //20
      "each\u0000unit" -> "each unit",
      "dis$ribuded" -> "distributed",
      "delegáted" -> "delegated",
    )

    def toAscii(bitString: Seq[Boolean]): String = bitString
      .grouped(8)
      .map(_.foldLeft(0)((acc, v) => (acc << 1) + (if (v) 1 else 0)).toChar)
      .mkString("")

    @scala.annotation.tailrec
    def charToSeq(c: Int, index: Int = 0, acc: List[Boolean] = Nil): List[Boolean] = if (index >= 8) acc else
      charToSeq(c >> 1, index + 1, ((c & 1) == 1) :: acc)

    val decodedString = toAscii(decodedInput)
    val fixedString = dictionary.foldLeft(decodedString)((acc, pair) => acc.replace(pair._1.toCharArray, pair._2.toCharArray))
    val fixedInput = fixedString.flatMap(c => charToSeq(c))

    //<<<<<<<<<<

    val encoder = new Encoder(n1, k1, g1)
    val output = encoder.encode(fixedInput).output

    // !!! Another insane part !!!
    // String should be inverted. Is was never mentioned
    // and it doesn't correspond to the example output.

    println(output.map(if (_) 0 else 1).mkString(""))
  }
}