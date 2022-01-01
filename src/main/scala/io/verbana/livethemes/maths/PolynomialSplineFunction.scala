package io.verbana.livethemes.maths

case class PolynomialSplineFunction(knots: Seq[Double], polynomials: Seq[PolynomialFunction]) {
  def evaluate(v: Double): Double = {
    if (v < knots.head || v > knots.last) {
      throw new UnsupportedOperationException(s"Value ${v} is out of range of knots: ${knots.mkString("(", ",", ")")}")
    }
    var i = java.util.Arrays.binarySearch(knots.toArray, v)
    if (i < 0) {
      i = -i - 2
    }
    // This will handle the case where v is the last knot value
    // There are only n-1 polynomials, so if v is the last knot
    // then we will use the last polynomial to calculate the value.
    if (i >= polynomials.length) {
      i -= 1
    }
    polynomials(i).evaluate(v - knots(i))
  }
}

case class KnotFunction(knot: Double, polynomial: PolynomialFunction)

object PolynomialSplineFunction {
  def apply(knotFunctions: Seq[KnotFunction]): PolynomialSplineFunction = {
    val knots = knotFunctions.map(_.knot)
    val polynomials = knotFunctions.map(_.polynomial)
    PolynomialSplineFunction(knots, polynomials)
  }
}
