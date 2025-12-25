// https://www.hackerrank.com/challenges/functions-and-fractals-sierpinski-triangles/problem

package recursion.functions_and_fractals_sierpinski_triangles

object Solution {
  def main(args: Array[String]): Unit = {
    drawTriangles(scala.io.StdIn.readInt())
  }

  def drawTriangles(n: Int): Unit = {
    def cut(field: IndexedSeq[IndexedSeq[Char]], n: Int, width: Int, height: Int): IndexedSeq[IndexedSeq[Char]] = {
      @scala.annotation.tailrec
      def innerCut(n: Int, x: Int, y: Int, left: Int, top: Int, width: Int, height: Int): Boolean = {
        def halfWidth = width / 2

        def halfHeight = height / 2

        n > 0 && (
          (y >= halfHeight && math.abs(halfWidth - x) <= height - 1 - y) ||
            innerCut(n - 1, (x + (if (y < halfHeight) (halfWidth + 1) / 2 else 0)) % (halfWidth + 1), y % halfHeight, left % (halfWidth + 1), top % halfHeight, halfWidth, halfHeight)
          )
      }

      field.indices.map(y => {
        field(y).indices.map(x => {
          if (innerCut(n, x, y, 0, 0, width, height)) zero else field(y)(x)
        })
      })
    }

    cut(init, n, width, height)
    println(cut(init, n, width, height).map(_.mkString("")).mkString("\n"))
  }

  def init: IndexedSeq[IndexedSeq[Char]] = {
    (0 until height).map(y => {
      (0 until width).map(x => {
        if (math.min(x, width - 1 - x) >= (height - 1 - y)) one else zero
      })
    })
  }

  def width = 63

  def height = 32

  def zero = '_'

  def one = '1'
}