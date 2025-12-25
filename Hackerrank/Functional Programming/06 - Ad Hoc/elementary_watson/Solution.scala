// https://www.hackerrank.com/challenges/elementary-watson/problem

package ad_hoc.elementary_watson

import java.util.UUID

import scala.collection.immutable.HashSet

class Parser(lines: List[String]) {

  def operations: List[Operation] = lines.map(toLexemes).map(toOperation)

  def toLexemes(s: String): List[Lexeme] = {
    val lexemeNames = Map(
      "#" -> NumberSign,
      "." -> Point,
      "?" -> Question,
      "!" -> Exclamation,
      "<" -> Less,
      ">" -> Greater,
      "[" -> OpenSquareBracket,
      "]" -> CloseSquareBracket,
      "(" -> OpenRoundBracket,
      ")" -> CloseRoundBracket,
      "{" -> OpenFigureBracket,
      "}" -> CloseFigureBracket,
      ": " -> Colon,
      ", " -> Comma,
      " => " -> Arrow,
      " = " -> Equal,
      " /= " -> NotEqual
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
        case '%' =>
          val (nextIndex, lexemeName) = parseLexeme(state.index + 1, _ => true)
          State(nextIndex, Comment(lexemeName) :: state.acc)

        case d: Char if d.isLetter =>
          val (nextIndex, lexemeName) = parseLexeme(state.index, c => c.isLetterOrDigit || c == '-')
          val lexeme = lexemeNames.getOrElse(lexemeName, Identifier(lexemeName))
          State(nextIndex, lexeme :: state.acc)

        case ',' | ':' if state.index + 1 < s.length && s(state.index + 1) == ' ' =>
          State(state.index + 2, lexemeNames(s.substring(state.index, state.index + 2)) :: state.acc)

        case ' ' =>
          val (nextIndex, lexemeName) = parseLexeme(state.index, c => Set(' ', '/', '=', '>').contains(c))
          State(nextIndex, lexemeNames(lexemeName) :: state.acc)

        case d: Char if lexemeNames.contains(d.toString) =>
          val lexeme = lexemeNames(d.toString)
          State(index = state.index + 1, lexeme :: state.acc)
      }

      inner(nextState)
    } else state

    inner(State()).acc.reverse
  }

  def toOperation(lexemes: List[Lexeme]): Operation = lexemes match {
    case Nil | (_: Comment) :: Nil => Nop

    case list :+ Point =>
      val (expr1, expr2) = list.span(_ != Arrow)
      val (terms, simpleTerm) = if (expr2.isEmpty) {
        (
          Nil,
          parseSimpleTerm(expr1)._1
        )
      }
      else {
        (
          parseComplexTerms(expr1.tail.tail.init),
          parseSimpleTerm(expr2.tail.init)._1
        )
      }
      Rule(Query(terms.toVector), simpleTerm)

    case list :+ Question =>
      Query(parseComplexTerms(list.tail.init).toVector)

    case Identifier("quit") :: Exclamation :: Nil =>
      Command

    case _ => throw new Exception("Wrong expression")
  }

  def parseSimpleTerms(lexemes: List[Lexeme]): List[SimpleTerm] = if (lexemes.isEmpty) Nil
  else {
    val (term, rest) = parseSimpleTerm(lexemes)

    rest match {
      case Nil => term :: Nil
      case Comma :: rest2 => term :: parseSimpleTerms(rest2)
      case _ => throw new Exception("Wrong simple terms")
    }
  }

  def parseRelationalTerm(lexemes: List[Lexeme]): SimpleTerm = lexemes match {
    case (identifier: Identifier) :: Colon :: simpleTerms =>
      Relation(identifier.name, parseSimpleTerms(simpleTerms).toVector)
    case _ => throw new Exception("Wrong relational term")
  }

  def parseSimpleTerm(lexemes: List[Lexeme]): (SimpleTerm, List[Lexeme]) =
    lexemes match {
      case (identifier: Identifier) :: rest =>
        (Name(identifier.name), rest)

      case NumberSign :: (identifier: Identifier) :: rest =>
        (Variable(identifier.name, ""), rest)

      case OpenSquareBracket :: rest =>
        val (expr1, expr2) = findCloseBracket(rest, OpenSquareBracket, CloseSquareBracket)
        (parseRelationalTerm(expr1), expr2)

      case _ => throw new Exception("Wrong simple terms")
    }

  def parseComplexTerms(lexemes: List[Lexeme]): List[ComplexTerm] = if (lexemes.isEmpty) Nil else {
    val (term, rest) = lexemes match {
      case Less :: rest =>
        val (expr1, expr2) = findCloseBracket(rest, Less, Greater)
        val (term0, rest1) = parseSimpleTerm(expr1)
        val isEquality = rest1.head == Equal
        val (term1, _) = parseSimpleTerm(rest1.tail)
        (if (isEquality) new Equal(term0, term1) else new NotEqual(term0, term1), expr2)
      case _ =>
        parseSimpleTerm(lexemes)
    }

    rest match {
      case Nil => term :: Nil
      case Comma :: rest2 => term :: parseComplexTerms(rest2)
      case _ => throw new Exception("Wrong complex terms")
    }
  }

  def findCloseBracket(lexemes: List[Lexeme], openBracket: Lexeme, closeBracket: Lexeme): (List[Lexeme], List[Lexeme]) = {
    case class Acc(lexemes: List[Lexeme] = Nil, bracketCount: Int = 1)
    @scala.annotation.tailrec
    def inner(lexemes: List[Lexeme], acc: Acc = Acc()): (List[Lexeme], List[Lexeme]) = {
      lexemes match {
        case (lex@`closeBracket`) :: ls =>
          if (acc.bracketCount == 1) (acc.lexemes.reverse, ls)
          else inner(ls, Acc(lex :: acc.lexemes, acc.bracketCount - 1))
        case (lex@`openBracket`) :: ls => inner(ls, Acc(lex :: acc.lexemes, acc.bracketCount + 1))
        case lex :: ls => inner(ls, Acc(lex :: acc.lexemes, acc.bracketCount))
        case Nil => throw new Exception("Wrong expression")
      }
    }

    inner(lexemes)
  }

  trait Lexeme

  trait PrimaryLexeme extends Lexeme

  case class Comment(text: String) extends PrimaryLexeme

  case class Identifier(name: String) extends PrimaryLexeme

  case object NumberSign extends PrimaryLexeme

  case object Point extends PrimaryLexeme

  case object Question extends PrimaryLexeme

  case object Exclamation extends PrimaryLexeme

  case object Less extends PrimaryLexeme

  case object Greater extends PrimaryLexeme

  case object Equal extends PrimaryLexeme

  case object NotEqual extends PrimaryLexeme

  case object Arrow extends PrimaryLexeme

  case object Comma extends PrimaryLexeme

  case object Colon extends PrimaryLexeme

  case object OpenRoundBracket extends PrimaryLexeme

  case object CloseRoundBracket extends PrimaryLexeme

  case object OpenFigureBracket extends PrimaryLexeme

  case object CloseFigureBracket extends PrimaryLexeme

  case object OpenSquareBracket extends PrimaryLexeme

  case object CloseSquareBracket extends PrimaryLexeme

}

trait ComplexTerm {
  def substitute(variable: Variable, term: SimpleTerm): ComplexTerm

  def variableNames: Set[String]

  def withNamespace(knowledge: Knowledge): ComplexTerm

  def resolve(binding: Map[Variable, SimpleTerm]): ComplexTerm
}

trait SimpleTerm extends ComplexTerm {
  override def substitute(variable: Variable, term: SimpleTerm): SimpleTerm

  def name: String

  def similar(term: SimpleTerm): Boolean = getClass == term.getClass && name == term.name

  override def withNamespace(knowledge: Knowledge): SimpleTerm

  override def resolve(binding: Map[Variable, SimpleTerm]): SimpleTerm
}

case class Name(override val name: String) extends SimpleTerm {
  override def substitute(variable: Variable, term: SimpleTerm): Name = this

  override def variableNames: Set[String] = HashSet()

  override def toString: String = name

  override def withNamespace(knowledge: Knowledge): Name = this

  override def resolve(binding: Map[Variable, SimpleTerm]): SimpleTerm = this
}

case class Variable(override val name: String, namespace: String) extends SimpleTerm {
  def isGlobal: Boolean = namespace == ""

  override def substitute(variable: Variable, term: SimpleTerm): SimpleTerm = if (this == variable) term else this

  override def variableNames: Set[String] = HashSet(id)

  def id: String = s"$namespace.$name"

  override def toString: String = s"#$name"

  override def similar(term: SimpleTerm): Boolean = term match {
    case variable: Variable => super.similar(variable) && namespace == variable.namespace
    case _ => false
  }

  override def withNamespace(knowledge: Knowledge): Variable =
    Variable(name, knowledge.guid)

  override def resolve(binding: Map[Variable, SimpleTerm]): SimpleTerm =
    if (binding.contains(this)) binding(this).resolve(binding) else this
}

case class Relation(override val name: String, terms: Vector[SimpleTerm]) extends SimpleTerm {
  override def substitute(variable: Variable, term: SimpleTerm): Relation =
    Relation(this.name, terms.map(_.substitute(variable, term)))

  override def similar(term: SimpleTerm): Boolean = term match {
    case relation: Relation => super.similar(relation) && terms.size == relation.terms.size
    case _ => false
  }

  override def variableNames: Set[String] =
    terms.map(_.variableNames) reduce (_ ++ _)

  override def toString: String = s"[$name: ${terms.mkString(", ")}]"

  override def withNamespace(knowledge: Knowledge): Relation =
    Relation(name, terms.map(_.withNamespace(knowledge)))

  override def resolve(binding: Map[Variable, SimpleTerm]): SimpleTerm =
    Relation(name, terms.map(_.resolve(binding)))
}

abstract class EqualOrNotEqual(val lhs: SimpleTerm, val rhs: SimpleTerm) extends ComplexTerm {
  override def variableNames: Set[String] = lhs.variableNames ++ rhs.variableNames
}

case class Equal(override val lhs: SimpleTerm, override val rhs: SimpleTerm) extends EqualOrNotEqual(lhs, rhs) {
  override def substitute(variable: Variable, term: SimpleTerm): Equal =
    Equal(lhs.substitute(variable, term), rhs.substitute(variable, term))

  override def toString: String = s"<$lhs = $rhs>"

  override def withNamespace(knowledge: Knowledge): Equal =
    Equal(lhs.withNamespace(knowledge), rhs.withNamespace(knowledge))

  override def resolve(binding: Map[Variable, SimpleTerm]): Equal =
    Equal(lhs.resolve(binding), rhs.resolve(binding))
}

case class NotEqual(override val lhs: SimpleTerm, override val rhs: SimpleTerm) extends EqualOrNotEqual(lhs, rhs) {
  override def substitute(variable: Variable, term: SimpleTerm): NotEqual =
    NotEqual(lhs.substitute(variable, term), rhs.substitute(variable, term))

  override def toString: String = s"<$lhs /= $rhs>"

  override def withNamespace(knowledge: Knowledge): NotEqual =
    NotEqual(lhs.withNamespace(knowledge), rhs.withNamespace(knowledge))

  override def resolve(binding: Map[Variable, SimpleTerm]): NotEqual =
    NotEqual(lhs.resolve(binding), rhs.resolve(binding))
}

trait Operation

case object Command extends Operation

case object Nop extends Operation

case class Rule(premise: Query, conclusion: SimpleTerm) extends Operation {
  override def toString: String = s"{(${premise.terms.mkString(", ")}) => $conclusion}."

  def resolve(binding: Map[Variable, SimpleTerm]): Rule =
    Rule(premise.resolve(binding), conclusion.resolve(binding))

  def use(term: SimpleTerm, knowledge: Knowledge): Vector[Map[Variable, SimpleTerm]] = {
    val nextKnowledge = knowledge.copy(guid = UUID.randomUUID().toString)
    withNamespace(nextKnowledge).useNext(term, nextKnowledge)
  }

  def withNamespace(knowledge: Knowledge): Rule =
    Rule(premise.withNamespace(knowledge), conclusion.withNamespace(knowledge))

  def useNext(term: SimpleTerm, knowledge: Knowledge): Vector[Map[Variable, SimpleTerm]] =
    Solution.unify(term, conclusion) match {
      case Some(conclusion_binding) =>
        val bound_premise = premise.resolve(conclusion_binding)
        val premise_bindings = bound_premise.prove(knowledge)
        premise_bindings.map(_.++(conclusion_binding))
      case None => Vector()
    }
}

case class Query(terms: Vector[ComplexTerm]) extends Operation {
  override def toString: String = s"(${terms.mkString(", ")})?"

  def resolve(bindings: Map[Variable, SimpleTerm]): Query =
    Query(terms.map(_.resolve(bindings)))

  def prove(knowledge: Knowledge): Vector[Map[Variable, SimpleTerm]] = {
    if (terms.isEmpty) Vector(Map())
    else {
      val term = terms.head
      val tail = terms.tail

      def deepBinding(bindings: Vector[Map[Variable, SimpleTerm]]) =
        bindings.map(binding => Query(tail.map(_.resolve(binding)))
          .prove(knowledge)
          .map(_.++(binding)))
          .fold(Vector())((a, b) => a ++ b)

      term match {
        case equal: Equal =>
          val bindings = Solution.unify(equal.lhs, equal.rhs) match {
            case Some(binding) => Vector(binding)
            case None => Vector()
          }
          deepBinding(bindings)

        case notEqual: NotEqual =>
          val (lhs, rhs) = (notEqual.lhs, notEqual.rhs)

          if (lhs == rhs) Vector()
          else Query(tail).prove(knowledge)
            .filter(binding => Solution.unify(lhs.resolve(binding), rhs.resolve(binding)).isEmpty)
        case simpleTerm: SimpleTerm =>
          val bindings = knowledge.rules.map(rule => rule.use(simpleTerm, knowledge)).fold(Vector())((a, b) => a ++ b)
          deepBinding(bindings)
      }
    }
  }

  def withNamespace(knowledge: Knowledge): Query = Query(terms.map(_.withNamespace(knowledge)))
}

case class Knowledge(rules: Vector[Rule] = Vector[Rule](), guid: String = "")

object Solution {
  def unify(lhs: SimpleTerm, rhs: SimpleTerm): Option[Map[Variable, SimpleTerm]] =
    unify(Vector(Equal(lhs, rhs)))

  def unify(pairs: Vector[Equal]): Option[Map[Variable, SimpleTerm]] = {
    if (pairs.isEmpty) Option(Map())
    else {
      val lhs = pairs.head.lhs
      val rhs = pairs.head.rhs

      if (lhs == rhs) unify(pairs.tail)
      else {
        (lhs, rhs) match {
          case (variable: Variable, _: Relation) if rhs.variableNames.contains(variable.id) =>
            None

          case (variable: Variable, _) =>
            unify(pairs.tail.map(_.substitute(variable, rhs))).map(_.updated(variable, rhs))

          case (_, _: Variable) =>
            unify(pairs.tail.+:(Equal(rhs, lhs)))

          case (relation0: Relation, relation1: Relation) if relation0.similar(relation1) =>
            unify(relation0.terms.zip(relation1.terms).map { case (l, r) => Equal(l, r) } ++ pairs)

          case _ =>
            None
        }
      }
    }
  }

  def main(args: Array[String]): Unit = {
    val lines = io.Source.stdin.getLines().toList

    new Parser(lines).operations.foldLeft(Knowledge())((knowledge, operation) => {
      operation match {
        case Command =>
          println("Bye.")
          knowledge

        case rule: Rule =>
          println("Ok.")
          knowledge.copy(rules = knowledge.rules :+ rule)

        case query: Query =>
          val bindings = query.prove(knowledge)

          if (bindings.isEmpty) {
            println("UNSAT")
          } else if (bindings.size == 1 && !bindings.head.exists(_._1.isGlobal)) {
            println("SAT")
          } else {
            bindings.foreach(binding => {
              val filteredBinding = binding.filter(_._1.isGlobal)
              println("SAT:")
              println("=====")

              println(
                filteredBinding
                  .toSeq
                  .map(_._1)
                  .sortBy(_.name)
                  .map(variable => s"$variable := ${filteredBinding(variable).resolve(binding)}")
                  .mkString("\n")
              )
            })
          }
          println("Ready.")
          knowledge

        case Nop =>
          knowledge
      }
    })
  }
}