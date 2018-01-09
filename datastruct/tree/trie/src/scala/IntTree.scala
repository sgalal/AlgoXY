import scala.util.Random    //for verification
import scala.language.postfixOps

object IntTree {
  sealed trait IntTree[+A]
  case object Empty extends IntTree[Nothing]
  case class Leaf[A] (key: Int, value: A) extends IntTree[A]
  case class Branch[A] (prefix: Int, mask: Int,
                        left: IntTree[A], right: IntTree[A]) extends IntTree[A]

  //Auxiliary

  /*
   * Mask a number with a mask, where
   *   number x = a(n) a(n-1) ... a(i) a(i-1) ... a(0)
   *   mask   m = 100...0  (= 2^i)
   *   result: a(n) a(n-1) ... a(i) 00...0
   */
  def maskbit(x: Int, mask: Int): Int = x & (~(mask - 1))

  /*
   * Test if the next bit after mask bit is zero
   * e.g. number x = a(n) a(n-1) ... a(i) b ...a(0)
   *      mask   m = 100..0 (= 2^i)
   *  returns true if b = 0; false if b = 1
   */
  def isZero(x: Int, mask: Int): Boolean = (x & (mask >> 1)) == 0

  //find the longest common prefix, returns a pair: (prefix, mask)
  def lcp(p1: Int, p2: Int): (Int, Int) = {
    def nbits(x: Int) : Int = (x == 0 ? 0 : (1 + nbits(x >> 1)))
    val mask = 1 << nbits(p1 ^ p2)
    val prefix = maskbit(p1, mask)
    (prefix, mask)
  }

  def join[A] (p1: Int, t1: IntTree[A], p2: Int, t2: IntTree[A]): IntTree[A] = {
    val (p, m) = lcp(p1, p2)
    if (isZero(p1, m)) {
      Branch(p, m, t1, t2)
    } else {
      Branch(p, m, t2, t1)
    }
  }

  def isMatch(key: Int, prefix: Int, mask: Int): Boolean = mask(key, mask) == prefix

  def insert[A] (tr: IntTree[A], key: Int, value: A): IntTree[A] = tr match {
  }
}
