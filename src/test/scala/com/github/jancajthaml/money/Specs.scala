package com.github.jancajthaml.money

import org.scalatest.{FlatSpec, Matchers}

class MoneySpecs extends FlatSpec with Matchers {

  "dummy" should "pass" in {
    val pivot = new PreciseNumber("0.0000001")
    
    var m = Money(new PreciseNumber("0"), "CZK")
    
    (1 to 10000000).foreach(x => {
      m += Money(pivot, "CZK")
    })

    //println(s"positive --> ${pos}")
    println(s"result --> ${m}")

    1 should === (1)
  }

  it should "perform addition         1 + 1 = 2 " in {
    var l = Money(new PreciseNumber("1"), "CZK")
    var r = Money(new PreciseNumber("1"), "CZK")
    (l + r) should === (Money(new PreciseNumber("2"), "CZK"))
  }

  it should "perform substraction     2 - 1 = 1" in {
    var l = Money(new PreciseNumber("2"), "CZK")
    var r = Money(new PreciseNumber("1"), "CZK")
    (l - r) should === (Money(new PreciseNumber("1"), "CZK"))
  }

  it should "perform multiplication   2 * 2 = 4" in {
    var l = Money(new PreciseNumber("2"), "CZK")
    var r = Money(new PreciseNumber("2"), "CZK")
    (l * r) should === (Money(new PreciseNumber("4"), "CZK"))
  }

  it should "perform division         4 / 2 = 2" in {
    var l = Money(new PreciseNumber("4"), "CZK")
    var r = Money(new PreciseNumber("2"), "CZK")
    (l / r) should === (Money(new PreciseNumber("2"), "CZK"))
  }
}