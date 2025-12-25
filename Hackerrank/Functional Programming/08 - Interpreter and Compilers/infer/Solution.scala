// https://www.hackerrank.com/challenges/infer/problem

package interpreter_and_compilers.infer

import java.util.Scanner

import scala.collection.mutable

trait Expression

object Expression {

  case class FunctionDefinition(params: List[String], body: Expression) extends Expression

  case class Variable(name: String) extends Expression

  case class FunctionCall(function: Expression, params: List[Expression]) extends Expression

  case class VariableDefinition(name: String, definition: Expression, body: Expression) extends Expression

}

object Parser {

  def toLexemes(s: String): List[Lexeme] = {
    val lexemeNames = Map(
      "let" -> Let,
      "in" -> In,
      "fun" -> Fun,
      "forall" -> Forall,
      "=" -> Equal,
      "->" -> Arrow,
      "," -> Comma,
      ":" -> Colon,
      "(" -> OpenRoundBracket,
      ")" -> CloseRoundBracket,
      "[" -> OpenSquareBracket,
      "]" -> CloseSquareBracket
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
        case d: Char if d.isLetter || d == '_' =>
          val (nextIndex, lexemeName) = parseLexeme(state.index, c => c.isLetterOrDigit || c == '_')
          val lexeme = lexemeNames.getOrElse(lexemeName, Identifier(lexemeName))
          State(nextIndex, lexeme :: state.acc)

        case '-' if state.index + 1 < s.length && s(state.index + 1) == '>' =>
          State(state.index + 2, Arrow :: state.acc)

        case d: Char if lexemeNames.contains(d.toString) =>
          val lexeme = lexemeNames(d.toString)
          State(index = state.index + 1, lexeme :: state.acc)

        case _ => state.copy(index = state.index + 1)
      }

      inner(nextState)
    } else state

    inner(State()).acc.reverse
  }

  def toExpression(lexemes: List[Lexeme]): Expression = lexemes match {
    case Let :: Identifier(name) :: Equal :: rest =>
      val (forExpr1, forExpr2) = findCloseBracket(rest, Let, In)
      Expression.VariableDefinition(name, toExpression(forExpr1), toExpression(forExpr2))

    case Fun :: rest =>
      val (forArgs, forExpr) = rest.span(_ != Arrow)
      val args = forArgs.map { case Identifier(name) => name }
      Expression.FunctionDefinition(args, toExpression(forExpr.tail))

    case OpenRoundBracket :: rest =>
      val (forExpr, nextRest) = findCloseBracket(rest, OpenRoundBracket, CloseRoundBracket)
      val expr = toExpression(forExpr)
      if (nextRest.isEmpty) {
        expr
      } else {
        val params = nextRest.tail.init.splitBy(Set(Comma), Set(Comma)).map(toExpression)
        Expression.FunctionCall(expr, params)
      }

    case Identifier(name) :: nextRest =>
      val expr = Expression.Variable(name)
      if (nextRest.isEmpty) {
        expr
      } else {
        @scala.annotation.tailrec
        def toFunctionCall(expr: Expression, nextRest: List[Lexeme]): Expression.FunctionCall = {
          val (forParams, rest2) = findCloseBracket(nextRest.tail, OpenRoundBracket, CloseRoundBracket)
          val params = forParams.splitBy(Set(Comma), Set(Comma)).map(toExpression)

          if (rest2.isEmpty) Expression.FunctionCall(expr, params) else toFunctionCall(Expression.FunctionCall(expr, params), rest2)
        }

        toFunctionCall(expr, nextRest)
      }

    case _ => throw new Exception("Wrong expression")
  }

  def loadEnvironment(s: String): Map[String, Inferer.Type] = {
    def parseLine(s: String): (String, Inferer.Type) = {
      val lexemes = toLexemes(s)
      lexemes match {
        case Identifier(name) :: Colon :: Forall :: rest =>
          val (forParams, nextRest) = rest.tail.span(_ != CloseSquareBracket)
          val params = forParams.collect { case Identifier(a) => a }
          val generics = params.map(name => name -> Inferer.nextVariable).toMap
          name -> parseType(generics, nextRest.tail)

        case Identifier(name) :: Colon :: rest =>
          name -> parseType(Map(), rest)

        case _ =>
          throw new Exception("Wrong type")
      }
    }

    s.split("\n").map(parseLine).toMap
  }

  private def findCloseBracket(lexemes: List[Lexeme], openBracket: Lexeme, closeBracket: Lexeme): (List[Lexeme], List[Lexeme]) = {
    case class Acc(lexemes: List[Lexeme] = Nil, bracketCount: Int = 1)
    @scala.annotation.tailrec
    def inner(lexemes: List[Lexeme], acc: Acc = Acc()): (List[Lexeme], List[Lexeme]) = {
      lexemes match {
        case (lex@`closeBracket`) :: lexemes =>
          if (acc.bracketCount == 1) (acc.lexemes.reverse, lexemes)
          else inner(lexemes, Acc(lex :: acc.lexemes, acc.bracketCount - 1))
        case (lex@`openBracket`) :: lexemes => inner(lexemes, Acc(lex :: acc.lexemes, acc.bracketCount + 1))
        case lex :: lexemes => inner(lexemes, Acc(lex :: acc.lexemes, acc.bracketCount))
        case Nil => throw new Exception("Wrong expression")
      }
    }

    inner(lexemes)
  }

  private def parseType(generics: Map[String, Inferer.Variable], lexemes: List[Lexeme]): Inferer.Type = {
    val res = lexemes match {
      case OpenRoundBracket :: rest =>
        val (forSubTypes, nextRest) = findCloseBracket(rest, OpenRoundBracket, CloseRoundBracket)
        val tys = forSubTypes.splitBy(Set(Comma), Set(Comma)).map(lexemes => parseType(generics, lexemes))
        Inferer.Function(tys, parseType(generics, nextRest.tail))

      case lexemes =>
        val (first, rest) = lexemes.span(_ != Arrow)
        val (id, params) = first.span(_ != OpenSquareBracket)

        val name = id.head.asInstanceOf[Identifier].name
        val firstType = if (params.isEmpty) { //Identifier
          generics.getOrElse(name, Inferer.Operation(name, Seq()))
        } else {
          Inferer.Operation(name, params.collect { case Identifier(a) => generics(a) })
        }

        if (rest.isEmpty)
          firstType
        else
          Inferer.Function(List(firstType), parseType(generics, rest.tail))
    }
    res
  }

  trait Lexeme

  case class Identifier(name: String) extends Lexeme

  case object Let extends Lexeme

  case object In extends Lexeme

  case object Fun extends Lexeme

  case object Forall extends Lexeme

  case object Equal extends Lexeme

  case object Arrow extends Lexeme

  case object Comma extends Lexeme

  implicit class Splitter(lexemes: List[Lexeme]) {
    def splitBy(separators: Set[Lexeme], drop: Set[Lexeme] = Set()): List[List[Lexeme]] = {
      case class Acc(current: List[Lexeme] = Nil, data: List[List[Lexeme]] = Nil)
      val acc = lexemes.foldLeft(Acc())((acc, lex) => {
        val nextCurrent = if (drop.contains(lex)) acc.current else lex :: acc.current
        if (separators.contains(lex)) Acc(Nil, nextCurrent.reverse :: acc.data)
        else Acc(nextCurrent, acc.data)
      })

      (if (acc.current.isEmpty) acc.data else acc.current.reverse :: acc.data).reverse
    }
  }

  case object Colon extends Lexeme

  case object OpenRoundBracket extends Lexeme

  case object CloseRoundBracket extends Lexeme

  case object OpenSquareBracket extends Lexeme

  case object CloseSquareBracket extends Lexeme

}

object Inferer {

  type Environment = Map[String, Type]
  private val arrow = "->"
  private var nextName = 'a'
  private var nextId = 0

  def Function(from: List[Type], to: Type): Operation = Operation(arrow, from.:+(to))

  def nextUniqueName: String = {
    val res = nextName
    nextName = (nextName + 1).toChar
    res.toString
  }

  def nextVariable: Variable = {
    val result = nextId
    nextId += 1
    Variable(result)
  }

  def infer(expression: Expression, environment: Environment, variables: mutable.Set[Variable] = mutable.Set()): Type = expression match {
    case Expression.VariableDefinition(name, definition, body) =>
      val definitionType = infer(definition, environment, variables)
      val nextEnvironment = environment + (name -> definitionType)
      infer(body, nextEnvironment, variables)

    case Expression.FunctionDefinition(params, body) =>
      val paramTypes = params.map(_ => nextVariable)
      val nextEnvironment = params.zip(paramTypes)
        .foldLeft(environment) { case (acc, (v, t)) => acc + (v -> t) }
      val nextVariables = paramTypes.foldLeft(variables)((acc, t) => acc.union(mutable.Set(t)))
      val resultType = infer(body, nextEnvironment, nextVariables)
      Function(paramTypes, resultType)

    case Expression.FunctionCall(function, params) =>
      val functionType = infer(function, environment, variables)
      val paramTypes = params.map(infer(_, environment, variables))
      val resultType = nextVariable
      unify(Function(paramTypes, resultType), functionType)
      resultType

    case Expression.Variable(name) =>
      inferVariableType(environment(name), variables)
  }

  def unify(type0: Type, type1: Type): Unit = {
    (prune(type0), prune(type1)) match {
      case (a: Variable, b) => if (a != b) {
        if (contains(b, a)) throw new Exception("recursion detected")
        a.typeOpt = Some(b)
      }

      case (a: Operation, b: Variable) =>
        unify(b, a)

      case (a: Operation, b: Operation) =>
        if (a.name != b.name || a.params.length != b.params.length)
          throw new Exception(s"Type mismatch: $a =/= $b")
        for (i <- a.params.indices)
          unify(a.params(i), b.params(i))

      case _ => throw new Exception("Wrong types.")
    }
  }

  def prune(t: Type): Type = t match {
    case v: Variable if v.typeOpt.isDefined =>
      val res = prune(v.typeOpt.get)
      v.typeOpt = Some(res)
      res
    case _ => t
  }

  def inferVariableType(t: Type, variables: mutable.Set[Variable]): Type = {
    val map = mutable.Map[Variable, Variable]()

    def inner(t: Type): Type = {
      prune(t) match {
        case v: Variable =>
          if (contains(variables, v)) v
          else map.getOrElseUpdate(v, nextVariable) // generic
        case Operation(name, args) =>
          Operation(name, args.map(inner))
      }
    }

    inner(t)
  }

  def contains(t: Type, v: Variable): Boolean = {
    prune(t) match {
      case `v` => true
      case Operation(_, params) => contains(params, v)
      case _ => false
    }
  }

  def contains(types: Iterable[Type], v: Variable): Boolean = types.exists(contains(_, v))

  private def asString(t: Type, variables: mutable.Set[String]): String = t match {
    case v: Variable => v.typeOpt match {
      case Some(t) =>
        asString(t, variables)
      case None =>
        variables.add(v.name)
        v.name
    }

    case Operation(name, params) =>
      if (params.isEmpty) name
      else if (name == arrow) {
        val tempLeft = params.take(params.length - 1).map(asString(_, variables)).mkString(", ")
        val parentheses = params match {
          case Operation(`arrow`, _) :: _ => true
          case (v: Inferer.Variable) :: _ if (v.typeOpt match {
            case Some(op: Operation) if op.name == arrow => true
            case _ => false
          }) => true
          case _ :: _ :: Nil => false
          case _ => true
        }

        val left = if (parentheses) s"($tempLeft)" else tempLeft
        val right = asString(params.last, variables)
        s"$left -> $right"
      } else {
        s"$name[${params.map(asString(_, variables)).mkString(", ")}]"
      }
  }

  trait Type {
    override def toString: String = {
      val variables = mutable.Set[String]()
      val s = asString(this, variables)
      if (variables.isEmpty) s else s"forall[${variables.toList.sorted.mkString(" ")}] $s"
    }
  }

  case class Variable(id: Int) extends Type {
    lazy val name: String = nextUniqueName
    var typeOpt: Option[Type] = None
  }

  case class Operation(name: String, params: Seq[Type]) extends Type

}

object Solution {
  private val envString =
    """head: forall[a] list[a] -> a
      |tail: forall[a] list[a] -> list[a]
      |nil: forall[a] list[a]
      |cons: forall[a] (a, list[a]) -> list[a]
      |cons_curry: forall[a] a -> list[a] -> list[a]
      |map: forall[a b] (a -> b, list[a]) -> list[b]
      |map_curry: forall[a b] (a -> b) -> list[a] -> list[b]
      |one: int
      |zero: int
      |succ: int -> int
      |plus: (int, int) -> int
      |eq: forall[a] (a, a) -> bool
      |eq_curry: forall[a] a -> a -> bool
      |not: bool -> bool
      |true: bool
      |false: bool
      |pair: forall[a b] (a, b) -> pair[a, b]
      |pair_curry: forall[a b] a -> b -> pair[a, b]
      |first: forall[a b] pair[a, b] -> a
      |second: forall[a b] pair[a, b] -> b
      |id: forall[a] a -> a
      |const: forall[a b] a -> b -> a
      |apply: forall[a b] (a -> b, a) -> b
      |apply_curry: forall[a b] (a -> b) -> a -> b
      |choose: forall[a] (a, a) -> a
      |choose_curry: forall[a] a -> a -> a
      |""".stripMargin

  def main(args: Array[String]): Unit = {
    val sc = new Scanner(System.in)
    val s = sc.nextLine
    solve(s)
  }

  def solve(s: String): Unit = {
    val lexemes = Parser.toLexemes(s)
    val expression = Parser.toExpression(lexemes)
    val environment: Inferer.Environment = Parser.loadEnvironment(envString)
    println(Inferer.infer(expression, environment))
  }
}