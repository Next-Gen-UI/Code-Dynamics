// https://www.hackerrank.com/challenges/intuitive-language/problem

package interpreter_and_compilers.intuitive_language

import scala.collection.mutable

case class RationalNumber(nominator: Long, denominator: Long) {
  def +(that: RationalNumber): RationalNumber = RationalNumber(this.nominator * that.denominator + this.denominator * that.nominator, this.denominator * that.denominator).normalize

  def -(that: RationalNumber): RationalNumber = RationalNumber(this.nominator * that.denominator - this.denominator * that.nominator, this.denominator * that.denominator).normalize

  def *(that: RationalNumber): RationalNumber = RationalNumber(this.nominator * that.nominator, this.denominator * that.denominator).normalize

  def /(that: RationalNumber): RationalNumber = RationalNumber(this.nominator * that.denominator, this.denominator * that.nominator).normalize

  def normalize: RationalNumber = {
    val div = gcd(math.abs(nominator), math.abs(denominator))
    val sign = if ((nominator > 0) == (denominator > 0)) 1 else -1
    RationalNumber(math.abs(nominator) / div * sign, math.abs(denominator) / div)
  }

  override def toString: String = if (denominator == 1) nominator.toString else s"$nominator/$denominator"

  @scala.annotation.tailrec
  private def gcd(a: Long, b: Long): Long = if (b == 0) a else gcd(b, a % b)
}

case class Ram(data: mutable.Map[String, RationalNumber], functions: Map[String, Syntax.FunctionDeclaration])

object Ram {
  val empty: Ram = new Ram(mutable.Map(), Map())
}

object Lex {

  trait Lexeme

  trait Operation extends Lexeme

  trait PotentialUnary extends Operation

  case class Variable(name: String) extends Lexeme

  case class Number(value: Long) extends Lexeme

  case object Function extends Lexeme

  case object Is extends Lexeme

  case object Of extends Lexeme

  case object Do extends Lexeme

  case object Assign extends Lexeme

  case object To extends Lexeme

  case object And extends Lexeme

  case object Exclamation extends Lexeme

  case object Question extends Lexeme

  case object What extends Lexeme

  case object Colon extends Lexeme

  case object Comma extends Lexeme

  case object Point extends Lexeme

  case object OpenCurlyBracket extends Lexeme

  case object CloseCurlyBracket extends Lexeme

  case object OpenRoundBracket extends Lexeme

  case object CloseRoundBracket extends Lexeme

  case object OpenSquareBracket extends Lexeme

  case object CloseSquareBracket extends Lexeme

  case object Addition extends PotentialUnary

  case object Subtraction extends PotentialUnary

  case object Multiplication extends Operation

  case object Division extends Operation

  case object Unary

}

object Syntax {

  trait Executable {
    def execute(ram: Ram): Unit
  }

  trait Operation {
    def eval(a: RationalNumber, b: RationalNumber): RationalNumber
  }

  trait Expression {
    def eval(ram: Ram): RationalNumber
  }

  case class VariableDeclaration(name: String, expression: Expression) extends Executable {
    override def execute(ram: Ram): Unit = ram.data(name) = expression.eval(ram)
  }

  case class FunctionDeclaration(name: String, parameters: List[RationalNumber]) extends Executable {
    override def execute(ram: Ram): Unit = {}
  }

  case class Pair(variable: Variable, expression: Expression)

  case class Assignment(pairs: List[Pair]) extends Executable {
    override def execute(ram: Ram): Unit = pairs.foreach(pair => ram.data(pair.variable.name) = pair.expression.eval(ram))
  }

  case class Do(expression: Expression, assignment: Assignment) extends Executable {
    override def execute(ram: Ram): Unit = {
      val count = expression.eval(ram).nominator.toInt
      (0 until count).foreach(_ => assignment.execute(ram))
    }
  }

  case class What(expressions: List[Expression]) extends Executable {
    override def execute(ram: Ram): Unit = println(expressions.map {
      case f: FunctionCall => f.partialCall(ram)
      case ex => ex.eval(ram)
    }.mkString("\n"))
  }

  case class Wrapper(expression: Expression) extends Lex.Lexeme

  case class Variable(name: String) extends Expression {
    override def eval(ram: Ram): RationalNumber = ram.data(name)
  }

  case class Number(value: Long) extends Expression {
    override def eval(ram: Ram): RationalNumber = RationalNumber(value, 1)
  }

  case class SquareBracket(parameters: List[Expression]) extends Expression {
    override def eval(ram: Ram): RationalNumber = throw new Exception("Not supported")
  }

  case class FunctionCall(name: String, parameters: List[Expression]) extends Expression {
    def partialCall(ram: Ram): String = {
      val coefficients = ram.functions(name).parameters
      val (suppliedCoefficients, restCoefficients) = coefficients.splitAt(parameters.length)
      val value = (if (parameters.isEmpty) RationalNumber(0, 1) else suppliedCoefficients.zip(parameters)
        .map { case (c, p) => c * p.eval(ram) }
        .reduce(_ + _)) + coefficients.last

      s"${restCoefficients.init.mkString(", ")}${if (parameters.length == coefficients.length - 1) "" else ", "}$value"
    }

    override def eval(ram: Ram): RationalNumber = {
      val coefficients = ram.functions(name).parameters
      coefficients.zip(parameters)
        .map { case (c, p) => c * p.eval(ram) }
        .reduce(_ + _) + coefficients.last
    }
  }

  case class Addition(a: Expression, b: Expression) extends Expression {
    override def eval(ram: Ram): RationalNumber = a.eval(ram) + b.eval(ram)
  }

  case class Subtraction(a: Expression, b: Expression) extends Expression {
    override def eval(ram: Ram): RationalNumber = a.eval(ram) - b.eval(ram)
  }

  case class Multiplication(a: Expression, b: Expression) extends Expression {
    override def eval(ram: Ram): RationalNumber = a.eval(ram) * b.eval(ram)
  }

  case class Division(a: Expression, b: Expression) extends Expression {
    override def eval(ram: Ram): RationalNumber = a.eval(ram) / b.eval(ram)
  }

  case class UnaryAddition(a: Expression) extends Expression {
    override def eval(ram: Ram): RationalNumber = a.eval(ram)
  }

  case class UnarySubtraction(a: Expression) extends Expression {
    override def eval(ram: Ram): RationalNumber = {
      val number = a.eval(ram)
      RationalNumber(-number.nominator, number.denominator)
    }
  }

}

object Expression {

  import Syntax._

  def findCloseBracket(lexemes: List[Lex.Lexeme], openBracket: Lex.Lexeme, closeBracket: Lex.Lexeme): (List[Lex.Lexeme], List[Lex.Lexeme]) = {
    case class Acc(lexemes: List[Lex.Lexeme] = Nil, bracketCount: Int = 1)
    @scala.annotation.tailrec
    def inner(lexemes: List[Lex.Lexeme], acc: Acc = Acc()): (List[Lex.Lexeme], List[Lex.Lexeme]) = {
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

  def apply(lexemes: List[Lex.Lexeme]): Expression = {
    val operationGroups = Seq(Seq(), Seq(Lex.Unary),
      Seq(Lex.Multiplication, Lex.Division), Seq(Lex.Addition, Lex.Subtraction))

    def headAsExpression(list: List[Lex.Lexeme]): Expression = list match {
      case Wrapper(expression) :: Nil => expression
      case _ => throw new Exception("Incorrect expression")
    }

    def inner(lexemes: List[Lex.Lexeme], startOfList: Boolean = false): List[Lex.Lexeme] = {
      val res = operationGroups.foldLeft(lexemes)((acc, ops) => {
        def simplify(lexemes: List[Lex.Lexeme], startOfList: Boolean = false): List[Lex.Lexeme] = lexemes match {
          case Nil => Nil

          case Lex.OpenRoundBracket :: rest =>
            val (roundBracket, nextRest) = findCloseBracket(rest, Lex.OpenRoundBracket, Lex.CloseRoundBracket)
            simplify(Wrapper(Expression(roundBracket)) :: nextRest)

          case Lex.Variable(name) :: Lex.OpenSquareBracket :: rest =>
            val (squareBracket, nextRest) = findCloseBracket(rest, Lex.OpenSquareBracket, Lex.CloseSquareBracket)
            val parameters = inner(squareBracket, startOfList = true).collect { case Wrapper(ex) => ex }
            simplify(Wrapper(FunctionCall(name, parameters)) :: nextRest)

          case Wrapper(FunctionCall(name, firstParameters)) :: Lex.OpenSquareBracket :: rest =>
            val (squareBracket, nextRest) = findCloseBracket(rest, Lex.OpenSquareBracket, Lex.CloseSquareBracket)
            val parameters = inner(squareBracket, startOfList = true).collect { case Wrapper(ex) => ex }
            simplify(Wrapper(FunctionCall(name, firstParameters ++ parameters)) :: nextRest)

          case Lex.Variable(name) :: exs => simplify(Wrapper(Syntax.Variable(name)) :: exs)

          case Lex.Number(a) :: exs => simplify(Wrapper(Number(a)) :: exs)

          case Wrapper(a) :: (op: Lex.Operation) :: Wrapper(b) :: exs if ops.contains(op) =>
            simplify(Wrapper(
              op match {
                case Lex.Addition => Addition(a, b)
                case Lex.Subtraction => Subtraction(a, b)
                case Lex.Multiplication => Multiplication(a, b)
                case Lex.Division => Division(a, b)
              }
            ) :: exs)

          case (lex: Lex.Lexeme) :: (op: Lex.PotentialUnary) :: Wrapper(a) :: exs if !lex.isInstanceOf[Wrapper] && ops.contains(Lex.Unary) =>
            simplify(lex :: Wrapper(
              op match {
                case Lex.Addition => UnaryAddition(a)
                case Lex.Subtraction => UnarySubtraction(a)
              }
            ) :: exs)

          case (op: Lex.PotentialUnary) :: Wrapper(a) :: exs if startOfList && ops.contains(Lex.Unary) =>
            simplify(Wrapper(
              op match {
                case Lex.Addition => UnaryAddition(a)
                case Lex.Subtraction => UnarySubtraction(a)
              }
            ) :: exs)

          case ex :: exs =>
            ex :: simplify(exs)
        }

        simplify(acc, startOfList)
      })

      res
    }

    headAsExpression(inner(lexemes, startOfList = true))
  }
}

object Parser {

  import Syntax._

  def toLexemes(s: String): List[Lex.Lexeme] = {
    val lexemeNames = Map(
      "is" -> Lex.Is,
      "function" -> Lex.Function,
      "of" -> Lex.Of,
      ":" -> Lex.Colon,
      "," -> Lex.Comma,
      "." -> Lex.Point,
      "do" -> Lex.Do,
      "{" -> Lex.OpenCurlyBracket,
      "}" -> Lex.CloseCurlyBracket,
      "(" -> Lex.OpenRoundBracket,
      ")" -> Lex.CloseRoundBracket,
      "[" -> Lex.OpenSquareBracket,
      "]" -> Lex.CloseSquareBracket,
      "assign" -> Lex.Assign,
      "to" -> Lex.To,
      "and" -> Lex.And,
      "!" -> Lex.Exclamation,
      "?" -> Lex.Question,
      "+" -> Lex.Addition,
      "-" -> Lex.Subtraction,
      "*" -> Lex.Multiplication,
      "/" -> Lex.Division,
      "what" -> Lex.What
    )

    def parseLexeme(index: Int, predicate: Char => Boolean): (Int, String) = {
      @scala.annotation.tailrec
      def inner(index: Int, sb: StringBuilder = new StringBuilder): (Int, String) = if (index < s.length) {
        val c = s(index)
        if (predicate(c)) inner(index + 1, sb.append(c)) else (index, sb.toString())
      } else (index, sb.toString())

      inner(index)
    }

    case class State(index: Int = 0, acc: List[Lex.Lexeme] = Nil)

    @scala.annotation.tailrec
    def inner(state: State): State = if (state.index < s.length) {
      val c = s(state.index)

      val nextState = c match {
        case d: Char if d.isLetter =>
          val (nextIndex, lexemeName) = parseLexeme(state.index, _.isLetterOrDigit)
          val lexeme = lexemeNames.getOrElse(lexemeName, Lex.Variable(lexemeName))
          State(nextIndex, lexeme :: state.acc)

        case d: Char if d.isDigit =>
          val (nextIndex, lexemeName) = parseLexeme(state.index, _.isDigit)
          val lexeme = lexemeNames.getOrElse(lexemeName, Lex.Number(lexemeName.toLong))
          State(nextIndex, lexeme :: state.acc)

        case d: Char if lexemeNames.contains(d.toString) =>
          val lexeme = lexemeNames(d.toString)
          State(index = state.index + 1, lexeme :: state.acc)

        case _ => state.copy(index = state.index + 1)
      }

      inner(nextState)
    } else state

    inner(State()).acc.reverse
  }

  implicit class Splitter(lexemes: List[Lex.Lexeme]) {
    def splitBy(separators: Set[Lex.Lexeme], drop: Set[Lex.Lexeme] = Set()): List[List[Lex.Lexeme]] = {
      case class Acc(current: List[Lex.Lexeme] = Nil, data: List[List[Lex.Lexeme]] = Nil)
      val acc = lexemes.foldLeft(Acc())((acc, lex) => {
        val nextCurrent = if (drop.contains(lex)) acc.current else lex :: acc.current
        if (separators.contains(lex)) Acc(Nil, nextCurrent.reverse :: acc.data)
        else Acc(nextCurrent, acc.data)
      })

      (if (acc.current.isEmpty) acc.data else acc.current.reverse :: acc.data).reverse
    }
  }

  def toExecutable(lexemes: List[Lex.Lexeme]): Executable = {
    lexemes match {
      case Lex.Variable(name) :: Lex.Is :: Lex.Function :: Lex.Of :: lexemes => //Function declaration
        val (paramCountLexemes, paramsLexemes) = lexemes.span(_ != Lex.Colon)
        val paramCount = Expression(paramCountLexemes).eval(Ram.empty).nominator
        val params = paramsLexemes.tail.splitBy(Set(Lex.Comma), Set(Lex.Comma, Lex.Point))

        if (paramCount == 0) {
          VariableDeclaration(name, Expression(params.head))
        } else {
          FunctionDeclaration(name, params.map(Expression(_).eval(Ram.empty)))
        }

      case Lex.Variable(name) :: Lex.Is :: lexemes => //Variable declaration
        val expression = Expression(lexemes.init)
        VariableDeclaration(name, expression)

      case Lex.Assign :: lexemes => //Assignment
        val params = lexemes.splitBy(Set(Lex.And), Set(Lex.And, Lex.Exclamation))
        val pairs = params.map(lexemes => {
          val (forExpression, forVariable) = lexemes.splitAt(lexemes.length - 2)
          Pair(Variable(forVariable.last.asInstanceOf[Lex.Variable].name), Expression(forExpression))
        })
        Assignment(pairs)

      case Lex.Do :: Lex.OpenCurlyBracket :: lexemes => //Loop
        val (expressionLexemes, rest) = lexemes.span(_ != Lex.CloseCurlyBracket)
        val assignmentLexemes = rest.tail
        Do(Expression(expressionLexemes), toExecutable(assignmentLexemes).asInstanceOf[Assignment])

      case Lex.What :: Lex.Is :: lexemes => //Query
        val params = lexemes.splitBy(Set(Lex.And), Set(Lex.And, Lex.Question))
        val expressions = params.map(Expression(_))

        What(expressions)

      case _ => throw new Exception("Wrong program")
    }
  }
}

object Solution {

  import Parser._
  import Syntax._

  def main(args: Array[String]): Unit = {
    val code = io.Source.stdin.getLines().map(_.toLowerCase).mkString("\n")
    solve(code)
  }

  def solve(s: String): Unit = {
    val lexemes = Parser.toLexemes(s)
    val groupedLexemes = lexemes.splitBy(Set(Lex.Point, Lex.Exclamation, Lex.Question))
    val executables = groupedLexemes.map(toExecutable)
    val ram = Ram(mutable.Map(), executables.collect { case f: FunctionDeclaration => f.name -> f }.toMap)
    val fixedExecutables = executables
      .map {
        case What(list) => What(list.map {
          case Variable(name) if ram.functions.contains(name) => FunctionCall(name, Nil)
          case v => v
        })
        case v => v
      }

    fixedExecutables.foreach(_.execute(ram))
  }
}