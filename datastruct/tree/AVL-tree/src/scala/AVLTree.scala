import scala.util.Random    //for verification
import scala.language.postfixOps

object AVLTree {
  sealed trait Tree[+A]
  case object Empty extends Tree[Nothing]
  case class Br[A] (left: Tree[A], key: A, right: Tree[A], delta: Int) extends Tree[A]

  def insert[A <% Ordered[A]] (tr: Tree[A], x: A): Tree[A] = ins(tr, x)._1

  // returns (t, d), where t: tree, d: increment of of height
  def ins[A <% Ordered[A]] (tr: Tree[A], x: A): (Tree[A], Int) = tr match {
    case Empty => (Br(Empty, x, Empty, 0), 1)
    case Br(l, k, r, d) => if (x == k) (Br(l, k, r, d), 0)
      else if (x < k) node(ins(l, x), k, (r, 0), d)
      else node((l, 0), k, ins(r, x), d)
  }

  def node(left: (Tree[A], Int), key: A, right: (Tree[A], Int), d: Int): (Tree[A], Int) = {
    val d1 = d + right._2 - left._2
    val delta = deltaH(d, d1, left._2, right._2)
    balance(Br(left._1, key, right._1, d1), delta)
  }
}
