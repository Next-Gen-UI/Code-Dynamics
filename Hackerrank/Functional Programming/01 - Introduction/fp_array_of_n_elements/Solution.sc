// https://www.hackerrank.com/challenges/fp-array-of-n-elements/problem

def f(num: Int): List[Int] = (0 until num).toList

// readInt() is deprecated in Scala 13, but it is called by HackerRank's predefined code.
// So it is added to fix the issue.
def readInt(): Int = scala.io.StdIn.readInt()