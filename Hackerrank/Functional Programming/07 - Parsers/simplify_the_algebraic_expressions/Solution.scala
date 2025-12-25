// https://www.hackerrank.com/challenges/simplify-the-algebraic-expressions/problem

package parsers.simplify_the_algebraic_expressions

import java.util.Scanner

trait Expression

trait Operand extends Expression {
  def toPolynomial: Polynomial
}

case class Number(value: Int) extends Operand {
  override def toPolynomial: Polynomial = Polynomial(Map(0 -> value))
}

case object X extends Operand {
  override def toPolynomial: Polynomial = Polynomial(Map(1 -> 1))
}

case class Parentheses(expressions: List[Expression]) extends Operand {
  override def toPolynomial: Polynomial = {
    val res = Parentheses.operationGroups.foldLeft(expressions.reverse)((acc, ops) => {
      def simplify(expressions: List[Expression]): List[Expression] = expressions match {
        case Nil => Nil
        case (op: Unary) :: (a: Operand) :: exs if ops.contains(op) =>
          simplify(op.eval(a.toPolynomial) :: exs)
        case (a: Operand) :: (op: Operation) :: (b: Operand) :: exs if ops.contains(op) =>
          simplify(op.eval(a.toPolynomial, b.toPolynomial) :: exs)
        case ex :: exs => ex :: simplify(exs)
      }

      simplify(acc)
    })

    res match {
      case (p: Operand) :: Nil => p.toPolynomial
      case _ => throw new Exception("Wrong expression")
    }
  }
}

object Parentheses {
  val operationGroups = Seq(Seq(Exponentiation), Seq(UnaryAddition, UnarySubtraction), Seq(Multiplication, Division), Seq(Addition, Subtraction))
}

case class Polynomial(data: Map[Int, Int]) extends Operand {
  override def toPolynomial: Polynomial = this

  override def toString: String = if (data.isEmpty) "0" else {
    val sortedData = data.toSeq.sortBy(-_._1)
    val highestPower = sortedData.head._1
    sortedData
      .map { case (p, v) =>
        val isNegative = v < 0
        val sign = if (p == highestPower) {
          if (isNegative) "-" else ""
        }
        else {
          if (isNegative) " - " else " + "
        }

        val x = if (p < 2) {
          if (p == 1) "x" else ""
        } else "x^"

        val absValue = math.abs(v)
        val strValue = if (absValue == 1 && p != 0) "" else absValue.toString
        val power = if (p < 2) "" else p.toString

        s"$sign$strValue$x$power"
      }
      .mkString("")
  }
}

trait Unary extends Expression {
  def eval(a: Polynomial): Polynomial
}

case object UnarySubtraction extends Unary {
  override def eval(a: Polynomial): Polynomial = Polynomial(a.data.map { case (k, v) => k -> -v })
}

case object UnaryAddition extends Unary {
  override def eval(a: Polynomial): Polynomial = a
}

trait Operation extends Expression {
  def eval(a: Polynomial, b: Polynomial): Polynomial
}

trait Additive extends Operation {
  def eval(a: Polynomial, b: Polynomial, f: (Int, Int) => Int): Polynomial = {
    val allKeys = a.data.keys.toSet ++ b.data.keys.toSet

    Polynomial(allKeys.map(k => {
      val av = a.data.getOrElse(k, 0)
      val bv = b.data.getOrElse(k, 0)
      k -> f(av, bv)
    })
      .filter { case (_, v) => v != 0 }
      .toMap)
  }
}

trait Multiplicative extends Operation

case object Addition extends Additive {
  override def eval(a: Polynomial, b: Polynomial): Polynomial = eval(a, b, _ + _)
}

case object Subtraction extends Additive {
  override def eval(a: Polynomial, b: Polynomial): Polynomial = eval(a, b, _ - _)
}

case object Multiplication extends Multiplicative {
  override def eval(a: Polynomial, b: Polynomial): Polynomial = {
    Polynomial(a.data.keys.flatMap(ak => b.data.keys.map(bk => {
      (ak + bk) -> a.data(ak) * b.data(bk)
    }))
      .groupBy { case (k, _) => k }
      .map { case (k, list) => k -> list.map(_._2).sum }
      .filter { case (_, v) => v != 0 })
  }
}

case object Division extends Multiplicative {
  override def eval(a: Polynomial, b: Polynomial): Polynomial = {
    val bv = b.data.getOrElse(0, 0)

    Polynomial(a.data.keys.map(k => {
      val av = a.data.getOrElse(k, 0)
      k -> av / bv
    }).toMap)
  }
}

case object Exponentiation extends Operation {
  override def eval(a: Polynomial, b: Polynomial): Polynomial = {
    if (a.data.isEmpty) {
      Polynomial(Map(0 -> 1))
    } else {
      val bp = b.data.getOrElse(0, 0)
      val (ap, av) = a.data.head
      if (ap == 0) Polynomial(Map(0 -> BigInt(av).pow(bp).toInt))
      Polynomial(Map(bp -> 1))
    }
  }
}

object Solution {
  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)

    val t = sc.nextInt
    sc.nextLine

    (0 until t).foreach(_ => {
      val s = sc.nextLine
      println(solve(s))
    })

    sc.close()
  }

  def solve(s: String): String = {
    case class State(index: Int = 0, expressions: List[Expression] = Nil, subexpressions: List[List[Expression]] = Nil)

    @scala.annotation.tailrec
    def parseNumber(index: Int, number: Int = 0): (Int, Int) = if (index < s.length) {
      val c = s(index)
      if (c.isDigit) parseNumber(index + 1, 10 * number + c - '0') else (index, number)
    } else (index, number)

    @scala.annotation.tailrec
    def inner(state: State): State = {
      def withExpression(expression: Expression): State =
        State(state.index + 1, expression :: state.expressions, state.subexpressions)

      if (state.index < s.length) {
        val c = s(state.index)
        val nextState = c match {
          case '(' => State(state.index + 1, Nil, state.expressions :: state.subexpressions)
          case ')' =>
            State(state.index + 1, Parentheses(state.expressions) :: state.subexpressions.head, state.subexpressions.tail)
          case '+' => withExpression(state.expressions match {
            case (_: Number) :: _ | (_: Parentheses) :: _ | X :: _ => Addition
            case _ => UnaryAddition
          })
          case '-' => withExpression(state.expressions match {
            case (_: Number) :: _ | (_: Parentheses) :: _ | X :: _ => Subtraction
            case _ => UnarySubtraction
          })
          case '*' => withExpression(Multiplication)
          case '/' => withExpression(Division)
          case '^' => withExpression(Exponentiation)
          case 'x' => withExpression(X)

          case d: Char if d.isDigit =>
            val (nextIndex, number) = parseNumber(state.index)
            State(nextIndex, Number(number) :: state.expressions, state.subexpressions)
          case _ => state.copy(index = state.index + 1)
        }

        val nextExpressions = nextState.expressions match {
          case (a: Operand) :: (b: Operand) :: exs => a :: Multiplication :: b :: exs
          case exs => exs
        }

        inner(nextState.copy(expressions = nextExpressions))
      } else
        state
    }

    Parentheses(inner(State()).expressions).toPolynomial.toString
  }
}