package com.github.jancajthaml.money

import org.scalatest.{FlatSpec, Matchers}

class AdditionSpecs extends FlatSpec with Matchers {

  val run = false

  if (run) "Trivia" should "add 1 + 1 = 2.0" in {
    val left = Real("1")
    val right = Real("1")
    val result = left + right
    assert(result.exponent == 1)
    assert(result.signum == false)
    assert(result.digits.deep == Array('2', '0').deep)
  }

  if (run) it should "add 0 + 10001 = 10001.0" in {
    val left = Real("0")
    val right = Real("10001")
    val result = left + right
    assert(result.exponent == 5)
    assert(result.signum == false)
    assert(result.digits.deep == Array('1', '0', '0', '0', '1', '0').deep)
  }

  if (run) it should "add 10001 + 0 = 10001.0" in {
    val left = Real("10001")
    val right = Real("0")
    val result = left + right
    assert(result.exponent == 5)
    assert(result.signum == false)
    assert(result.digits.deep == Array('1', '0', '0', '0', '1', '0').deep)
  }

  if (run) it should "add -1 + -1 = -2.0" in {
    val left = Real("-1")
    val right = Real("-1")
    val result = left + right
    assert(result.exponent == 1)
    assert(result.signum == true)
    assert(result.digits.deep == Array('2', '0').deep)
  }

  if (run) it should "add -0 + -10001 = -10001.0" in {
    val left = Real("-0")
    val right = Real("-10001")
    val result = left + right
    assert(result.exponent == 5)
    assert(result.signum == true)
    assert(result.digits.deep == Array('1', '0', '0', '0', '1', '0').deep)
  }

  if (run) it should "add 1 + 9 = 10.0" in {
    val left = Real("1")
    val right = Real("9")
    val result = left + right
    assert(result.exponent == 2)
    assert(result.signum == false)
    assert(result.digits.deep == Array('1', '0', '0').deep)
  }

  if (run) it should "add 9 + 9 = 18.0" in {
    val left = Real("9")
    val right = Real("9")
    val result = left + right
    assert(result.exponent == 2)
    assert(result.signum == false)
    assert(result.digits.deep == Array('1', '8', '0').deep)
  }

  if (run) "Normalized" should "add 1.0 + 0.1 = 1.1" in {
    val left = Real("1.0")
    val right = Real("0.1")
    val result = left + right
    assert(result.exponent == 1)
    assert(result.signum == false)
    assert(result.digits.deep == Array('1', '1').deep)
  }

  if (run) "Normalized" should "add 0.001 + 1000.0 = 1000.001" in {
    val left = Real("0.001")
    val right = Real("1000.0")
    val result = left + right
    assert(result.exponent == 4)
    assert(result.signum == false)
    assert(result.digits.deep == Array('1', '0', '0', '0', '0', '0', '1').deep)
  }

  if (run) it should "add 1000.0 + 0.001 = 1000.001" in {
    val left = Real("1000.0")
    val right = Real("0.001")
    val result = left + right
    assert(result.exponent == 4)
    assert(result.signum == false)
    assert(result.digits.deep == Array('1', '0', '0', '0', '0', '0', '1').deep)
  }

  if (run) it should "add 1.01 + 1.00001 = 11.010012" in {
    val left =  Real("1.010002")
    val right = Real("10.00001")
    val result = left + right
    assert(result.exponent == 2)
    assert(result.signum == false)
    assert(result.digits.deep == Array('1', '1', '0', '1', '0', '0', '1', '2').deep)
  }

  if (run) it should "add 0.0000091 + 0.9999909 = 1.0000000" in {
    val left =  Real("0.0000091")
    val right = Real("0.9999909")
    val result = left + right
    assert(result.exponent == 1)
    assert(result.signum == false)
    assert(result.digits.deep == Array('1','0', '0', '0', '0', '0', '0', '0').deep)
  }
  
  if (run) {
    val randomDecimalL = new java.math.BigDecimal(java.lang.Math.random()).divide(new java.math.BigDecimal(10000 + ".0"), java.math.BigDecimal.ROUND_UP).multiply(new java.math.BigDecimal("100000000000.0")).toBigInteger()
    val randomDecimalR = new java.math.BigDecimal(java.lang.Math.random()).divide(new java.math.BigDecimal(10000 + ".0"), java.math.BigDecimal.ROUND_UP).multiply(new java.math.BigDecimal("100000000000.0")).toBigInteger()
    val randomDecimal = s"${randomDecimalL}.${randomDecimalR}"
    val resultDecimal = new java.math.BigDecimal(randomDecimal).add(new java.math.BigDecimal(randomDecimal)).toPlainString()

    "Random" should s"add ${randomDecimal} + ${randomDecimal} = ${resultDecimal}" in {
      val left = Real(randomDecimal)
      val right = Real(randomDecimal)
      val result = left + right

      assert(result.exponent == resultDecimal.indexOf('.'))
      assert(result.signum == false)
      assert(result.digits.deep == resultDecimal.toCharArray.filterNot(_ == '.').deep)
    }
  }
}