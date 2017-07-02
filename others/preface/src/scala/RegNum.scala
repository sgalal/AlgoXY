/* Regular Number, generate numbers only with factors of 2, 3, 5. */
import scala.math.BigInt
import scala.collection.mutable.Queue
import scala.collection.mutable.Seq

object RegNum {
  lazy val ns: Stream[BigInt] = 1 #:: merge(ns map {_ * 2}, merge(ns map {_ * 3}, ns map {_ * 5}))

  /*
   * We can't use Haskell like case (x #:: xs, y #:: ys) to match
   * As the tail will be forced to evaluat, which cause stack overflow
   */

  def merge[T <% Ordered[T]](a: Stream[T], b: Stream[T]) : Stream[T] =
    if (a.head < b.head) a.head #:: merge(a.tail, b)
    else if (a.head == b.head) a.head #:: merge(a.tail, b.tail)
    else b.head #:: merge(a, b.tail)

  //ns.take(1500).last

  def get(n: Int) = {
    var xs : Seq[BigInt] = Seq(1)
    val q2 : Queue[BigInt] = Queue(2)
    val q3 : Queue[BigInt] = Queue(3)
    val q5 : Queue[BigInt] = Queue(5)
    def generate(n : Int) : Seq[BigInt] = {
      if (n == 1) xs
      else {
        val x = q2.head min q3.head min q5.head
        xs = xs :+ x
        if (x == q2.head) {
          q2.dequeue
          q2 enqueue 2 * x
          q3 enqueue 3 * x
          q5 enqueue 5 * x
        } else if (x == q3.head) {
          q3.dequeue
          q3 enqueue 3 * x
          q5 enqueue 5 * x
        } else {
          q5.dequeue
          q5 enqueue 5 * x
        }
        generate(n - 1)
      }
    }
    generate(n)
  }

  //get(1500).last
}
