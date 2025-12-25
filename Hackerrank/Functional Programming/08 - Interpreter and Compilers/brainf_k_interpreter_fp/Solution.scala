// https://www.hackerrank.com/challenges/brainf-k-interpreter-fp/problem

package interpreter_and_compilers.brainf_k_interpreter_fp

trait Command {
  def call(machine: Machine): Machine

  def check(machine: Machine, f: Machine => Machine): Machine = {
    val m = machine.copy(count = machine.count + 1)
    if (machine.count < Program.maxCommandCount)
      f(m)
    else m
  }
}

object Command {
  val brainChars = "><+-.,[]"

  def parse(text: List[Char]): (List[Command], List[Char]) = text match {
    case Nil => (Nil, Nil)
    case c :: cs =>
      val (command, rest) = c match {
        case '>' => (Greater(), cs)
        case '<' => (Less(), cs)
        case '+' => (Plus(), cs)
        case '-' => (Minus(), cs)
        case '.' => (Point(), cs)
        case ',' => (Comma(), cs)
        case '[' =>
          val (commands, rest) = parse(cs)
          (Open(commands), rest)
        case ']' => (Close(), cs)
      }

      command match {
        case _: Close => (command :: Nil, rest)
        case _ =>
          val (list, restOfRest) = parse(rest)
          (command :: list, restOfRest)
      }
  }
}

case class Greater() extends Command {
  override def call(machine: Machine): Machine = check(machine, m => m.copy(cursor = m.cursor + 1))
}

case class Less() extends Command {
  override def call(machine: Machine): Machine = check(machine, m => m.copy(cursor = m.cursor - 1))
}

case class Plus() extends Command {
  override def call(machine: Machine): Machine = check(machine,
    m => {
      m.memory.update(m.cursor, (m.memory(m.cursor) + 1).toByte)
      m
    }
  )
}

case class Minus() extends Command {
  override def call(machine: Machine): Machine = check(machine,
    m => {
      m.memory.update(m.cursor, (m.memory(m.cursor) - 1).toByte)
      m
    }
  )
}

case class Point() extends Command {
  override def call(machine: Machine): Machine = check(machine,
    m => {
      print(m.memory(m.cursor).toChar)
      m
    }
  )
}

case class Comma() extends Command {
  override def call(machine: Machine): Machine = check(machine,
    m => {
      m.memory(m.cursor) = m.input.head
      m.copy(input = m.input.tail)
    }
  )
}

case class Close() extends Command {
  override def call(machine: Machine): Machine = check(machine,
    m => m.copy(count = m.count + (if (m.memory(m.cursor) == 0) -2 else 0))
  )
}

trait CommandSequence extends Command {
  def commands: List[Command] = Nil
}

case class Open(override val commands: List[Command]) extends CommandSequence {
  @scala.annotation.tailrec
  final override def call(machine: Machine): Machine = {
    val m = machine.copy(count = machine.count + 1)
    if (machine.count < Program.maxCommandCount) {
      if (m.memory(m.cursor) == 0) m.copy(count = m.count + 1)
      else call(commands.foldLeft(m)((innerMachine, command) => command.call(innerMachine)))
    }
    else m
  }
}

class Program(text: List[Char]) extends CommandSequence {
  override val commands: List[Command] = Command.parse(text)._1

  override def call(machine: Machine): Machine = commands.foldLeft(machine)((m, command) => command.call(m))
}

object Program {
  val maxCommandCount = 100000
}

case class Machine(
                    cursor: Int,
                    input: Seq[Byte],
                    memory: Array[Byte],
                    count: Int
                  )

object Solution {
  def main(args: Array[String]): Unit = {
    val sc = new java.util.Scanner(System.in)
    val (_, m) = (sc.nextInt, sc.nextInt)

    sc.nextLine()
    val input = sc.nextLine.map(c => c.toByte)

    val text = (0 until m).flatMap(_ => sc.nextLine.filter(c => Command.brainChars.contains(c))).toList

    val program = new Program(text)
    val machine = program.call(Machine(0, input, new Array[Byte](Program.maxCommandCount), 0))
    if (machine.count > Program.maxCommandCount) {
      println
      println("PROCESS TIME OUT. KILLED!!!")
    }
  }
}