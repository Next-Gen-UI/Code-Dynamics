// https://www.hackerrank.com/challenges/fractal-trees/problem

package recursion.fractal_trees

object Solution {
  def main(args: Array[String]): Unit = {
    drawTrees(scala.io.StdIn.readInt())
  }

  def drawTrees(n: Int): Unit = {
    def draw(field: IndexedSeq[IndexedSeq[Char]], n: Int, width: Int, height: Int): IndexedSeq[IndexedSeq[Char]] = {
      @scala.annotation.tailrec
      def innerDraw(n: Int, x: Int, y: Int, left: Int, top: Int, width: Int, height: Int): Boolean = {
        def halfWidth = width / 2

        def halfHeight = height / 2

        def trunkHeight = (height + 1) / 4

        n > 0 && (
          (y >= 3 * trunkHeight - 1 && y < 4 * trunkHeight - 1 && x == halfWidth - 1) || y >= 2 * trunkHeight - 1 && y < 3 * trunkHeight - 1 && math.abs(halfWidth - 1 - x) == 3 * trunkHeight - 1 - y ||
            innerDraw(n - 1, if (x < halfWidth) x else x - halfWidth
              , y, 0, 0, halfWidth, halfHeight)
          )
      }

      field.indices.map(y => {
        field(y).indices.map(x => {
          if (innerDraw(n, x, y, 0, 0, width, height)) one else zero //field(y)(x)
        })
      })
    }

    println(draw(init, n, width, height).map(zero.toString * shift + _.mkString("") + zero.toString * shift).mkString("\n"))
  }

  def width = 64

  def shift = 18

  def height = 63

  def zero = '_'

  def one = '1'

  def init: IndexedSeq[IndexedSeq[Char]] = {
    (0 until height).map(_ => {
      (0 until width).map(_ => {
        zero
      })
    })
  }
}
