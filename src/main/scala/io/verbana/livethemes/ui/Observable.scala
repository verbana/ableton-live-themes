package io.verbana.livethemes.ui

class Observable[T](initial: T) {
  private var value: T = initial

  def get: T = value
  def set(next: T): T = {
    value = next
    next
  }
}

object Observable {
  def apply[T](value: T): Observable[T] = {
    new Observable[T](value)
  }
}