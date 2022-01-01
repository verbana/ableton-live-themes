/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.verbana.livethemes.maths

/**
 * Computes a natural (also known as "free", "unclamped") cubic spline interpolation for the data set.
 * <p>
 * The {@link # interpolate ( double [ ], double[])} method returns a {@link PolynomialSplineFunction}
 * consisting of n cubic polynomials, defined over the subintervals determined by the x values,
 * {@code x[0] < x[i] ... < x[n].}  The x values are referred to as "knot points."</p>
 * <p>
 * The value of the PolynomialSplineFunction at a point x that is greater than or equal to the smallest
 * knot point and strictly less than the largest knot point is computed by finding the subinterval to which
 * x belongs and computing the value of the corresponding polynomial at <code>x - x[i] </code> where
 * <code>i</code> is the index of the subinterval.  See {@link PolynomialSplineFunction} for more details.
 * </p>
 * <p>
 * The interpolating polynomials satisfy: <ol>
 * <li>The value of the PolynomialSplineFunction at each of the input x values equals the
 * corresponding y value.</li>
 * <li>Adjacent polynomials are equal through two derivatives at the knot points (i.e., adjacent polynomials
 * "match up" at the knot points, as do their first and second derivatives).</li>
 * </ol>
 * <p>
 * The cubic spline interpolation algorithm implemented is as described in R.L. Burden, J.D. Faires,
 * <u>Numerical Analysis</u>, 4th Ed., 1989, PWS-Kent, ISBN 0-53491-585-X, pp 126-131.
 * </p>
 *
 */
object SplineInterpolator {

  /**
   * Computes an interpolating function for the data set.
   *
   * @param x the arguments for the interpolation points
   * @param y the values for the interpolation points
   * @return a function which interpolates the data set
   */
  def interpolate(x: Array[Double], y: Array[Double]): PolynomialSplineFunction = {
    assert(x.length == y.length)
    assert(x.length >= 3)
    assert(x.sameElements(x.sorted))

    // Number of intervals.  The number of data points is n + 1.
    val n = x.length - 1

    // Differences between knot points
    val h = new Array[Double](n)
    for (i <- 0 until n) {
      h(i) = x(i + 1) - x(i)
    }
    val mu = new Array[Double](n)
    val z = new Array[Double](n + 1)
    var g = 0.0
    var indexM1 = 0
    var index = 1
    var indexP1 = 2
    while ( {
      index < n
    }) {
      val xIp1 = x(indexP1)
      val xIm1 = x(indexM1)
      val hIm1 = h(indexM1)
      val hI = h(index)
      g =  2d * (xIp1 - xIm1) - hIm1 * mu(indexM1)
      mu(index) = hI / g
      z(index) = (3d * (y(indexP1) * hIm1 - y(index) * (xIp1 - xIm1) + y(indexM1) * hI) / (hIm1 * hI) - hIm1 * z(indexM1)) / g
      indexM1 = index
      index = indexP1
      indexP1 = indexP1 + 1
    }
    // cubic spline coefficients --  b is linear, c quadratic, d is cubic (original y's are constants)
    val b = new Array[Double](n)
    val c = new Array[Double](n + 1)
    val d = new Array[Double](n)
    for (j <- n - 1 to 0 by -1) {
      val cJp1 = c(j + 1)
      val cJ = z(j) - mu(j) * cJp1
      val hJ = h(j)
      b(j) = (y(j + 1) - y(j)) / hJ - hJ * (cJp1 + 2d * cJ) / 3d
      c(j) = cJ
      d(j) = (cJp1 - cJ) / (3d * hJ)
    }
    val polynomials = new Array[PolynomialFunction](n)
    val coefficients = new Array[Double](4)
    for (i <- 0 until n) {
      coefficients(0) = y(i)
      coefficients(1) = b(i)
      coefficients(2) = c(i)
      coefficients(3) = d(i)
      polynomials(i) = new PolynomialFunction(coefficients)
    }
    new PolynomialSplineFunction(x, polynomials)
  }
}
