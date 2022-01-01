package io.verbana.livethemes

import org.scalajs.dom
import org.scalajs.dom.{document, window}

object Main {
  def main(args: Array[String]): Unit = {
    document.addEventListener("DOMContentLoaded", { (e: dom.Event) =>
      window.navigator.userAgent match {
        // https://developer.mozilla.org/en-US/docs/Web/HTTP/Browser_detection_using_the_user_agent#mobile_tablet_or_desktopc
        case agent if agent.contains("Mobi") =>
          ui.Mobile.render()
        case _ =>
          ui.Index.render()
      }
    })
  }
}

