// https://www.hackerrank.com/challenges/area-under-curves-and-volume-of-revolving-a-curv/problem

val step = 0.001

def area(coefficients: List[Int], powers: List[Int], x: Double): Double = {
  val r = f(coefficients, powers, x)
  r * r * math.Pi
}

// This function will be used while invoking "Summation" to compute
// The area under the curve.
def f(coefficients: List[Int], powers: List[Int], x: Double): Double = {
  coefficients.zip(powers).map { case (c, p) => c * math.pow(x, p) }.sum
}

def summation(func: (List[Int], List[Int], Double) => Double, upperLimit: Int, lowerLimit: Int, coefficients: List[Int], powers: List[Int]): Double = {
  (BigDecimal(lowerLimit) to upperLimit by step).map(x => func(coefficients, powers, x.toDouble)).sum * step
}

// readLine() is deprecated in Scala 13, but it is called by HackerRank's predefined code.
// So it is added to fix the issue.
def readLine(): String = scala.io.StdIn.readLine()