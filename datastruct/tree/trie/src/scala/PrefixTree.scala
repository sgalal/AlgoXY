import scala.util.Random    // for verification
import scala.language.postfixOps

object PrefixTree {
  case class Tree[+K, V] (v: Option[V], cs: List[(List[K], Tree[K, V])]) {
    val value = v
    val children = cs
  }

  def empty[K, V](): Tree[K, V] = Tree(None, List())

  def leaf[K, V](v: V): Tree[K, V] = Tree(Some(v), List())

  def insert[K, V] (t: Tree[K, V], key: List[K], v: V): Tree[K, V] = {
    def ins[K, V] (ts: List[(List[K], Tree[K, V])],
                   key: List[K], v: V): List[(List[K], Tree[K, V])] =
      ts match {
        case List() => List((key, leaf(v)))
        case p :: ps => {
          val (key1, t1) = p
          if (key1 == key) {
            (key, Tree(Some(v), t1.children)) :: ps  // overwrite
          } else if (hasPrefix(key1, key)) {
            branch(key, v, key1, t1) :: ps
          } else {
            p :: ins(ps, key, v)
          }
        }
      }
    Tree(t.value, ins(t.children, key, v))
  }

  def hasPrefix[K] (key1: List[K], key2: List[K]) =
    ! key1.isEmpty && ! key2.isEmpty && key1.head == key2.head

  def branch[K, V] (key1: List[K], v: V,
                    key2: List[K], t2: Tree[K, V]): (List[K], Tree[K, V]) = {
    val key = lcp(key1, key2)
    val m = key.length
    val newKey1 = key1.drop(m)
    val newKey2 = key2.drop(m)
    if (key1 == key) { // insert "an" into "another"
      (key, Tree(Some(v), List((newKey2, t2))))
    } else if (key2 == key) { // insert "another" into "an"
      (key, insert(t2, newKey1, v))
    } else {
      (key, Tree(None, List((newKey1, leaf(x)), (newKey2, t2))))
    }
  }

  // the longest common prefix
  def lcp[K] (xs: List[K], ys: List[K]): List[K] =
    if (!xs.isEmpty && !ys.isEmpty && xs.head == ys.head) {
      xs.head :: lcp(xs.tail, ys.tail)
    } else {
      List()
    }

  // lookup
  def lookup[K, V] (t: Tree[K, V], key: List[K]): Option[V] = {
    def extract[K] (xs: List[K], ys: List[K]) = ys.drop(lcp(xs, ys).length)
    def find[K, V] (assoc: List[(List[K], Tree[K, V])], key: List[K]): Option[V] =
      assoc match {
        case List() => None
        case p :: ps => {
          val (key1, t1) = p
          if (key1 == key) {
            t1.value
          } else if (ks.startsWith(key1)) {
            lookup(t1, extract(key, key1))
          }
        }
      }
    find(t.children, key)
  }
}
