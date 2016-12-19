package com.github.jancajthaml.money

import org.scalatest.{FlatSpec, Matchers}

class ParsingSpecs extends FlatSpec with Matchers {

  val run = false

  if (run) "Trivia" should "parse 1" in {
    val left = Real("1")
    assert(left.decimal == 1)
    assert(left.signum == false)
    assert(left.value == "1.0")
  }

  if (run) it should "parse 2" in {
    val left = Real("2")
    assert(left.decimal == 1)
    assert(left.signum == false)
    assert(left.value == "2.0")
  }

  if (run) it should s"parse ${Long.MaxValue}${Long.MaxValue}" in {
    val left = Real(s"${Long.MaxValue}${Long.MaxValue}")
    assert(left.decimal == s"${Long.MaxValue}${Long.MaxValue}".size)
    assert(left.signum == false)
    assert(left.value == s"${Long.MaxValue}${Long.MaxValue}.0")
  }

  if (run) "Normalized" should "parse 1.0" in {
    val left = Real("1.0")
    assert(left.decimal == 1)
    assert(left.signum == false)
    assert(left.value == "1.0")
  }

  if (run) it should "parse 0.1" in {
    val left = Real("0.1")
    assert(left.decimal == 1)
    assert(left.signum == false)
    assert(left.value == "0.1")
  }

  if (run) it should "parse 0.01" in {
    val left = Real("0.01")
    assert(left.decimal == 1)
    assert(left.signum == false)
    assert(left.value == "0.01")
  }

  if (run) it should "parse 10" in {
    val left = Real("10")
    assert(left.decimal == 2)
    assert(left.signum == false)
    assert(left.value == "10.0")
  }

  if (run) it should "parse 10.0" in {
    val left = Real("10.0")
    assert(left.decimal == 2)
    assert(left.signum == false)
    assert(left.value == "10.0")
  }

  if (run) it should "parse 10.1" in {
    val left = Real("10.1")
    assert(left.decimal == 2)
    assert(left.signum == false)
    assert(left.value == "10.1")
  }

  if (run) it should "parse 10.01" in {
    val left = Real("10.01")
    assert(left.decimal == 2)
    assert(left.signum == false)
    assert(left.value == "10.01")
  }

  if (run) it should "parse 10000000.00000001" in {
    val left = Real("10000000.00000001")
    assert(left.decimal == 8)
    assert(left.signum == false)
    assert(left.value == "10000000.00000001")
  }

  if (run) it should "parse 0" in {
    val left = Real("0")
    assert(left.decimal == 1)
    assert(left.signum == false)
    assert(left.value == "0.0")
  }

  if (run) it should "parse -1.0" in {
    val left = Real("-1.0")
    assert(left.decimal == 1)
    assert(left.signum == true)
    assert(left.value == "-1.0")
  }

  if (run) it should "parse -0.1" in {
    val left = Real("-0.1")
    assert(left.decimal == 1)
    assert(left.signum == true)
    assert(left.value == "-0.1")
  }

  if (run) it should "parse -0.01" in {
    val left = Real("-0.01")
    assert(left.decimal == 1)
    assert(left.signum == true)
    assert(left.value == "-0.01")
  }

  if (run) it should "parse -10" in {
    val left = Real("-10")
    assert(left.decimal == 2)
    assert(left.signum == true)
    assert(left.value == "-10.0")
  }

  if (run) it should "parse -10.0" in {
    val left = Real("-10.0")
    assert(left.decimal == 2)
    assert(left.signum == true)
    assert(left.value == "-10.0")
  }

  if (run) it should "parse -10.1" in {
    val left = Real("-10.1")
    assert(left.decimal == 2)
    assert(left.signum == true)
    assert(left.value == "-10.1")
  }

  if (run) it should "parse -10.01" in {
    val left = Real("-10.01")
    assert(left.decimal == 2)
    assert(left.signum == true)
    assert(left.value == "-10.01")
  }

  if (run) it should "parse -10000000.00000001" in {
    val left = Real("-10000000.00000001")
    assert(left.decimal == 8)
    assert(left.signum == true)
    assert(left.value == "-10000000.00000001")
  }

  if (run) it should "parse -0" in {
    val left = Real("-0")
    assert(left.decimal == 1)
    assert(left.signum == true)
    assert(left.value == "-0.0")
  }

  if (run) "Malformed" should "parse 0.0" in {
    val left = Real("0.0")
    assert(left.decimal == 1)
    assert(left.signum == false)
    assert(left.value == "0.0")
  }

  if (run) it should "parse 01.10" in {
    val left = Real("01.10")
    assert(left.decimal == 2)
    assert(left.signum == false)
    assert(left.value == "01.10")
  }

  if (run) it should "parse 00000000.00000001" in {
    val left = Real("00000000.00000001")
    assert(left.decimal == 8)
    assert(left.signum == false)
    assert(left.value == "00000000.00000001")
  }

  if (run) it should "parse 10000000.00000000" in {
    val left = Real("10000000.00000000")
    assert(left.decimal == 8)
    assert(left.signum == false)
    assert(left.value == "10000000.00000000")
  }

  if (run) {
    val randomDecimalL = new java.math.BigDecimal(java.lang.Math.random()).divide(new java.math.BigDecimal(10000 + ".0"), java.math.BigDecimal.ROUND_UP).multiply(new java.math.BigDecimal("100000000000.0")).toBigInteger()
    val randomDecimalR = new java.math.BigDecimal(java.lang.Math.random()).divide(new java.math.BigDecimal(10000 + ".0"), java.math.BigDecimal.ROUND_UP).multiply(new java.math.BigDecimal("100000000000.0")).toBigInteger()
      
    val randomDecimal = s"${randomDecimalL}.${randomDecimalR}"

    "Random" should s"parse ${randomDecimal}" in {
      val left = Real(randomDecimal)
      assert(left.decimal == randomDecimal.indexOf('.'))
      assert(left.signum == false)
      assert(left.value == randomDecimal)
    }
  }
}
