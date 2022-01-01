package io.verbana.livethemes.ui

import io.verbana.livethemes.Gradient.Midpoint
import io.verbana.livethemes.RGB.StringConversions
import io.verbana.livethemes.ThemeBuilder.Classes.{Alert, Automation, AutomationHoverFreeze, Cue, Fader, Play, PrimaryAccent, Record, Search, SecondaryAccent, Selection}
import io.verbana.livethemes.{RGB, ThemeBuilder, ThemeBuilderState, themes}
import org.scalajs.dom
import org.scalajs.dom._

import scala.collection.immutable.ListMap

object Index {
  import DomNodeExtensions._
  import Elements._

  val printer = new scala.xml.PrettyPrinter(256, 2)
  val params = ThemeBuilderState(
    name = "cool",
    base = themes.Dark,
    gradientFrom =
      "#000000".rgb,
    gradientTo =
      "#FFFFFF".rgb,
    gradientMidpoint = Midpoint(
      value = "#505050".rgb,
      position = 0.5,
    ),
    accents = ListMap(
      PrimaryAccent ->
        "#F39420".rgb,
      SecondaryAccent ->
        "#33BFDB".rgb,
      Selection ->
        "#8CCAD8".rgb,
      Fader ->
        "#1a707f".rgb,
      Cue ->
        "#2B7ED8".rgb,
      Record ->
        "#E0332A".rgb,
      Play ->
        "#00FF81".rgb,
      Alert ->
        "#FF7F42".rgb,
      Automation ->
        "#E55544".rgb,
      AutomationHoverFreeze ->
        "#4391E6".rgb,
      Search ->
        "#EBB62D".rgb
    ),
  )

  def render(): Unit = {
    // Dark / light switch - to flip the font / background relationship
    // val (darkBuilder, lightBuilder) = (new ThemeBuilder(themes.Dark), new ThemeBuilder(themes.Light))
    val builder = new ThemeBuilder
    var state = params.copy()

    val xmlDoc = new Observable("")
    def updateXML(): Unit = {
      val newNodes = builder.buildNodes(state)
      val result = builder.buildXML(newNodes)
      xmlDoc.set(builder.buildXMLDoc(result))
    }

    //
    // Top-level container elements
    //
    document.body.classList.add("overscroll-none")
    val root = document.body.child(
      div(classes = Seq(
        "flex flex-col h-screen"
      ))
    )

    val header = root.child(Header.create(xmlDoc))
    val container = root.child(
      div(classes = Seq(
        "grid grid-cols-6",
        "bg-neutral-900 grid-bg",
      ))
    )
   container.style.height = "calc(100vh - 3.5rem - 1px)"

    //
    // Elements for output
    //
    val outputParent = container.child(
      div(classes = Seq(
        "col-span-5 p-24"
      ))
    )

    val helpIcon = outputParent.child(
      p(
        text = "HELP",
        classes = Seq(
          "fixed bottom-0 right-[16.6%]",
          "px-4 py-4 mr-4 mb-2 rounded",
          "text-neutral-400 text-sm",
          "group hover:text-neutral-300"
        )
      )
    )
    helpIcon.insertAdjacentElement("afterbegin", Tooltip.questionSvg(Seq(
      "inline w-4 pb-1 mr-1",
      "stroke-neutral-400 fill-neutral-400",
      "group-hover:stroke-neutral-300 group-hover:fill-neutral-300",
    )))
    val helpText = Tooltip(helpIcon, "")
    helpText.innerHTML =
      """
        |<div class="w-11/12 bg-neutral-800/90 px-4 py-4 ml-4 mb-4 rounded">
        |<p>Works best in Chrome. Mouse over colors on the left or names on the right to show what elements are affected</p>
        |<p class="mt-2">To install on MacOS:</p>
        |<ol class="list-decimal ml-6">
        |  <li>Go to <code class="bg-neutral-500/30 px-1">Applications</code></li>
        |  <li>Right click <code class="bg-neutral-500/30 px-1">Ableton Live 11 Suite</code> and select <code class="bg-neutral-500/30 px-1">Show Package Contents</code></li>
        |  <li>Place downloaded <code class="bg-neutral-500/30 px-1">.ask</code> file under <code class="bg-neutral-500/30 px-1">Contents/App-Resources/Themes</code></li>
        |</ol>
        |
        |<p class="mt-2">To install on Windows:</p>
        |<ol class="list-decimal ml-6">
        |  <li>Place downloaded <code class="bg-neutral-500/30 px-1">.ask</code> file under <code class="bg-neutral-500/30 px-1">C:\ProgramData\Ableton\Live 11 Suite\Resources\Themes</code></li>
        |</ol>
        |
        |<p class="mt-2">If overwriting an existing <code class="bg-neutral-500/30 px-1">.ask</code> file, switch to a different theme and then switch back for the changes to take effect</p>
        |<p class="mt-2">You can also manually edit the <code class="bg-neutral-500/30 px-1">.ask</code> file to tweak even more details <span class="inline-block align-top px-1">🧑‍🎨</span></p>
        |<p class="mt-2 text-right">Built by <a href="https://verbana.io" class="text-violet-500">@verbanamusic</a></p>
        |</div>
        |""".stripMargin
    Seq(
      "bg-transparent",
      "w-4/5",
    ).foreach(helpText.classList.add)

    // Set up element for gradient output
    val gradient = new GradientNodes(state)
    outputParent.appendChild(gradient.element)
    Tooltip(gradient.element, "Gradient that sets colors used for text & backgrounds")

    // Set up element for accent output
    val accentRow = outputParent.child(
      div(Seq("flex", "flex-row", "h-1/5"))
    )

    //
    // Elements for input
    //
    val formParent = container.child(
      div(classes = Seq(
        // "basis-1/5 p-4",
        "col-span-1", "pb-12", "pt-1",
        "border-l border-solid border-neutral-100",
        "bg-neutral-100",
        "h-full overflow-y-scroll",
      ))
    )

    val coreUITitle = formParent.child(
      p(
        text = "Core",
        classes = Seq(
          "text-2xl", "font-medium", "text-neutral-600", "uppercase",
          "pt-6", "px-3", "lg:px-10"
        )
      )
    )
    Tooltip(coreUITitle, "Gradient that sets colors used for text & backgrounds")

    // Widget for Mode
    {
      val container: Element = formParent.child(
        div(classes = Seq(
          "container", "py-3", "px-3", "lg:px-10"
        ))
      )
      val title = container.child(
        p(text = "Mode",
          classes = Seq("text-lg", "font-normal", "text-neutral-600")
        )
      )
      Tooltip(title, "Text is displayed with dark colors in light mode and with light colors in dark mode")

      val darkWidget = container.child(
        div(classes = Seq(
          "group select-none bg-neutral-700 rounded",
          "2xl:max-w-[80%] pl-2 mt-1 py-px"
        )),
      )
      val darkButton = darkWidget.child(input(
        id = "darkMode", tpe = "radio", default = None, attrs = Map(
          "name" -> "mode",
          "value" -> "dark",
          "class" -> "hover:cursor-pointer"
        )
      ))
      val darkModeLabel = darkWidget.child(label(
        text = "Dark Mode",
        classes = Seq(
          "pl-2 pr-5 w-full h-full ",
          "font-mono text-sm text-neutral-200",
          "hover:cursor-pointer"
        )
      ))
      darkModeLabel.htmlFor = "darkMode"

      val lightWidget = container.child(
        div(classes = Seq(
          "group select-none bg-neutral-200 rounded",
          "2xl:max-w-[80%] pl-2 mt-1"
        )),
      )
      val lightButton = lightWidget.child(input(
        id = "lightMode", tpe = "radio", default = None, attrs = Map(
          "name" -> "mode",
          "value" -> "light",
          "class" -> "hover:cursor-pointer"
        )
      ))
      val lightModeLabel = lightWidget.child(label(
        text = "Light Mode",
        classes = Seq(
          "pl-2 pr-5 w-full h-full",
          "font-mono text-sm text-neutral-700",
          "hover:cursor-pointer"
        )
      ))
      lightModeLabel.htmlFor = "lightMode"

      // Default is dark mode
      darkButton.checked = true
      darkButton.addEventListener("input", { (_: dom.Event) => {
        state = state.copy(base = themes.Dark)
        gradient.update(state)
        updateXML()
      }})
      lightButton.addEventListener("input", { (_: dom.Event) => {
        state = state.copy(base = themes.Light)
        gradient.update(state)
        updateXML()
      }})
    }

    // Widget for midpoint
    {
      val container: Element = formParent.child(
        div(classes = Seq(
          "container", "py-3", "px-3", "lg:px-10"
        ))
      )
      val title = container.child(
        p(text = "Midpoint",
          classes = Seq("text-lg", "font-normal", "text-neutral-600")
        )
      )
      Tooltip(title, "Midpoint color of gradient")
      val colorInput = new ColorInput(state.gradientMidpoint.value, (hex: String) => {
        state = state.copy(gradientMidpoint = state.gradientMidpoint.copy(value = hex.rgb))
        gradient.update(state)
        updateXML()
      })
      container.appendChild(colorInput.root)
    }

    // Widget for bias
    {
      val container: Element = formParent.child(
        div(classes = Seq(
          "container", "py-3", "px-3", "lg:px-10"
        ))
      )
      val title = container.child(
        p(text = "Midpoint Offset",
          classes = Seq("text-lg", "font-normal", "text-neutral-600")
        )
      )
      Tooltip(title, "Moves the midpoint color's position in the gradient")
      val positionInput = container.child(
        input("midpointPosition", "range", Some("0.5"),
          Map("min" -> "0.2", "max" -> "0.8", "step" -> "0.01", "defaultValue" -> "0.5",
            "class" -> "cursor-grab mr-2 max-w-full"
          ))
      )
      positionInput.addInputListener(element => {
        state = state.copy(gradientMidpoint = state.gradientMidpoint.copy(position = element.value.toFloat))
        gradient.update(state)
        updateXML()
      })
    }

    // Widget for black point
    {
      val container: Element = formParent.child(
        div(classes = Seq(
          "container", "py-3", "px-3", "lg:px-10"
        ))
      )
      val title = container.child(
        p(text = "Black Point",
          classes = Seq("text-lg", "font-normal", "text-neutral-600")
        )
      )
      Tooltip(title, "Darkest color used - basis of text color for themes in light mode")

      val colorInput = new ColorInput(state.gradientFrom, (hex: String) => {
        state = state.copy(gradientFrom = hex.rgb)
        gradient.update(state)
        updateXML()
      })
      container.appendChild(colorInput.root)
    }

    // Widget for white point
    {
      val container: Element = formParent.child(
        div(classes = Seq(
          "container", "py-3", "px-3", "lg:px-10"
        ))
      )
      val title = container.child(
        p(text = "White Point",
          classes = Seq("text-lg", "font-normal", "text-neutral-600")
        )
      )
      Tooltip(title, "Lightest color used - basis of text color for themes in dark mode")

      val colorInput = new ColorInput(state.gradientTo, (hex: String) => {
        state = state.copy(gradientTo = hex.rgb)
        gradient.update(state)
        updateXML()
      })
      container.appendChild(colorInput.root)
    }

    val accentsTitle = formParent.child(
      p(
        text = "Accents",
        classes = Seq(
          "text-2xl", "font-medium", "text-neutral-600", "uppercase",
          "pt-6", "px-3", "lg:px-10"
        )
      )
    )
    Tooltip(accentsTitle, "Accent colors used throughout UI")

    state.accents.foreach({ case (colorClass, color) =>
      // Container row for input widgets
      val container: Element = formParent.child(
        div(classes = Seq(
          "container", "py-3", "px-3", "lg:px-10"
        ))
      )

      // Set up output color display box
      val boxClasses = Seq("flex-auto", "flex-basis-1/12")
      val box = accentRow.child(
        div(boxClasses :+ s"bg-[${color.hex}]")
      )
      box.addEventListener("mouseenter", { (e: dom.Event) =>
        Seq("bg-neutral-200", "outline", "outline-1", "outline-neutral-300").foreach(container.classList.add)
      })
      box.addEventListener("mouseleave", { (e: dom.Event) =>
        Seq("bg-neutral-200", "outline", "outline-1", "outline-neutral-300").foreach(container.classList.remove)
      })

      // Set up input widgets
      val title = container.child(
        p(text = colorClass.name,
          classes = Seq("text-lg", "font-normal", "text-neutral-600")
        )
      )
      Tooltip(title, colorClass.docs)
      Tooltip(box, colorClass.docs)

      val colorInput = new ColorInput(color, (hex: String) => {
        state = state.copy(accents = state.accents.updated(colorClass, hex.rgb))
        box.setAttribute("class", (boxClasses :+ s"bg-[${hex}]").mkString(" "))
        updateXML()
      })
      container.appendChild(colorInput.root)
    })

    gradient.update(state)
    updateXML() // Initialize
  }
}


class GradientNodes(initial: ThemeBuilderState) {
  import Elements._

  val element: HTMLDivElement = div(Seq("flex", "flex-row", "h-4/5"))
  private var boxes = initial.gradient.map(rgb =>{
    div(classes(rgb, 1.0 / initial.gradient.size))
  })
  boxes.foreach(element.appendChild)

  private def classes(rgb: RGB, basis: Double): Seq[String] = {
    Seq("flex-auto", s"flex-basis-[${basis}%]", s"bg-[${rgb.hex}]")
  }

  def update(state: ThemeBuilderState): Unit = {
    if (state.gradient.size == boxes.size) { // Gradient colors changed
      boxes.zip(state.gradient).foreach({ case (node, rgb) =>
        node.setAttribute("class", classes(rgb, 1.0 / state.gradient.size).mkString(" "))
      })
    } else { // Gradient mode changed from dark -> light or vice versa
      boxes.foreach(element.removeChild)

      boxes = state.gradient.map(rgb => {
        div(classes = classes(rgb, 1.0 / state.gradient.size))
      })
      boxes.foreach(element.appendChild)
    }
  }
}


object Elements {
  def div(classes: Seq[String] = Seq.empty): HTMLDivElement = {
    val node = document.createElement(s"div").asInstanceOf[HTMLDivElement]
    if (classes.nonEmpty) { node.setAttribute("class", classes.mkString(" ")) }
    node
  }

  def h(text: String, level: Int, classes: Seq[String] = Seq.empty): Element = {
    val node = document.createElement(s"h${level}")
    node.textContent = text
    if (classes.nonEmpty) { node.setAttribute("class", classes.mkString(" ")) }
    node
  }

  def input(id: String, tpe: String, default: Option[String] = None, attrs: Map[String, String] = Map.empty): HTMLInputElement = {
    val node = document.createElement(s"input").asInstanceOf[HTMLInputElement]
    node.setAttribute("id", id)
    node.setAttribute("name", id)
    node.setAttribute("type", tpe)
    default.foreach(value => node.setAttribute("value", value))
    attrs.foreach({ case (k, v) => node.setAttribute(k, v)})
    node
  }

  def p(text: String, classes: Seq[String] = Seq.empty): HTMLParagraphElement = {
    val node = document.createElement("p").asInstanceOf[HTMLParagraphElement]
    node.textContent = text
    if (classes.nonEmpty) { node.setAttribute("class", classes.mkString(" ")) }
    node
  }

  def span(text: String, classes: Seq[String] = Seq.empty): HTMLSpanElement = {
    val node = document.createElement(s"span").asInstanceOf[HTMLSpanElement]
    node.textContent = text
    if (classes.nonEmpty) { node.setAttribute("class", classes.mkString(" ")) }
    node
  }

  def label(text: String, classes: Seq[String] = Seq.empty): HTMLLabelElement = {
    val node = document.createElement(s"label").asInstanceOf[HTMLLabelElement]
    node.textContent = text
    if (classes.nonEmpty) { node.setAttribute("class", classes.mkString(" ")) }
    node
  }

  def pre(text: String): Element = {
    val node = document.createElement("pre")
    node.textContent = text
    node
  }

  def a(url: String, attrs: Map[String, String] = Map.empty): HTMLAnchorElement = {
    val node = document.createElement("a").asInstanceOf[HTMLAnchorElement]
    node.href = url
    attrs.foreach({ case (k,v) => node.setAttribute(k, v)})
    node
  }
}
