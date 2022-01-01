package io.verbana.livethemes.ui

import org.scalajs.dom
import org.scalajs.dom.Node

object DomNodeExtensions {
  implicit class DomNodeMethods[T <: Node](node: T) {
    def child[U <: Node](newChild: U): U = {
      node.appendChild(newChild)
      newChild
    }
  }

  implicit class HTMLInputElementMethods[T <: dom.HTMLInputElement](node: T) {
    // Adds a listener on the 'input' event for the given element
    def addInputListener(f: dom.HTMLInputElement => Unit): Unit = {
      node.addEventListener("input", { (e: dom.Event) =>
        e.target match {
          case element: dom.HTMLInputElement =>
            f(element)
          case _ =>
            println(s"Error: Event ${e} has target ${e.target}")
        }
      })
    }
  }
}
