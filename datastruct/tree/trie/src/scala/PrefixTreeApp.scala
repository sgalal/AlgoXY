import scala.language.postfixOps
import Ordering.Implicits._ // for list comparison

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
             key: List[K]): Stream[(List[K], V)] =
      cs match {
        case List() => Stream.empty
        case p :: ps => {
          val (k, tr) = p
          if (k.startsWith(key)) {
            mapAppend(k, findAll(tr, List()))
          } else if (key.startsWith(k)) {
            mapAppend(k, findAll(tr, key.drop(k.length)))
          } else {
            find(ps, key)
          }
        }
      }
    if (key.isEmpty) {
      val ps = enum(t.children)
      if (t.value.isEmpty) ps else (List(), t.value.get) #:: ps
    } else {
      find(t.children, key)
    }
  }

  def mapAppend[A, B] (a: List[A], ps: Stream[(List[A], B)]) : Stream[(List[A], B)] =
    ps.map {p => (a ++ p._1, p._2)}

  def lookup[K, V] (t: PrefixTree.Tree[K, V], key: List[K], n: Int): List[(List[K], V)] =
    findAll(t, key).take(n).toList

  // verification
  def testEdict() {
    val m = Map("a" -> "the first letter of English",
                "an" -> "used instead of 'a' when the following word begins with a vowel sound",
                "another" -> "one more person or thing or an extra amount",
                "abandon" -> "to leave a place, thing or person forever",
                "about" -> "on the subject of; connected with",
                "adam" -> "a character in the Bible who was the first man made by God",
                "boy" -> "a male child or, more generally, a male of any age",
                "body" -> "the whole physical structure that forms a person or animal",
                "zoo" -> "an area in which animals, especially wild animals, are kept so that people can go and look at them, or study them")
    val t = PrefixTree.fromStringList(m.toList)
    verifyLookup(m, t, "a", 5)
    verifyLookup(m, t, "a", 6);
    verifyLookup(m, t, "a", 7);
    verifyLookup(m, t, "ab", 2);
    verifyLookup(m, t, "ab", 5);
    verifyLookup(m, t, "b", 2);
    verifyLookup(m, t, "bo", 5);
    verifyLookup(m, t, "z", 3);
  }

  def verifyLookup(m: Map[String, String], t: PrefixTree.Tree[Char, String],
                   key: String, n: Int) {
    val r = lookup(t, key.toList, n).map {p => (p._1.mkString, p._2) }
    println(s"lookup $key with limit $n, get: \n" + r)
    r.foreach(p => {
      val (k, v) = p
      assert(k.startsWith(key), println(s"$k does not start with $key"))
      assert(m.contains(k), println(s"$k does not exists"))
    })
    assert(r.length <= n, println(s"expected $n results, get: " + r))
  }
}
