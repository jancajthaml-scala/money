package com.github.jancajthaml.money

import org.scalatest.{FlatSpec, Matchers}

class MoneySpecs extends FlatSpec with Matchers {

  "dummy" should "pass" in {
    val k = new RealNumber("12.3")
    var m = Money(k, "CZK")

    /*
    (0 to 10000).foreach(x => {
      m += Money(k, "CZK")
    })*/

    println(s"money --> ${m}")

    1 should === (1)
  }
}