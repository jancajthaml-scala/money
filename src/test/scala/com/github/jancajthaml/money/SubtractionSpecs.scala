package com.github.jancajthaml.money

import org.scalatest.{FlatSpec, Matchers}

class SubtractionSpecs extends FlatSpec with Matchers {

  val run = true

  if (run) "Trivia" should "subtract 3 - 2 = 1.0" in {
    val left = Money("3", "EUR")
    val right = Money("2", "EUR")
    val result = left - right
    //assert(result.decimal == 1)
    //assert(result.signum == false)
    assert(result.value == "1")
  }

  if (run) it should "subtract 2 - 3 = -1.0" in {
    val left = Money("2", "EUR")
    val right = Money("3", "EUR")
    val result = left - right
    //assert(result.decimal == 1)
    //assert(result.signum == true)
    assert(result.value == "-1")
  }

  if (run) it should "subtract 100 - 1 = 99.0" in {
    val left = Money("100", "EUR")
    val right = Money("1", "EUR")
    val result = left - right
    //assert(result.decimal == 3)
    //assert(result.signum == false)
    assert(result.value == "99")
  }

  if (run) it should "subtract 1 - 0.00001 = 0.99999" in {
    val left = Money("1", "EUR")
    val right = Money("0.00001", "EUR")
    val result = left - right
    //assert(result.decimal == 1)
    //assert(result.signum == false)
    assert(result.value == "0.99999")
  }

  if (run) it should "subtract 0.00501 - 1 = -0.99499" in {
    val left = Money("0.00501", "EUR")
    val right = Money("1", "EUR")
    val result = left - right
    //assert(result.decimal == 1)
    //assert(result.signum == true)
    assert(result.value == "-0.99499")
  }

  if (run) it should "subtract 0 - 10001 = -10001.0" in {
    val left = Money("0", "EUR")
    val right = Money("10001", "EUR")
    val result = left - right
    //assert(result.decimal == 5)
    //assert(result.signum == true)
    assert(result.value == "-10001")
  }

  if (run) it should "subtract 10001 - 0 = 10001.0" in {
    val left = Money("10001", "EUR")
    val right = Money("0", "EUR")
    val result = left - right
    //assert(result.decimal == 5)
    //assert(result.signum == false)
    assert(result.value == "10001")
  }

  if (run) it should "subtract -1 - -1 = 0.0" in {
    val left = Money("-1", "EUR")
    val right = Money("-1", "EUR")
    val result = left - right
    //assert(result.decimal == 1)
    //assert(result.signum == false)
    assert(result.value == "0")
  }

  /*

  if (run) it should "add -0 + -10001 = -10001.0" in {
    val left = Money("-0")
    val right = Money("-10001")
    val result = left + right
    assert(result.exponent == 5)
    //assert(result.signum == true)
    assert(result.digits.deep == Array('1', '0', '0', '0', '1', '0').deep)
  }

  if (run) it should "add 1 + 9 = 10.0" in {
    val left = Money("1")
    val right = Money("9")
    val result = left + right
    assert(result.exponent == 2)
    //assert(result.signum == false)
    assert(result.digits.deep == Array('1', '0', '0').deep)
  }

  if (run) it should "add 9 + 9 = 18.0" in {
    val left = Money("9")
    val right = Money("9")
    val result = left + right
    assert(result.exponent == 2)
    //assert(result.signum == false)
    assert(result.digits.deep == Array('1', '8', '0').deep)
  }

  if (run) "Normalized" should "add 1.0 + 0.1 = 1.1" in {
    val left = Money("1.0")
    val right = Money("0.1")
    val result = left + right
    assert(result.exponent == 1)
    //assert(result.signum == false)
    assert(result.digits.deep == Array('1', '1').deep)
  }

  if (run) "Normalized" should "add 0.001 + 1000.0 = 1000.001" in {
    val left = Money("0.001")
    val right = Money("1000.0")
    val result = left + right
    assert(result.exponent == 4)
    //assert(result.signum == false)
    assert(result.digits.deep == Array('1', '0', '0', '0', '0', '0', '1').deep)
  }

  if (run) it should "add 1000.0 + 0.001 = 1000.001" in {
    val left = Money("1000.0")
    val right = Money("0.001")
    val result = left + right
    assert(result.exponent == 4)
    //assert(result.signum == false)
    assert(result.digits.deep == Array('1', '0', '0', '0', '0', '0', '1').deep)
  }

  if (run) it should "add 1.01 + 1.00001 = 11.010012" in {
    val left =  Money("1.010002")
    val right = Money("10.00001")
    val result = left + right
    assert(result.exponent == 2)
    //assert(result.signum == false)
    assert(result.digits.deep == Array('1', '1', '0', '1', '0', '0', '1', '2').deep)
  }

  if (run) it should "add 0.0000091 + 0.9999909 = 1.0000000" in {
    val left =  Money("0.0000091")
    val right = Money("0.9999909")
    val result = left + right
    assert(result.exponent == 1)
    //assert(result.signum == false)
    assert(result.digits.deep == Array('1','0', '0', '0', '0', '0', '0', '0').deep)
  }
  
  if (run) {
    val randomDecimalL = new java.math.BigDecimal(java.lang.Math.random()).divide(new java.math.BigDecimal(10000 + ".0"), java.math.BigDecimal.ROUND_UP).multiply(new java.math.BigDecimal("100000000000.0")).toBigInteger()
    val randomDecimalR = new java.math.BigDecimal(java.lang.Math.random()).divide(new java.math.BigDecimal(10000 + ".0"), java.math.BigDecimal.ROUND_UP).multiply(new java.math.BigDecimal("100000000000.0")).toBigInteger()
    val randomDecimal = s"${randomDecimalL}.${randomDecimalR}"
    val resultDecimal = new java.math.BigDecimal(randomDecimal).add(new java.math.BigDecimal(randomDecimal)).toPlainString()

    "Random" should s"add ${randomDecimal} + ${randomDecimal} = ${resultDecimal}" in {
      val left = Money(randomDecimal)
      val right = Money(randomDecimal)
      val result = left + right

      assert(result.exponent == resultDecimal.indexOf('.'))
      //assert(result.signum == false)
      assert(result.digits.deep == resultDecimal.toCharArray.filterNot(_ == '.').deep)
    }
  }
  */
}