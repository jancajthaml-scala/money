package com.github.jancajthaml.money

import org.scalatest.{FlatSpec, Matchers}

class MoneySpecs extends FlatSpec with Matchers {

  /*
  "Real" should "parse number" in {
    val r = scala.util.Random

    (1 to 1000).foreach(x => {
      val value = r.nextInt(10000).toString + "" + r.nextInt(10000).toString + "" + Math.abs(r.nextGaussian()).toString
      val real = Real(value)
      
      //println(s">>> ${real} / ${value}")

      value should === (real.toString)
    })
  }
  */
  /*
  "Real" should "perform 100 + 123 = 223" in {
    val left = Real("100")
    val right = Real("123")
    val result = left + right
    result.toString should === ("223")
  }
  */
  /*
  it should "perform 100.5 + 123.5 = 224" in {
    val left = Real("100.5")
    val right = Real("123.5")
    val result = left + right
    result.toString should === ("224")
  }
*/
  "Real" should "perform 100 + 123 = 223" in {
    val left = Real("1" + ("0" * 100000) + "1")
    val right = Real("2" + ("0" * 100000) + "1")
    val result = left + right
    println(result.toString())
    result.toString should === ("3" + ("0" * 100000) + "2")
  }

  it should "perform 100 + 23 = 123" in {
    val left = Real("100")
    val right = Real("23")
    val result = left + right
    result.toString should === ("123")
  }

  it should "perform 1.975 + 0.025 = 2" in {
    val left = Real("1.975")
    val right = Real("0.025")
    val result = left + right
    result.toString should === ("2")
  }

  it should "perform 3 - 2 = 1" in {
    val left = Real("3")
    val right = Real("2")
    val result = left - right
    result.toString should === ("1")
  }
  it should "perform 2 - 0.025 = 1.975" in {
    val left = Real("2.000")
    val right = Real("0.025")
    val result = left - right
    result.toString should === ("1.975")
  }

  /*
  "dummy" should "produce same result" in {
    val pos_pivot = new PreciseNumber("0.1")
    val neg_pivot = new PreciseNumber("-0.1")

    var m = Money(new PreciseNumber("100"), "CZK")
    var n = Money(new PreciseNumber("100"), "CZK")

    (1 to 1000).foreach(x => {
      m -= Money(pos_pivot, "CZK")
      n += Money(neg_pivot, "CZK")
    })

    println(s"check subtraction   --> ${m}")
    println(s"check addition      --> ${n}")

    println(s"check equality      --> calculated:${n == m} referenced:${Money(new PreciseNumber("0"), "CZK") == n}")

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
  */
}