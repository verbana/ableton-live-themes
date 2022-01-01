package io.verbana.livethemes

case class RGB(r: Int, g: Int, b: Int) {
  def toHSB: HSB = Chroma.RGBtoHSB(r, g, b)
  def hex: String = f"#$r%02X$g%02X$b%02X"
  def isNeutral: Boolean = r == g && g == b
  def clipped(): RGB = {
    RGB(RGB.clip(r), RGB.clip(g), RGB.clip(b))
  }
}


object RGB {
  def fromHexString(hex: String): RGB = {
    val Seq(r, g, b) = hex.stripPrefix("#").grouped(2).toSeq
    RGB(
      Integer.parseInt(r, 16),
      Integer.parseInt(g, 16),
      Integer.parseInt(b, 16)
    )
  }

  implicit class StringConversions(string: String) {
    def rgb: RGB = fromHexString(string)
  }

  def clip(value: Int): Int = {
    math.max(0, math.min(255, value))
  }
}


case class HSB(h: Double, s: Double, b: Double) {
  def toRGB: RGB = Chroma.HSBtoRGB(h.toFloat, s.toFloat, b.toFloat)
  def clipped(): HSB = {
    HSB(HSB.clip(h), HSB.clip(s), HSB.clip(b))
  }
}

object HSB {
  def clip(value: Double): Double = {
    math.max(0, math.min(1.0f, value))
  }
}

object Chroma {
  /**
   * Converts the components of a color, as specified by the HSB
   * model, to an equivalent set of values for the default RGB model.
   * <p>
   * The {@code saturation} and {@code brightness} components
   * should be floating-point values between zero and one
   * (numbers in the range 0.0-1.0).  The {@code hue} component
   * can be any floating-point number.  The floor of this number is
   * subtracted from it to create a fraction between 0 and 1.  This
   * fractional number is then multiplied by 360 to produce the hue
   * angle in the HSB color model.
   * <p>
   * The integer that is returned by {@code HSBtoRGB} encodes the
   * value of a color in bits 0-23 of an integer value that is the same
   * format used by the method {@link # getRGB ( ) getRGB}.
   * This integer can be supplied as an argument to the
   * {@code Color} constructor that takes a single integer argument.
   *
   * @param hue        the hue component of the color
   * @param saturation the saturation of the color
   * @param brightness the brightness of the color
   * @return the RGB value of the color with the indicated hue,
   *         saturation, and brightness.
   * @see java.awt.Color#getRGB()
   * @see java.awt.Color#Color(int)
   * @see java.awt.image.ColorModel#getRGBdefault()
   * @since 1.0
   */
  def HSBtoRGB(hue: Float, saturation: Float, brightness: Float): RGB = {
    var r: Int = 0
    var g: Int = 0
    var b: Int = 0
    if (saturation == 0) {
      r = (brightness * 255.0f + 0.5f).toInt
      g = (brightness * 255.0f + 0.5f).toInt
      b = (brightness * 255.0f + 0.5f).toInt
    } else {
      val h: Float = (hue - Math.floor(hue).toFloat) * 6.0f
      val f: Float = h - java.lang.Math.floor(h).toFloat
      val p: Float = brightness * (1.0f - saturation)
      val q: Float = brightness * (1.0f - saturation * f)
      val t: Float = brightness * (1.0f - (saturation * (1.0f - f)))
      h.toInt match {
        case 0 =>
          r = (brightness * 255.0f + 0.5f).toInt
          g = (t * 255.0f + 0.5f).toInt
          b = (p * 255.0f + 0.5f).toInt

        case 1 =>
          r = (q * 255.0f + 0.5f).toInt
          g = (brightness * 255.0f + 0.5f).toInt
          b = (p * 255.0f + 0.5f).toInt

        case 2 =>
          r = (p * 255.0f + 0.5f).toInt
          g = (brightness * 255.0f + 0.5f).toInt
          b = (t * 255.0f + 0.5f).toInt

        case 3 =>
          r = (p * 255.0f + 0.5f).toInt
          g = (q * 255.0f + 0.5f).toInt
          b = (brightness * 255.0f + 0.5f).toInt

        case 4 =>
          r = (t * 255.0f + 0.5f).toInt
          g = (p * 255.0f + 0.5f).toInt
          b = (brightness * 255.0f + 0.5f).toInt

        case 5 =>
          r = (brightness * 255.0f + 0.5f).toInt
          g = (p * 255.0f + 0.5f).toInt
          b = (q * 255.0f + 0.5f).toInt

      }
    }
    // 0xff000000 | (r << 16) | (g << 8) | (b << 0)
    RGB(r, g, b)
  }

  /**
   * Converts the components of a color, as specified by the default RGB
   * model, to an equivalent set of values for hue, saturation, and
   * brightness that are the three components of the HSB model.
   * <p>
   * If the {@code hsbvals} argument is {@code null}, then a
   * new array is allocated to return the result. Otherwise, the method
   * returns the array {@code hsbvals}, with the values put into
   * that array.
   *
   * @param r       the red component of the color
   * @param g       the green component of the color
   * @param b       the blue component of the color
   * @param hsbvals the array used to return the
   *                three HSB values, or {@code null}
   * @return an array of three elements containing the hue, saturation,
   *         and brightness (in that order), of the color with
   *         the indicated red, green, and blue components.
   * @see java.awt.Color#getRGB()
   * @see java.awt.Color#Color(int)
   * @see java.awt.image.ColorModel#getRGBdefault()
   * @since 1.0
   */
  def RGBtoHSB(r: Int, g: Int, b: Int): HSB = {
    var hue: Float = 0.0f
    var saturation: Float = 0.0f
    var brightness: Float = 0.0f

    var cmax: Int = if (r > g) { r } else { g }
    if (b > cmax) {
      cmax = b
    }

    var cmin: Int = if (r < g) { r }  else { g }
    if (b < cmin) {
      cmin = b
    }

    brightness = (cmax.toFloat) / 255.0f
    if (cmax != 0) {
      saturation = ((cmax - cmin).toFloat) / (cmax.toFloat)
    } else {
      saturation = 0
    }
    if (saturation == 0) {
      hue = 0
    } else {
      val redc: Float = ((cmax - r).toFloat) / ((cmax - cmin).toFloat)
      val greenc: Float = ((cmax - g).toFloat) / ((cmax - cmin).toFloat)
      val bluec: Float = ((cmax - b).toFloat) / ((cmax - cmin).toFloat)
      if (r == cmax) {
        hue = bluec - greenc
      }
      else {
        if (g == cmax) {
          hue = 2.0f + redc - bluec
        }
        else {
          hue = 4.0f + greenc - redc
        }
      }
      hue = hue / 6.0f
      if (hue < 0) {
        hue = hue + 1.0f
      }
    }

    HSB(hue, saturation, brightness)
  }
}
