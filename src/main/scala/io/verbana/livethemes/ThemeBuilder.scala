package io.verbana.livethemes

import io.verbana.livethemes.Gradient.Midpoint
import io.verbana.livethemes.ThemeBuilder.ColorClass
import io.verbana.livethemes.themes.Theme

import scala.xml.{Elem, Node, UnprefixedAttribute}

class ThemeBuilder {
  private val printer = new scala.xml.PrettyPrinter(1024, 2)

  def buildNodes(state: ThemeBuilderState): Seq[Node] = {
    assert(state.base.coreUINodes.size == state.gradient.size)

    // Build new gradient for core UI
    val gradientNodes = state.base.coreUINodes.zip(state.gradient).flatMap({ case ((beforeColor, elems), nextColor) =>
      val elemsOfColor = elems.map(elem => {
        val nextAttr = new UnprefixedAttribute("Value", nextColor.hex, scala.xml.Null)
        elem.copy(
          attributes = elem.attributes.append(nextAttr)
        )
      })
      xml.Comment(s" Core UI (${nextColor.hex}) ") ++ elemsOfColor
    })

    // Override accent colors
    val accentElems = state.accents.flatMap({ case (cls, rgb) =>
      val elemsOfColor = cls.labels.flatMap(label => {
        // If label under accent class was actually grey in the base theme....
        // Should get transformed by the coreUI gradient so just ignore
        state.base.accentNodes.get(label.name).map(elem => {
          val nextColor = label.transform(rgb.toHSB)
          val nextAttr = new UnprefixedAttribute("Value", nextColor.toRGB.hex, scala.xml.Null)
          elem.copy(
            attributes = elem.attributes.append(nextAttr)
          )
        })
      })

      xml.Comment(s" Accent: ${cls.name} (${rgb.hex}) ") ++ elemsOfColor
    })

    val changedLabels = (gradientNodes ++ accentElems).map(elem => elem.label -> elem).toMap
    val unchangedNodes = state.base.elements.filterNot(elem => {
      changedLabels.contains(elem.label)
    })
    gradientNodes ++ accentElems ++ xml.Comment(" Unchanged elements below ") ++ unchangedNodes
  }

  def buildXML(nodes: Seq[Node]): Node = {
    val result: Elem = {
      <Ableton MajorVersion="5" MinorVersion="11.0_432" SchemaChangeCount="3" Creator="Ableton Live 11.0.1d1" Revision="">
        <Theme>
          {nodes}
          {themes.Operator.nodes}
          {themes.Sampler.nodes}
          {themes.Clips.nodes}
          {themes.VuMeters.All}
        </Theme>
      </Ableton>
    }
    result
  }

  def buildXMLDoc(node: Node): String = {
    val writer = new java.io.StringWriter()
    writer.append("""<?xml version="1.0" encoding="UTF-8"?>""")
    writer.append('\n')
    writer.append(printer.format(node))
    writer.toString
  }
}

case class ThemeBuilderState(name: String,
                             base: Theme,
                             gradientFrom: RGB,
                             gradientTo: RGB,
                             gradientMidpoint: Midpoint,
                             accents: Map[ColorClass, RGB]) {
  lazy val gradient: Seq[RGB] = Gradient.create(
    from = gradientFrom,
    to = gradientTo,
    buckets = base.coreUINodes.size,
    midpoint = gradientMidpoint
  )
}

object ThemeBuilder {
  object Classes {
    val PrimaryAccent = ColorClass(
      name = "Primary",
      labels = Seq(
        Label("ViewCheckControlEnabledOn"),
        Label("ChosenDefault"),
        Label("Progress"),
        Label("TransportProgress"),
        Label("RetroDisplayForeground"),
        Label("RetroDisplayGreen"),
        Label("RetroDisplayHandle1"),
        Label("GainReductionLineColor")
      ),
      docs = "Primary accent color (e.g. device buttons)"
    )
    val SecondaryAccent = ColorClass(
      name = "Secondary",
      labels = Seq(
        Label("RangeDefault"),
        Label("BipolReset"),
        Label("ChosenAlternative"),
        Label("RetroDisplayForeground2"),
        Label("RetroDisplayRed"),
        Label("ThresholdLineColor"),
        Label("Modulation")
      ),
      docs = "Secondary accent color (e.g. device knobs)",
    )
    val Fader = ColorClass(
      name = "Fader",
      labels = Seq(
        Label("RangeEditField")
      ),
      docs = "Color for faders",
    )
    val Cue = ColorClass(
      name = "Cue",
      labels = Seq(
        Label("ChosenPreListen"),
        Label("SpectrumAlternativeColor"),
      ),
      docs = "Color for cue fader & audition buttons",
    )
    val Record = ColorClass(
      name = "Record",
      labels = Seq(
        Label("ChosenRecord"),
      ),
      docs = "Color for record & arm buttons",
    )
    val Play = ColorClass(
      name = "Play",
      labels = Seq(
        Label("ChosenPlay"),
        Label("LearnMacro"),
        Label("AbletonColor"),
      ),
      docs = "Color for play button"
    )
    val Alert = ColorClass(
      name = "Alert",
      labels = Seq(
        Label("ChosenAlert"),
        Label("Alert"),
        Label("LearnKey"),
      ),
      docs = "Color for alerts at bottom of UI"
    )
    val Automation = ColorClass(
      name = "Automation",
      labels = Seq(
        Label("AutomationColor"),
        Label("VelocityColor"),
        Label("MidiNoteMaxVelocity"),
        Label("RetroDisplayHandle2"),
        Label("RangeEditField2"),
      ),
      docs = "Color for automation curves"
    )
    val AutomationHoverFreeze = ColorClass(
      name = "Frozen Tracks",
      labels = Seq(
        Label("FreezeColor"),
        Label("VelocitySelectedOrHovered"),
        Label("LearnMidi"),
        Label("AutomationMouseOver"),
        Label("ModulationMouseOver"),
      ),
      docs = "Color for frozen tracks & automation curves on mouseover",
    )
    val Selection = ColorClass(
      name = "Selection",
      labels = Seq(
        Label("SelectionBackground"),
        Label("StandbySelectionBackground",
          hsb => HSB(hsb.h, hsb.s + 5.hsb, hsb.b - 38.hsb).clipped()),
        Label("SelectionBackgroundContrast",
          hsb => HSB(hsb.h, hsb.s - 7.hsb, hsb.b - 16.hsb).clipped()),
        Label("LinkedTrackHover",
        hsb => HSB(hsb.h, hsb.s - 13.hsb, hsb.b + 5.hsb).clipped()),
      ),
      docs = "Color for selected devices & browser items"
    )
    val Search = ColorClass(
      name = "Search",
      labels = Seq(
        Label("SearchIndication"),
        Label("SearchIndicationStandby",
          hsb => HSB(hsb.h, hsb.s + 19.hsb, hsb.b - 22.hsb).clipped()),
      ),
      docs = "Color for searched browser items"
    )

    val Values = Seq(
      PrimaryAccent,
      SecondaryAccent,
      Fader,
      Cue,
      Record,
      Play,
      Alert,
      Automation,
      AutomationHoverFreeze,
      Selection,
      Search
    )
  }

  case class ColorClass(name: String, labels: Seq[Label], docs: String = "")

  case class Label(name: String, transform: HSB => HSB = identity)

  implicit class ColorFloatConversions(x: Double) {
    // Transforms a 0 - 255 HSB value to the corresponding float
    def hsb(): Double = {
      assert(x >= 0)
      assert(x <= 255)
      x / 255
    }
  }
}
