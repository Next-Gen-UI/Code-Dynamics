// https://www.hackerrank.com/challenges/while-language-fp/problem

package interpreter_and_compilers.while_language_fp

import scala.collection.mutable

case class Ram(data: mutable.Map[String, Long])

trait Expression

trait Operand extends Expression

case class Variable(name: String) extends ArithmeticEvaluation {
  override def eval(ram: Ram): Long = ram.data(name)
}

case class Number(value: Long) extends ArithmeticEvaluation {
  override def eval(ram: Ram): Long = value
}

case class Parentheses(expressions: List[Expression]) extends ArithmeticEvaluation {
  private lazy val evaluation = toEvaluation.asInstanceOf[ArithmeticEvaluation]

  def toEvaluation: Evaluation = {
    val res = Parentheses.operationGroups.foldLeft(expressions)((acc, ops) => {
      def simplify(expressions: List[Expression]): List[Expression] = expressions match {
        case Nil => Nil
        case (a: BooleanValue) :: Nil => simplify(ValueBoolean(a) :: Nil)
        case (a: BooleanEvaluation) :: (op: BooleanOperation) :: (b: BooleanEvaluation) :: exs if ops.contains(op) =>
          simplify(BinaryBoolean(op, a, b) :: exs)
        case (a: Number) :: Nil => simplify(ValueArithmetic(a) :: Nil)
        case (a: Variable) :: Nil => simplify(ValueArithmetic(a) :: Nil)
        case (a: ArithmeticEvaluation) :: (op: ArithmeticOperation) :: (b: ArithmeticEvaluation) :: exs if ops.contains(op) =>
          simplify(BinaryArighmetic(op, a, b) :: exs)
        case (a: ArithmeticEvaluation) :: (op: RelationalOperation) :: (b: ArithmeticEvaluation) :: exs if ops.contains(op) =>
          simplify(BinaryRelational(op, a, b) :: exs)
        case ex :: exs => ex :: simplify(exs)
      }

      simplify(acc)
    })

    res.head.asInstanceOf[Evaluation]
  }

  override def eval(ram: Ram): Long = evaluation.eval(ram)
}

case class CurlyBrackets(expressions: List[Expression]) extends Expression

object Parentheses {
  val operationGroups = Seq(
    Seq(Multiplication, Division),
    Seq(Addition, Subtraction),
    Seq(Greater, Less),
    Seq(And),
    Seq(Or),
    Seq(Assignment)
  )
}

trait Operation extends Expression

trait ArithmeticOperation extends Operation {
  def eval(a: Long, b: Long): Long
}

trait Additive extends ArithmeticOperation

trait Multiplicative extends ArithmeticOperation

case object Addition extends Additive {
  override def eval(a: Long, b: Long): Long = a + b
}

case object Subtraction extends Additive {
  override def eval(a: Long, b: Long): Long = a - b
}

case object Multiplication extends Multiplicative {
  override def eval(a: Long, b: Long): Long = a * b
}

case object Division extends Multiplicative {
  override def eval(a: Long, b: Long): Long = a / b
}


trait RelationalOperation extends Operation {
  def eval(a: Long, b: Long): Boolean
}

case object Less extends RelationalOperation {
  override def eval(a: Long, b: Long): Boolean = a < b
}

case object Greater extends RelationalOperation {
  override def eval(a: Long, b: Long): Boolean = a > b
}


trait BooleanOperation extends Operation {
  def eval(a: Boolean, b: Boolean): Boolean
}

case object And extends BooleanOperation {
  override def eval(a: Boolean, b: Boolean): Boolean = a && b
}

case object Or extends BooleanOperation {
  override def eval(a: Boolean, b: Boolean): Boolean = a || b
}


trait Keyword extends Expression

case object If extends Keyword

case object Then extends Keyword

case object Else extends Keyword

case object While extends Keyword

case object Do extends Keyword

case object Assignment extends Operation


trait BooleanValue extends Operand {
  def value: Boolean
}

case object False extends BooleanValue {
  override def value: Boolean = false
}

case object True extends BooleanValue {
  override def value: Boolean = true
}


case class Statement(expressions: List[Expression]) extends Expression

case class Lexeme(s: String) extends Expression

trait Evaluation extends Operand


trait BooleanEvaluation extends Evaluation {
  def eval(ram: Ram): Boolean
}

case class ValueBoolean(a: BooleanValue) extends BooleanEvaluation {
  override def eval(ram: Ram): Boolean = a.value
}

case class BinaryBoolean(op: BooleanOperation, a: BooleanEvaluation, b: BooleanEvaluation) extends BooleanEvaluation {
  override def eval(ram: Ram): Boolean = op.eval(a.eval(ram), b.eval(ram))
}

case class BinaryRelational(op: RelationalOperation, a: ArithmeticEvaluation, b: ArithmeticEvaluation) extends BooleanEvaluation {
  override def eval(ram: Ram): Boolean = op.eval(a.eval(ram), b.eval(ram))
}


trait ArithmeticEvaluation extends Evaluation {
  def eval(ram: Ram): Long
}

case class ValueArithmetic(a: ArithmeticEvaluation) extends ArithmeticEvaluation {
  override def eval(ram: Ram): Long = a.eval(ram)
}

case class BinaryArighmetic(op: ArithmeticOperation, a: ArithmeticEvaluation, b: ArithmeticEvaluation) extends ArithmeticEvaluation {
  override def eval(ram: Ram): Long = op.eval(a.eval(ram), b.eval(ram))
}


trait Operator extends Expression {
  def execute(ram: Ram): Unit
}

case class WhileOperator(condition: BooleanEvaluation, body: List[Operator]) extends Operator {
  override def execute(ram: Ram): Unit =
    while (condition.eval(ram))
      body.foreach(_.execute(ram))
}

case class IfOperator(condition: BooleanEvaluation, op1: List[Operator], op2: List[Operator]) extends Operator {
  override def execute(ram: Ram): Unit =
    if (condition.eval(ram)) op1.foreach(_.execute(ram))
    else op2.foreach(_.execute(ram))
}

case class AssignmentOperator(variable: Variable, evaluation: ArithmeticEvaluation) extends Operator {
  override def execute(ram: Ram): Unit = ram.data(variable.name) = evaluation.eval(ram)
}


object Solution {

  def main(args: Array[String]): Unit = {
    val code = io.Source.stdin.getLines().mkString("\n")
    println(solve(code.split("""\s+""").iterator))
  }

  def solve(s: Iterator[String]): String = {
    case class State(expressions: List[Expression] = Nil, subexpressions: List[List[Expression]] = Nil)

    @scala.annotation.tailrec
    def extractStatement(expressions: List[Expression], acc: List[Expression] = Nil): List[Expression] = expressions match {
      case Nil => Statement(acc) :: Nil
      case (st: Statement) :: exs => Statement(acc) :: st :: exs
      case ex :: exs => extractStatement(exs, ex :: acc)
    }

    val lexemes = Map(
      "+" -> Addition,
      "-" -> Subtraction,
      "*" -> Multiplication,
      "/" -> Division,
      "and" -> And,
      "or" -> Or,
      ">" -> Greater,
      "<" -> Less,
      ":=" -> Assignment,
      "true" -> True,
      "false" -> False,
      "if" -> If,
      "then" -> Then,
      "else" -> Else,
      "while" -> While,
      "do" -> Do,
      "(" -> Lexeme("("),
      ")" -> Lexeme(")"),
      "{" -> Lexeme("{"),
      "}" -> Lexeme("}"),
      ";" -> Lexeme(";"),
    )

    @scala.annotation.tailrec
    def inner(state: State): State = {
      def withExpression(expression: Expression): State =
        State(expression :: state.expressions, state.subexpressions)

      if (s.hasNext) {
        val c = s.next()
        val nextState = c match {
          case lexeme if lexemes.contains(lexeme) =>
            lexemes(lexeme) match {
              case Lexeme("(") | Lexeme("{") => State(Nil, state.expressions :: state.subexpressions)
              case Lexeme(")") => State(Parentheses(state.expressions.reverse) :: state.subexpressions.head, state.subexpressions.tail)
              case Lexeme("}") => State(CurlyBrackets(extractStatement(state.expressions)) :: state.subexpressions.head, state.subexpressions.tail)
              case Lexeme(";") => State(extractStatement(state.expressions), state.subexpressions)
              case lex => withExpression(lex)
            }

          case variable if variable.head.isLetter =>
            State(Variable(variable) :: state.expressions, state.subexpressions)

          case number if number.head.isDigit =>
            State(Number(number.toLong) :: state.expressions, state.subexpressions)

          case _ => state
        }

        inner(nextState)
      } else
        state
    }

    def rectifyStatements(statements: List[Statement], acc: List[Operator] = Nil): List[Operator] = statements match {
      case Nil => acc
      case st :: sts =>
        val nextStatement = st.expressions match {
          case While :: (condition: Parentheses) :: Do :: CurlyBrackets(body) :: Nil =>
            WhileOperator(condition.toEvaluation.asInstanceOf[BooleanEvaluation], rectifyStatements(body.map(_.asInstanceOf[Statement])))
          case If :: (condition: Parentheses) :: Then :: CurlyBrackets(op1) :: Else :: CurlyBrackets(op2) :: Nil =>
            IfOperator(condition.toEvaluation.asInstanceOf[BooleanEvaluation],
              rectifyStatements(op1.map(_.asInstanceOf[Statement])),
              rectifyStatements(op2.map(_.asInstanceOf[Statement]))
            )
          case (variable: Variable) :: Assignment :: exs => AssignmentOperator(variable, Parentheses(exs).toEvaluation.asInstanceOf[ArithmeticEvaluation])
          case _ => throw new Exception("Unsupported statement")
        }
        rectifyStatements(sts, nextStatement :: acc)
    }

    val expressions = extractStatement(inner(State()).expressions)
    val operators = rectifyStatements(expressions.map(_.asInstanceOf[Statement]))

    val ram = Ram(mutable.Map())
    operators.foreach(_.execute(ram))

    ram.data.toSeq.sortBy(_._1).map { case (name, value) => s"$name $value" }.mkString("\n")
  }
}