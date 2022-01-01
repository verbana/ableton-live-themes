package io.verbana.livethemes.ui

import org.scalajs.dom
import org.scalajs.dom.{HTMLElement, HTMLParagraphElement, HTMLSpanElement, SVGElement, document}

object Tooltip {
  private val questionPath: String = {
    """
      |<path fill-rule="evenodd" d="M8 1.5a6.5 6.5 0 100 13 6.5 6.5 0 000-13zM0 8a8 8 0 1116 0A8 8 0 010 8zm9 3a1 1 0 11-2 0 1 1 0 012 0zM6.92 6.085c.081-.16.19-.299.34-.398.145-.097.371-.187.74-.187.28 0 .553.087.738.225A.613.613 0 019 6.25c0 .177-.04.264-.077.318a.956.956 0 01-.277.245c-.076.051-.158.1-.258.161l-.007.004a7.728 7.728 0 00-.313.195 2.416 2.416 0 00-.692.661.75.75 0 001.248.832.956.956 0 01.276-.245 6.3 6.3 0 01.26-.16l.006-.004c.093-.057.204-.123.313-.195.222-.149.487-.355.692-.662.214-.32.329-.702.329-1.15 0-.76-.36-1.348-.863-1.725A2.76 2.76 0 008 4c-.631 0-1.155.16-1.572.438-.413.276-.68.638-.849.977a.75.75 0 101.342.67z"></path>
      |
      |""".stripMargin
    """
      |<path d="M10.97 8.265a1.45 1.45 0 00-.487.57.75.75 0 01-1.341-.67c.2-.402.513-.826.997-1.148C10.627 6.69 11.244 6.5 12 6.5c.658 0 1.369.195 1.934.619a2.45 2.45 0 011.004 2.006c0 1.033-.513 1.72-1.027 2.215-.19.183-.399.358-.579.508l-.147.123a4.329 4.329 0 00-.435.409v1.37a.75.75 0 11-1.5 0v-1.473c0-.237.067-.504.247-.736.22-.28.486-.517.718-.714l.183-.153.001-.001c.172-.143.324-.27.47-.412.368-.355.569-.676.569-1.136a.953.953 0 00-.404-.806C12.766 8.118 12.384 8 12 8c-.494 0-.814.121-1.03.265zM13 17a1 1 0 11-2 0 1 1 0 012 0z"></path>
      |<path fill-rule="evenodd" d="M12 1C5.925 1 1 5.925 1 12s4.925 11 11 11 11-4.925 11-11S18.075 1 12 1zM2.5 12a9.5 9.5 0 1119 0 9.5 9.5 0 01-19 0z"></path>
      |""".stripMargin
  }

  def questionSvg(classes: Seq[String]): SVGElement = {
    val svg = document.createElementNS("http://www.w3.org/2000/svg", "svg").asInstanceOf[SVGElement]
    Map(
      "viewBox" -> s"0 0 24 24",
      "width" -> s"24",
      "height" -> s"24",
      "class" -> classes.mkString(" ") ,
    ).foreach({ case(k, v) => svg.setAttribute(k, v)})
    svg.innerHTML = questionPath
    svg
  }

  def apply(helpText: String, classes: Seq[String] = Seq.empty): HTMLSpanElement = {
    val body = document.createElement("p").asInstanceOf[HTMLParagraphElement]
    body.textContent = helpText
    body.setAttribute("class", Seq(
      "px-4 py-4 ml-4 mb-4 rounded",
      "fixed h-1/10 w-max-1/2 bottom-0 left-0",
      "bg-violet-700/20",
      "text-neutral-200"
    ).mkString(" "))
    body.style.display = "none"


    val icon = document.createElement("span").asInstanceOf[HTMLSpanElement]
    icon.insertAdjacentElement("beforeend", questionSvg(classes))
    icon.insertAdjacentElement("beforeend", body)

    icon.addEventListener("mouseenter", { (e: dom.Event) =>
      body.style.display = "inline"
    })
    icon.addEventListener("mouseleave", { (e: dom.Event) =>
      body.style.display = "none"
    })
    icon
  }

  def apply(parent: HTMLElement, helpText: String): HTMLParagraphElement = {
    val body = document.createElement("p").asInstanceOf[HTMLParagraphElement]
    body.textContent = helpText
    body.setAttribute("class", Seq(
      "px-4 py-4 ml-4 mb-4 rounded",
      "fixed min-h-1/10 max-w-1/2 bottom-0 left-0",
      "bg-violet-700/20",
      "text-neutral-200 text-base font-normal normal-case",
      "transition-opacity ease-out duration-300",
      "opacity-0 group-hover:opacity-100",
      "invisible group-hover:visible",
    ).mkString(" "))

    parent.appendChild(body)
    Seq(
      "group",
      "hover:cursor-help",
      "hover:underline",
      "hover:decoration-dotted",
      "hover:decoration-auto",
      "hover:underline-offset-2"
    ).foreach(parent.classList.add)
    parent.classList.add("group")
    body
  }
}