package io.verbana.livethemes

import io.verbana.livethemes.maths.SplineInterpolator

// TODO: Color gradient needes to wrap on hue ...?
object Gradient {
  // Position: between 0.0 & 1.0
  case class Midpoint(value: RGB, position: Double)

  def create(from: RGB, to: RGB, buckets: Int, midpoint: Midpoint): Seq[RGB] = {
    // Add extra knots past beginning & end to make HSB values less likely to clip
    val r = SplineInterpolator.interpolate(
      x = Array(-0.5, 0, midpoint.position, 1, 1.5),
      y = Array(-128, from.r, midpoint.value.r, to.r, 255 + 128)
    )
    val g = SplineInterpolator.interpolate(
      x = Array(-0.5, 0, midpoint.position, 1, 1.5),
      y = Array(-128, from.g, midpoint.value.g, to.g, 255 + 128)
    )
    val b = SplineInterpolator.interpolate(
      x = Array(-0.5, 0, midpoint.position, 1, 1.5),
      y = Array(-128, from.b, midpoint.value.b, to.b, 255 + 128)
    )

    (0 until buckets).map(i => {
      val x = i / (buckets - 1).toDouble
      val rgb = RGB(
        r.evaluate(x).toInt,
        g.evaluate(x).toInt,
        b.evaluate(x).toInt
      )
      val clipped = rgb.clipped()
      if (rgb != clipped) { System.err.println(s"Gradient interpolation clipped color: ${rgb}")}
      clipped
    })
  }
}
