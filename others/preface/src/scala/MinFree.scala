/*
 * Given a list of non-negative numbers, find the minimum free number,
 * which is the minimum one not in this list.
 */

import scala.annotation.tailrec
import scala.language.postfixOps

object MinFree {
  def minFree(xs : List[Int]) : Int = {
    @tailrec def bsearch(xs : List[Int], l : Int, u : Int) : Int = {
      val m = (l + u) / 2
      val (as, bs) = xs partition (_ <= m)
      if (xs isEmpty)
        l
      else if ((as length) == m - l + 1)
        bsearch(bs, m + 1, u)
      else
        bsearch(as, l, m)
    }
    bsearch(xs, 0, xs length)
  }

  def test() {
    val test = minFree(List(8, 23, 9, 0, 12, 11, 1, 10, 13, 7, 41, 4, 14, 21, 5, 17, 3, 19, 2, 6))
    println(s"==>$test");
  }
}
