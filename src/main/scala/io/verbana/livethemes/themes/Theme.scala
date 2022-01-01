package io.verbana.livethemes.themes

import io.verbana.livethemes.RGB

import scala.util.matching.Regex
import scala.xml.{Elem, Node}

trait Theme {
  def name: String
  def elements: Seq[Node]

  lazy val (colorIndex, sourceOrdering) = {
    val index = scala.collection.mutable.Map.empty[RGB, Seq[Elem]]
    val ordering = scala.collection.mutable.Map.empty[String, Int]

    elements.zipWithIndex.foreach({
      case (node: Elem, i) =>
        node.attribute("Value").foreach({
          case Seq(text: scala.xml.Text) if Theme.colorPattern.matches(text.text) =>
            val color = RGB.fromHexString(text.text)
            val nodesOfColor = index.getOrElse(color, Seq.empty)
            index.put(color, nodesOfColor ++ Seq(node))
            ordering.put(node.label, i)

          case _ =>
          // Do Nothing
        })
      case _ =>
      // Not a regular XML element- do Nothing
    })

    (index.toMap, ordering.toMap)
  }

  // Sorted from darkest to lightest
  lazy val coreUINodes: Seq[(RGB, Seq[Elem])] = {
    colorIndex
      .filter({ case (rgb, _) => rgb.isNeutral }).toSeq
      .sortBy({ case (rgb, _) => rgb.toHSB.b })
  }

  lazy val accentNodes: Map[String, Elem] = {
    colorIndex
      .filter({ case (rgb, _) => !rgb.isNeutral })
      .values.flatten
      .map(elem => elem.label -> elem)
      .toMap
  }
}

object Theme {
  val colorPattern: Regex = "#[0-9A-Fa-f]{6}".r
  val neutralPattern: Regex = "#([0-9A-Fa-f])([0-9A-Fa-f])((?:\\1\\2){2})\\b".r
}