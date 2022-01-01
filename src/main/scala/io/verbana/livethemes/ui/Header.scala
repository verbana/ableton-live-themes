package io.verbana.livethemes.ui

import io.verbana.livethemes.ui.DomNodeExtensions._
import org.scalajs.dom
import org.scalajs.dom.{Blob, BlobPropertyBag, HTMLDivElement, URL}

import scala.scalajs.js

object Header {
  import io.verbana.livethemes.ui.Elements._

  def create(xmlDoc: Observable[String]): HTMLDivElement = {
    val container = div(classes = Seq(
      "min-h-14", // 3.5rem // wtf is happenin to the height
      "bg-neutral-800",
    ))

    val contents = container.child(div(classes = Seq(
      "grid grid-cols-10 w-full px-8"
    )))

    val anchor = contents.child(
      a(url = "https://verbana.io", attrs = Map(
        "class" -> Seq(
          "bg-violet-900",
          "col-start-1 col-span-1 py-4",
          "hover:bg-violet-700 ease-out duration-200"
        ).mkString(" ")
      ))
    )
    anchor.child(
      p(text = "verbana.io", classes = Seq(
        "text-neutral-200 text-base font-medium text-center",
        "hover:text-neutral-100 ease-out duration-200"
      ))
    )

    val title = contents.child(
      p(
        text = "Ableton Live Theme Editor",
        classes = Seq(
          "text-neutral-300 text-base font-medium",
          "col-start-2 col-span-3 py-4 pl-6",
      ))
    )

    // contents.child(Tooltip.questionSvg(Seq(
    //   "col-start-5 col-span-1",
    //   "py-4 pl-6",
    //   "w-6 stroke-neutral-100 fill-neutral-100",
    //   "hover:stroke-neutral-300 hover:fill-neutral-300 hover:cursor-help",
    // )))

    val name = contents.child(p(text = "Theme Name:", classes = Seq(
      "text-neutral-300 text-base font-normal text-right",
      "col-start-6 col-span-2 py-4",
    )))

    val nameInput = contents.child(
      input(
        id = "name",
        tpe = "text",
        default = None,
        attrs = Map(
          "class" -> Seq(
            "col-start-8 col-span-2 my-4 mx-4",
            "bg-neutral-800",
            "border-b border-solid border-neutral-100",
            "text-neutral-400 text-base font-normal",
            "focus:outline-none"
          ).mkString(" ")
        )
      )
    )


    val dlAnchor = contents.child(
      a("", Map(
        "class" -> "text-neutral-200 col-start-10 text-center",
      ))
    )
    val downloadButton = dlAnchor.child(
      input(id = "download", tpe = "button", default = Some("Download"),
        Map("class" -> Seq(
          "rounded bg-violet-800",
          "my-2 px-4 py-2",
          "hover:text-neutral-100 hover:cursor-pointer hover:bg-violet-700 ease-out duration-200"
        ).mkString(" "))
      )
    )
    downloadButton.addEventListener("click", { (e: dom.Event) =>
      val filename = if (nameInput.value.isEmpty) { "Verbana.ask" } else { s"${nameInput.value}.ask" }
      dlAnchor.setAttribute("download", filename)
      val url = URL.createObjectURL(new Blob(js.Array(xmlDoc.get), new BlobPropertyBag {
        `type` = "text/plain"
      }))
      dlAnchor.href = url
    })

    container
  }
}
