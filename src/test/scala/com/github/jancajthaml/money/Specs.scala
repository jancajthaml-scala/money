package com.github.jancajthaml.money

import org.scalatest.{FlatSpec, Matchers}

class MoneySpecs extends FlatSpec with Matchers {

  "dummy" should "pass" in {
    val pivot = new PreciseNumber("0.01")
    //val divident = Money(new PreciseNumber("100"), "CZK")
    
    var m = Money(new PreciseNumber("0"), "CZK")
    var n = Money(new PreciseNumber("100"), "CZK")
    
    
    (1 to 100).foreach(x => {
      m += Money(pivot, "CZK")
    })

    (1 to 100).foreach(x => {
      n -= Money(pivot, "CZK")
      //n += Money(pivot, "CZK")
    })

    //val k = Money(pivot, "CZK") * Money(new PreciseNumber("1000000"), "CZK")

    println(s"100 times add 1       --> ${m}")
    println(s"100 times substract 1 --> ${n}")
    //println(s"1 time multiply by 1000000 --> ${k}")
    //println(s"1000000 divided by 1000000 --> ${divident / divident}")
    //println(s"1000000 divided by 1000000 --> ${divident / divident}")

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