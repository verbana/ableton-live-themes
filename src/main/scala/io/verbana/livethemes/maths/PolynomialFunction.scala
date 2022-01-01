package io.verbana.livethemes.maths


/**
 * Represents polynomial function with the given coefficients
 * @param coefficients are ordered by degree (i.e `a = coefficients(n)` is for `a * x^n``
 */
class PolynomialFunction(coefficients: Seq[Double]) {
  /**
   * Uses Horner's Method to evaluate the polynomial with the given coefficients at the argument
   * @param argument
   * @return
   */
  def evaluate(argument: Double): Double = {
    coefficients.init.reverse.foldLeft(coefficients.last)({ case(result, coefficient) =>
      (argument * result) + coefficient
    })
  }
}

object PolynomialFunction {
  def apply(coefficients: Double*): PolynomialFunction = {
    new PolynomialFunction(coefficients)
  }
}
