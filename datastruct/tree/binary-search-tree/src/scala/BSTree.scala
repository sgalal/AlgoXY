import scala.util.Random
import scala.language.postfixOps

// BST definition
sealed trait Tree[+A]

case object Empty extends Tree[Nothing]

case class Node[A] (left: Tree[A], key: A, right: Tree[A]) extends Tree[A]

object BSTree {
  // helpers
  def leaf[A] (x: A) : Tree[A] = Node(Empty, x, Empty)

  // in-order traverse
  def map [A, B] (f: A => B, tr: Tree[A]): Tree[B] =
    tr match {
      case Empty => Empty
      case Node(left, x, right) => Node(map(f, left), f(x), map(f, right))
    }

  def lookup[A <% Ordered[A]] (tr: Tree[A], x: A): Tree[A] =
    tr match {
      case Empty => Empty
      case Node(left, y, right) =>
        if (x == y) tr
        else if (x < y) lookup(left, x)
        else lookup(right, x)
    }

  def min[A] (tr: Tree[A]): A =
    tr match {
      case Node(Empty, x, _) => x
      case Node(left, _, _) => min(left)
    }

  def max[A] (tr: Tree[A]): A =
    tr match {
      case Node(_, x, Empty) => x
      case Node(_, _, right) => max(right)
    }

  def insert[A <% Ordered[A]] (tr: Tree[A], x: A): Tree[A] =
    tr match {
      case Empty => Node(Empty, x, Empty)
      case Node(left, y, right) =>
        if (x < y) Node(insert(left, x), y, right)
        else Node(left, y, insert(right, x))
    }

  def fromList [A <% Ordered[A]] (xs: Seq[A]) : Tree[A] = ((Empty: Tree[A]) /: xs) (insert)

  def toList[A] (tr: Tree[A]) : List[A] =
    tr match {
      case Empty => List()
      case Node(left, x, right) => toList(left) ::: (x :: toList(right))
    }

  val N = 100
  def genList(r: Random) = r.shuffle(0 to N - 1).take(r.nextInt(N))

  def testBuild(xs: Seq[Int]) = {
    assert(toList(fromList(xs)) == xs.sortWith(_ < _), println("violate build invariant"))
  }

  def test() = {
    val r = Random
    for (_ <- 1 to N) {
      testBuild(genList(r))
    }
    println(s"$N tests passed");
  }
}
