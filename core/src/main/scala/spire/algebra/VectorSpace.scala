package spire.algebra

import spire.NoImplicit

import scala.{ specialized => spec }
import scala.collection.SeqLike
import scala.collection.generic.CanBuildFrom

trait VectorSpace[V, @spec(Int, Long, Float, Double) F] extends Module[V, F] {
  implicit def scalar: Field[F]

  def divr(v: V, f: F): V = timesl(scalar.reciprocal(f), v)
}

object VectorSpace extends VectorSpace2 {
  @inline final def apply[V, @spec(Int,Long,Float,Double) R](implicit V: VectorSpace[V, R]) = V
}

trait VectorSpace0 {
  implicit def seq[A, CC[A] <: SeqLike[A, CC[A]]](implicit field0: Field[A],
      cbf0: CanBuildFrom[CC[A], A, CC[A]],
      ev: NoImplicit[NormedVectorSpace[CC[A], A]]) = new SeqVectorSpace[A, CC[A]] {
    val scalar = field0
    val cbf = cbf0
  }

  implicit def MapVectorSpace[K, V](implicit V0: Field[V],
      ev: NoImplicit[NormedVectorSpace[Map[K, V], V]]) = new MapVectorSpace[K, V] {
    val scalar = Field[V]
  }
}

trait VectorSpace1 extends VectorSpace0 {
  implicit def ArrayVectorSpace[@spec(Int,Long,Float,Double) A](implicit
      ev: NoImplicit[NormedVectorSpace[Array[A], A]], classTag0: Manifest[A],
      scalar0: Field[A]): VectorSpace[Array[A], A] = new ArrayVectorSpace[A] {
    val scalar = scalar0
    val classTag = classTag0
  }
}

trait VectorSpace2 extends VectorSpace1 {
  implicit def NormedVectorSpaceIsVectorSpace[V, @spec(Int, Long, Float, Double) F](implicit
    vectorSpace: NormedVectorSpace[V, F]): VectorSpace[V, F] = vectorSpace
}

final class VectorSpaceOps[V, @spec(Int,Long,Float,Double) F](lhs: V)(implicit ev: VectorSpace[V, F]) {
  def :/ (rhs:F): V = ev.divr(lhs, rhs)
}
