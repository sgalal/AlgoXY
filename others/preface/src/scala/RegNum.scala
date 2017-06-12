/* Regular Number, generate numbers only with factors of 2, 3, 5. */
import scala.math.BigInt

object RegNum {
  lazy val ns: Stream[BigInt] = 1 #:: merge(ns map {_ * 2}, merge(ns map {_ * 3}, ns map {_ * 5}))

  /*
   * We can't use Haskell like case (x #:: xs, y #:: ys) to match
   * As the tail will be forced to evaluat, which cause stack overflow
   */

  def merge(a: Stream[BigInt], b: Stream[BigInt]) : Stream[BigInt] =
    (a, b) match {
      case (Stream.Empty, _) => b
      case (_, Stream.Empty) => a
      case (_, _) =>
        if (a.head < b.head) a.head #:: merge(a.tail, b)
        else if (a.head == b.head) a.head #:: merge(a.tail, b.tail)
        else b.head #:: merge(a, b.tail)
    }

  //ns.take(1500).last
}
