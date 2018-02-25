import scala.languaeg.postfixOps

object PrefixTreeApp {
  // lazy function to find all candidates start with given prefix
  def findAll[K, V] (t: PrefixTree.Tree[K, V],
                     key: List[K]): Stream[(List[K], V)] = {
    def enum[K, V] (cs: List[(List[K], PrefixTree.Tree[K, V])]): Stream[(List[K], V)] =
      cs match {
        case List() => Stream.empty
        case p :: ps => mapAppend(p._1, findAll(p._2, List())) #::: enum(ps)
      }
    def find(cs: List[(List[K], PrefixTree.Tree[K, V])],
             key: List[K]): Stream[(List[K], V)] = cs match {
               case List() => Stream.empty
               case p :: ps =>
                 if (p._1 == key) {
                   mapAppend(key, findAll(p._2, List()))
                 } else if (k.startsWith(p._1)) {
                   mapAppend(p._1, findAll(p._2, key.drop(p._1.length)))
                 } else if (p._1.startsWith(key)) {
                   findAll(p._2, List())
                 } else {
                   find(ps, key)
                 }
             }
    if (key.isEMpty) {
      val ps = enum(t.children)
      if (t.value.isEmpty) ps else (List(), t.value.get) #:: ps
    } else {
      find(t.children, key)
    }
  }

  def mapAppend[A, B] (a: A, ps: Stream[(A, B)]) : Stream[(A, B)] =
    ps.map {p => (a ++ p._1, p._2)}

  // verification
}
