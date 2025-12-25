// https://www.hackerrank.com/challenges/expressions-v2/problem

package parsers.expressions_v2

import java.io.{BufferedReader, IOException, InputStreamReader}
import java.util.StringTokenizer

class FastReader() {
  val br: BufferedReader = new BufferedReader(new InputStreamReader(System.in))
  var st: StringTokenizer = _

  def nextInt: Int = next.toInt

  def nextLong: Long = next.toLong

  def nextDouble: Double = next.toDouble

  def next: String = {
    while (st == null || !st.hasMoreElements)
      try
        st = new StringTokenizer(br.readLine)
      catch {
        case e: IOException =>
          e.printStackTrace()
      }
    st.nextToken
  }

  def nextLine: String = {
    var str = ""
    try
      str = br.readLine
    catch {
      case e: IOException =>
        e.printStackTrace()
    }
    str
  }
}

trait Expression

object Expression {
  val modulo = 1000000007
}

case class Number(value: Int) extends Expression

trait Binary extends Expression {
  def eval(a: Int, b: Int): Int
}

trait Additive extends Binary

trait Multiplicative extends Binary

trait Unary extends Expression {
  def eval(v: Int): Int
}

case object UnaryAddition extends Unary {
  override def eval(v: Int): Int = v
}

case object UnarySubtraction extends Unary {
  override def eval(v: Int): Int = -v
}

case object BinaryAddition extends Additive {
  override def eval(a: Int, b: Int): Int = Math.floorMod(a.toLong + b, Expression.modulo).toInt
}

case object BinarySubtraction extends Additive {
  override def eval(a: Int, b: Int): Int = Math.floorMod(a.toLong - b, Expression.modulo).toInt
}

case object Multiplication extends Multiplicative {
  override def eval(a: Int, b: Int): Int = Math.floorMod(a.toLong * b, Expression.modulo).toInt
}

case object Division extends Multiplicative {
  override def eval(a: Int, b: Int): Int = {
    val bb = BigInt(b).modPow(Expression.modulo - 2, Expression.modulo).toLong
    Math.floorMod(a * bb, Expression.modulo).toInt
  }
}

object Solution {
  def main(args: Array[String]): Unit = {
    val sc = new FastReader

    val s = sc.nextLine
    println(solve(s))
  }

  def solve(s: String): Int = {
    case class State(index: Int = 0, expressions: List[Expression] = Nil, subexpressions: List[List[Expression]] = Nil)

    @scala.annotation.tailrec
    def parseNumber(index: Int, number: Int = 0): (Int, Int) = if (index < s.length) {
      val c = s(index)
      if (c.isDigit) parseNumber(index + 1, 10 * number + c - '0') else (index, number)
    } else (index, number)

    @scala.annotation.tailrec
    def eval(expressions: List[Expression]): Int = expressions match {
      case Number(b) :: Nil => b
      case Number(b) :: (op: Binary) :: Number(a) :: exs => eval(Number(op.eval(a, b)) :: exs)
      case _ => throw new Exception("Wrong expression")
    }

    @scala.annotation.tailrec
    def simplify(expressions: List[Expression]): List[Expression] = expressions match {
      case Number(b) :: (op: Unary) :: exs => simplify(Number(op.eval(b)) :: exs)
      case (nextOp: Additive) :: Number(b) :: (op: Multiplicative) :: Number(a) :: exs =>
        simplify(nextOp :: Number(op.eval(a, b)) :: exs)
      case exs@_ => exs
    }

    @scala.annotation.tailrec
    def inner(state: State): State = {
      def withExpression(expression: Expression): State =
        State(state.index + 1, expression :: state.expressions, state.subexpressions)

      if (state.index < s.length) {
        val c = s(state.index)
        val nextState = c match {
          case '(' => State(state.index + 1, Nil, state.expressions :: state.subexpressions)
          case ')' => State(state.index + 1, Number(eval(state.expressions)) :: state.subexpressions.head, state.subexpressions.tail)
          case '+' => withExpression(state.expressions match {
            case (_: Number) :: _ => BinaryAddition
            case _ => UnaryAddition
          })
          case '-' => withExpression(state.expressions match {
            case (_: Number) :: _ => BinarySubtraction
            case _ => UnarySubtraction
          })
          case '*' => withExpression(Multiplication)
          case '/' => withExpression(Division)

          case d: Char if d.isDigit =>
            val (nextIndex, number) = parseNumber(state.index)
            State(nextIndex, Number(number) :: state.expressions, state.subexpressions)
          case _ => state.copy(index = state.index + 1)
        }

        val nextExpressions = simplify(nextState.expressions)

        inner(nextState.copy(expressions = nextExpressions))
      } else state
    }

    eval(inner(State()).expressions)
  }
}