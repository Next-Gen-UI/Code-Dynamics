// https://www.hackerrank.com/challenges/down-with-abstractions/problem

package interpreter_and_compilers.down_with_abstractions

import java.util.Scanner

trait Lexeme

trait PrimaryLexeme extends Lexeme

case object Backslash extends PrimaryLexeme

case object Point extends PrimaryLexeme

case object OpenRoundBracket extends PrimaryLexeme

case object CloseRoundBracket extends PrimaryLexeme

trait Body {
  def freeVariables: Set[String] = Set()
}

case class Variable(name: String) extends PrimaryLexeme with Body {
  override lazy val freeVariables: Set[String] = Set(name)
}

trait SecondaryLexeme extends Lexeme

case class Lambda(name: String, body: Body) extends SecondaryLexeme with Body {
  override lazy val freeVariables: Set[String] = body.freeVariables - name
}

case class Application(e1: Body, e2: Body) extends SecondaryLexeme with Body {
  override lazy val freeVariables: Set[String] = e1.freeVariables ++ e2.freeVariables

  override def toString: String = e2 match {
    case _: Application => s"$e1($e2)"
    case _ => s"$e1$e2"
  }
}

case object Empty extends Body

case object K extends Body

case object I extends Body

case object S extends Body

case object C extends Body

case object B extends Body

case class T(body: Body) extends SecondaryLexeme with Body {

  def transform: Body = {
    val res = body match {
      //eta-reduction (if x is not free in E)
      case Lambda(x, Application(e, Variable(y))) if x == y && isNotFree(x, e) =>
        T(e).transform

      //1
      case v: Variable =>
        v

      //2
      case Application(e1, e2) =>
        Application(T(e1).transform, T(e2).transform)

      //3. (if x is not free in E)
      case Lambda(x, e) if isNotFree(x, e) =>
        Application(K, T(e).transform)

      //4
      case Lambda(x, Variable(y)) if x == y =>
        I

      //5. (if x is free in E)
      case Lambda(x, Lambda(y, e)) if isFree(x, e) =>
        T(Lambda(x, T(Lambda(y, e)).transform)).transform

      //6. (if x is free in both E₁ and E₂)
      case Lambda(x, Application(e1, e2)) if isFree(x, e1) && isFree(x, e2) =>
        Application(Application(S, T(Lambda(x, e1)).transform), T(Lambda(x, e2)).transform)

      //7.  (if x is free in E₁ but not E₂)
      case Lambda(x, Application(e1, e2)) if isFree(x, e1) && isNotFree(x, e2) =>
        Application(Application(C, T(Lambda(x, e1)).transform), T(e2).transform)

      //8. (if x is free in E₂ but not E₁)
      case Lambda(x, Application(e1, e2)) if isNotFree(x, e1) && isFree(x, e2) =>
        Application(Application(B, T(e1).transform), T(Lambda(x, e2)).transform)

      case v =>
        v
    }

    res
  }

  private def isFree(x: String, e: Body): Boolean = e.freeVariables.contains(x)

  private def isNotFree(x: String, e: Body): Boolean = !isFree(x, e)
}

object Expression {

  def findCloseBracket(lexemes: List[Lexeme], openBracket: Lexeme, closeBracket: Lexeme): (List[Lexeme], List[Lexeme]) = {
    case class Acc(lexemes: List[Lexeme] = Nil, bracketCount: Int = 1)
    @scala.annotation.tailrec
    def inner(lexemes: List[Lexeme], acc: Acc = Acc()): (List[Lexeme], List[Lexeme]) = {
      lexemes match {
        case (lex@`closeBracket`) :: lexemes =>
          if (acc.bracketCount == 1) (acc.lexemes.reverse, lexemes)
          else inner(lexemes, Acc(lex :: acc.lexemes, acc.bracketCount - 1))
        case (lex@`openBracket`) :: lexemes => inner(lexemes, Acc(lex :: acc.lexemes, acc.bracketCount + 1))
        case lex :: lexemes => inner(lexemes, Acc(lex :: acc.lexemes, acc.bracketCount))
        case Nil => throw new Exception("Wrong exception")
      }
    }

    inner(lexemes)
  }
}

object Solution {
  def curry(name: String, otherNames: List[String], body: Body): Lambda = otherNames match {
    case Nil => Lambda(name, body)
    case n :: ns => Lambda(name, curry(n, ns, body))
  }

  def toLambda(lexemes: List[Lexeme]): Body = lexemes match {
    case Nil => Empty

    case (v: Variable) :: Nil => v

    case (v: Variable) :: rest => Application(v, toLambda(rest))

    case OpenRoundBracket :: Backslash :: rest =>
      val (bound, tempRest) = rest.span(_ != Point)
      val (part, nextRest) = findCloseBracket(tempRest.tail)
      val names = bound.collect { case Variable(name) => name }
      val lambda = curry(names.head, names.tail, toLambda(part))

      if (nextRest.isEmpty) lambda
      else Application(lambda, toLambda(nextRest))

    case OpenRoundBracket :: (v: Variable) :: rest =>
      val (part, nextRest) = findCloseBracket(rest)

      if (part.isEmpty) v
      else {
        val app = Application(v, toLambda(part))
        if (nextRest.isEmpty) app
        else Application(app, toLambda(nextRest))
      }

    case OpenRoundBracket :: OpenRoundBracket :: rest =>
      val (part, nextRest) = findCloseBracket(OpenRoundBracket :: rest)

      if (nextRest.isEmpty) toLambda(part)
      else Application(toLambda(part), toLambda(nextRest))

    case _ => throw new Exception("Wrong expression")
  }

  def solve(s: String): Body = {
    def toLexemes: List[Lexeme] = {
      val lexemeNames = Map(
        "\\" -> Backslash,
        "." -> Point,
        "(" -> OpenRoundBracket,
        ")" -> CloseRoundBracket
      )

      def parseLexeme(index: Int, predicate: Char => Boolean): (Int, String) = {
        @scala.annotation.tailrec
        def inner(index: Int, sb: StringBuilder = new StringBuilder): (Int, String) = if (index < s.length) {
          val c = s(index)
          if (predicate(c)) inner(index + 1, sb.append(c)) else (index, sb.toString())
        } else (index, sb.toString())

        inner(index)
      }

      case class State(index: Int = 0, acc: List[Lexeme] = Nil)

      @scala.annotation.tailrec
      def inner(state: State): State = if (state.index < s.length) {
        val c = s(state.index)

        val nextState = c match {
          case d: Char if d.isLetter =>
            val (nextIndex, lexemeName) = parseLexeme(state.index, c => c.isLetterOrDigit || c == '_')
            val lexeme = lexemeNames.getOrElse(lexemeName, Variable(lexemeName))
            State(nextIndex, lexeme :: state.acc)

          case d: Char if lexemeNames.contains(d.toString) =>
            val lexeme = lexemeNames(d.toString)
            State(index = state.index + 1, lexeme :: state.acc)

          case _ => state.copy(index = state.index + 1)
        }

        inner(nextState)
      } else state

      inner(State()).acc
    }

    val lexemes = toLexemes.reverse
    val lambda = toLambda(lexemes)
    val res = T(lambda).transform
    res
  }

  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)
    val t = sc.nextInt
    sc.nextLine

    (0 until t).foreach(_ => {
      val s = sc.nextLine
      println(solve(s))
    })
  }

  private def findCloseBracket(lexemes: List[Lexeme]): (List[Lexeme], List[Lexeme]) = {
    case class Acc(lexemes: List[Lexeme] = Nil, bracketCount: Int = 1)
    @scala.annotation.tailrec
    def inner(lexemes: List[Lexeme], acc: Acc = Acc()): (List[Lexeme], List[Lexeme]) = {
      lexemes match {
        case (lex@CloseRoundBracket) :: lexemes =>
          if (acc.bracketCount == 1) (acc.lexemes.reverse, lexemes)
          else inner(lexemes, Acc(lex :: acc.lexemes, acc.bracketCount - 1))
        case (lex@OpenRoundBracket) :: lexemes => inner(lexemes, Acc(lex :: acc.lexemes, acc.bracketCount + 1))
        case lex :: lexemes => inner(lexemes, Acc(lex :: acc.lexemes, acc.bracketCount))
        case Nil => throw new Exception("Wrong exception")
      }
    }

    inner(lexemes)
  }
}