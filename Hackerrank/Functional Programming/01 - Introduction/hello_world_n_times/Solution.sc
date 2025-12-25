// https://www.hackerrank.com/challenges/fp-hello-world-n-times/problem

def f(n: Int): Unit = {
  (0 until n).foreach(_ => println("Hello World"))
}