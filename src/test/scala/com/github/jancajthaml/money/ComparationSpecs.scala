package com.github.jancajthaml.money

import org.scalatest.{FlatSpec, Matchers}

class ComparationSpecs extends FlatSpec with Matchers {

  val run = true

  if (run) "Trivia" should "parse 1" in {
    val left = Money("40", "EUR")
    val right = Money("39", "EUR")
    assert(left >= right)
    assert(left > right)
    assert(right <= left)
    assert(right < left)
  }
}
