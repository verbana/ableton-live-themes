package io.verbana.livethemes.ui

import DomNodeExtensions._
import io.verbana.livethemes.RGB
import org.scalajs.dom
import org.scalajs.dom.{HTMLDivElement, HTMLLabelElement, document}

// color dropper + text input widget
class ColorInput(default: RGB, updateState: String => Unit) {
  import io.verbana.livethemes.ui.Elements._

  def root: HTMLDivElement = pickerWrapper

  private val pickerWrapper: HTMLDivElement = div(classes = Seq(
    "grid grid-cols-12",
    "min-h-11 mt-1 w-full xl:w-4/5",
    "outline outline-1 outline-neutral-300 rounded",
  ))

  private val inputWrapper = pickerWrapper.child(
    div(classes = Seq(
      "border-neutral-50 max-h-6 col-span-3",
      "hover:cursor-pointer",
    ))
  )
  inputWrapper.style.background = default.hex

  private val inputElem = inputWrapper.child({
    val elem = input(
      id = "TODO",
      tpe = "color",
      default = Some(default.hex),
    )
    elem.style.opacity = "0"
    elem.style.border = "none"
    elem.style.height = "0"
    elem.style.width = "0"
    elem
  })

  private val label = pickerWrapper.child({
    val elem = document.createElement("label").asInstanceOf[HTMLLabelElement]
    Seq("col-span-2", "bg-neutral-200", "font-mono", "text-neutral-500", "text-center").foreach(elem.classList.add)
    elem.textContent = "#"
    elem
  })

  private val textInput = pickerWrapper.child({
    input(
      id = inputElem.id + "-text",
      tpe = "text",
      default = Some(default.hex.stripPrefix("#")),
      attrs = Map(
        "class" -> Seq(
          "col-span-7 pl-1",
          "bg-neutral-100",
          "font-mono text-neutral-900",
          "focus:outline-none invalid:bg-rose-100"
        ).mkString(" "),
        "pattern" -> "[0-9A-Fa-f]{6}",
      )
    )
  })

  private def onChange(hex: String): Unit = {
    inputWrapper.style.background = hex
    textInput.value = hex.stripPrefix("#").toUpperCase
    inputElem.value = hex
    updateState(hex)
  }

  inputWrapper.addEventListener("click", { (_: dom.Event) => inputElem.click() })
  textInput.addEventListener("input", { (_: dom.Event) => {
    onChange(s"#${textInput.value}")
  }})
  inputElem.addInputListener(element => {
    onChange(element.value)
  })
}
