import scala.util.Random   //for verification
import scala.language.postfixOps

sealed abstract class Color
case object R extends Color
case object B extends Color
case object BB extends Color  //doubly black

sealed trait Tree[+A]
case object E extends Tree[Nothing]
case class T[A] (c: Color, left: Tree[A], key: A, right: Tree[A]) extends Tree[A]

object RBTree {
  def insert[A <% Ordered[A]] (tr: Tree[A], x: A): Tree[A] = makeBlack(ins(tr, x))

  def ins[A <% Ordered[A]] (tr: Tree[A], x: A): Tree[A] = tr match {
    case E => T(R, E, x, E)
    case T(c, left, y, right) =>
      if (x < y) balance(c, ins(left, x), y, right)
      else balance(c, left, y, ins(right, x))
  }

  def makeBlack[A](tr: Tree[A]): Tree[A] = tr match {
    case T(_, l, k, r) => T(B, l, k, r)
    case _ => tr
  }

  def balance[A](c: Color, left: Tree[A], key: A, right: Tree[A]) : Tree[A] =
    (c, left, key, right) match {
      case (B, T(R, T(R, a, x, b), y, c), z, d) => T(R, T(B, a, x, b), y, T(B, c, z, d))
      case (B, T(R, a, x, T(R, b, y, c)), z, d) => T(R, T(B, a, x, b), y, T(B, c, z, d))
      case (B, a, x, T(R, b, y, T(R, c, z, d))) => T(R, T(B, a, x, b), y, T(B, c, z, d))
      case (B, a, x, T(R, T(R, b, y, c), z, d)) => T(R, T(B, a, x, b), y, T(B, c, z, d))
      case _ => T(c, left, key, right)
    }

  def fromList [A <% Ordered[A]] (xs: Seq[A]) : Tree[A] = ((E: Tree[A]) /: xs) (insert)

  def toList[A] (tr: Tree[A]) : List[A] = tr match {
    case E => List()
    case T(_, left, x, right) => toList(left) ::: (x :: toList(right))
  }

  // test
  def isBlack[A](tr: Tree[A]) : Boolean = tr match {
    case E => true
    case T(B, _, _, _) => true
    case _ => false
  }

  def adjacentRed[A](tr: Tree[A]) : Boolean = tr match {
    case E => false
    case T(R, T(R, _, _, _), _, _) => true
    case T(R, _, _, T(R, _, _, _)) => true
    case T(_, left, _, right) => adjacentRed(left) || adjacentRed(right)
  }

  def eqBlack[A](tr: Tree[A]) = blackness(tr) > 0

  def blackness[A](tr: Tree[A]) : Int = tr match {
    case E => 1
    case T(c, left, _, right) => {
      val a = blackness(left)
      val b = blackness(right)
      val i = if (isBlack(tr)) 1 else 0
      if (a != b) -1000 else a + i
    }
  }

  def isRedBlack[A](tr: Tree[A]) = isBlack(tr) && (!adjacentRed(tr)) && eqBlack(tr)

  val N = 100
  def genList(r: Random) = r.shuffle(0 to N - 1).take(r.nextInt(N))

  def testBuild(xs: Seq[Int]) = {
    val tr = fromList(xs)
    assert(toList(tr) == xs.sortWith(_ < _), println("violate build invariant"))
    assert(isRedBlack(tr), println("violate red-black properties"))
  }

  def test() = {
    val r = Random
    for (_ <- 1 to N) {
      val xs = genList(r)
      testBuild(xs)
    }
    println(s"$N tests passed");
  }
}
