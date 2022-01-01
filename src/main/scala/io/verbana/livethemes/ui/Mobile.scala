package io.verbana.livethemes.ui

import io.verbana.livethemes.ui.DomNodeExtensions.DomNodeMethods
import org.scalajs.dom.document

object Mobile {
  import io.verbana.livethemes.ui.Elements._

  def render(): Unit = {
    val root = document.body.child(
      div(classes = Seq(
        "h-screen w-screen",
        "bg-neutral-900",
        "font-mono text-neutral-300",
        "flex flex-col place-content-center"
      ))
    )

    root.child(p(
      text = "┐(￣ヘ￣;)┌",
      classes = Seq(
        "self-center mb-6",
        "text-center",
      )
    ))

    val msgContainer = root.child(div(
      classes = Seq(
        "self-center w-2/3",
        "text-left text-sm",
      )
    ))
    msgContainer.child(p(
      text = "Sorry! This editor wasn't designed to be used on mobile 🥲 Please come back on your computer",
    ))

    val link = msgContainer.child(p("", classes = Seq(
      "mt-6"
    )))
    link.innerHTML =
      """
        |Come visit <a class="text-violet-700 hover:text-violet-500" href="https://verbana.io">verbana.io</a> in the meantime
        |""".stripMargin
  }
}
