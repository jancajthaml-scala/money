package com.github.jancajthaml.money

import org.scalatest.{FlatSpec, Matchers}

class MoneySpecs extends FlatSpec with Matchers {

  "dummy" should "pass" in {
    val pivot = new PreciseNumber("0.1")

    var m = Money(new PreciseNumber("100"), "CZK")

    (1 to 1000).foreach(x => {
      m -= Money(pivot, "CZK")
    })

    println(s"check       --> ${m}")

    1 should === (1)
  }

  "dummy" should "aliasing boundaries" in {
    var m = Money(new PreciseNumber("0"), "CZK")

    m += Money(new PreciseNumber("0." + ("0" * 200) + "1"), "CZK")
    m += Money(new PreciseNumber("1" + ("0" * 200)), "CZK")

    m *= Money(new PreciseNumber("2"), "CZK");

    println(s"check       --> ${m}")

    1 should === (1)
  }

  it should "perform addition         3 + 5 = 8 " in {
    var l = Money(new PreciseNumber("3"), "CZK")
    var r = Money(new PreciseNumber("5"), "CZK")
    (l + r) should === (Money(new PreciseNumber("8"), "CZK"))
  }

  it should "perform substraction     5 - 3 = 2" in {
    var l = Money(new PreciseNumber("5"), "CZK")
    var r = Money(new PreciseNumber("3"), "CZK")
    (l - r) should === (Money(new PreciseNumber("2"), "CZK"))
  }

  it should "perform multiplication   3 * 0.5 = 1.5" in {
    var l = Money(new PreciseNumber("3"), "CZK")
    var r = Money(new PreciseNumber("0.5"), "CZK")
    (l * r) should === (Money(new PreciseNumber("1.5"), "CZK"))
  }

  it should "perform division         6 / 2 = 3" in {
    var l = Money(new PreciseNumber("6"), "CZK")
    var r = Money(new PreciseNumber("2"), "CZK")
    (l / r) should === (Money(new PreciseNumber("3"), "CZK"))
  }
}