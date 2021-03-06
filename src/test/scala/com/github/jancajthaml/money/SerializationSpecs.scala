package com.github.jancajthaml.money

import org.scalatest.{FlatSpec, Matchers}

class SerializationSpecs extends FlatSpec with Matchers {

  val run = true

  if (run) "Trivia" should "serialize 1" in {
    val left = Money("1", "EUR")
    assert(left.toString() == "1")
  }

  if (run) it should "serialize 10000000.00000001" in {
    val left = Money("10000000.00000001", "EUR")
    assert(left.toString() == "10000000.00000001")
  }

  

  /*
  it should "parse 2" in {
    val left = Money("2")
    assert(left.exponent == 1)
    //assert(left.signum == false)
    assert(left.digits.deep == Array('2', '0').deep)
  }

  it should s"parse ${Long.MaxValue}${Long.MaxValue}" in {
    val left = Money(s"${Long.MaxValue}${Long.MaxValue}")
    assert(left.exponent == s"${Long.MaxValue}${Long.MaxValue}".size)
    //assert(left.signum == false)
    assert(left.digits.deep == Array('9', '2', '2', '3', '3', '7', '2', '0', '3', '6', '8', '5', '4', '7', '7', '5', '8', '0', '7', '9', '2', '2', '3', '3', '7', '2', '0', '3', '6', '8', '5', '4', '7', '7', '5', '8', '0', '7', '0').deep)
  }

  "Normalized" should "parse 1.0" in {
    val left = Money("1.0")
    assert(left.exponent == 1)
    //assert(left.signum == false)
    assert(left.digits.deep == Array('1', '0').deep)
  }

  it should "parse 0.1" in {
    val left = Money("0.1")
    assert(left.exponent == 1)
    //assert(left.signum == false)
    assert(left.digits.deep == Array('0', '1').deep)
  }

  it should "parse 0.01" in {
    val left = Money("0.01")
    assert(left.exponent == 1)
    //assert(left.signum == false)
    assert(left.digits.deep == Array('0', '0', '1').deep)
  }

  it should "parse 10" in {
    val left = Money("10")
    assert(left.exponent == 2)
    //assert(left.signum == false)
    assert(left.digits.deep == Array('1', '0', '0').deep)
  }

  it should "parse 10.0" in {
    val left = Money("10.0")
    assert(left.exponent == 2)
    //assert(left.signum == false)
    assert(left.digits.deep == Array('1', '0', '0').deep)
  }

  it should "parse 10.1" in {
    val left = Money("10.1")
    assert(left.exponent == 2)
    //assert(left.signum == false)
    assert(left.digits.deep == Array('1', '0', '1').deep)
  }

  it should "parse 10.01" in {
    val left = Money("10.01")
    assert(left.exponent == 2)
    //assert(left.signum == false)
    assert(left.digits.deep == Array('1', '0', '0', '1').deep)
  }

  it should "parse 10000000.00000001" in {
    val left = Money("10000000.00000001")
    assert(left.exponent == 8)
    //assert(left.signum == false)
    assert(left.digits.deep == Array('1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1').deep)
  }

  it should "parse 0" in {
    val left = Money("0")
    assert(left.exponent == 1)
    //assert(left.signum == false)
    assert(left.digits.deep == Array('0', '0').deep)
  }

  it should "parse -1.0" in {
    val left = Money("-1.0")
    assert(left.exponent == 1)
    //assert(left.signum == true)
    assert(left.digits.deep == Array('1', '0').deep)
  }

  it should "parse -0.1" in {
    val left = Money("-0.1")
    assert(left.exponent == 1)
    //assert(left.signum == true)
    assert(left.digits.deep == Array('0', '1').deep)
  }

  it should "parse -0.01" in {
    val left = Money("-0.01")
    assert(left.exponent == 1)
    //assert(left.signum == true)
    assert(left.digits.deep == Array('0', '0', '1').deep)
  }

  it should "parse -10" in {
    val left = Money("-10")
    assert(left.exponent == 2)
    //assert(left.signum == true)
    assert(left.digits.deep == Array('1', '0', '0').deep)
  }

  it should "parse -10.0" in {
    val left = Money("-10.0")
    assert(left.exponent == 2)
    //assert(left.signum == true)
    assert(left.digits.deep == Array('1', '0', '0').deep)
  }

  it should "parse -10.1" in {
    val left = Money("-10.1")
    assert(left.exponent == 2)
    //assert(left.signum == true)
    assert(left.digits.deep == Array('1', '0', '1').deep)
  }

  it should "parse -10.01" in {
    val left = Money("-10.01")
    assert(left.exponent == 2)
    //assert(left.signum == true)
    assert(left.digits.deep == Array('1', '0', '0', '1').deep)
  }

  it should "parse -10000000.00000001" in {
    val left = Money("-10000000.00000001")
    assert(left.exponent == 8)
    //assert(left.signum == true)
    assert(left.digits.deep == Array('1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1').deep)
  }

  it should "parse -0" in {
    val left = Money("-0")
    assert(left.exponent == 1)
    //assert(left.signum == true)
    assert(left.digits.deep == Array('0', '0').deep)
  }

  "Malformed parsing" should "parse 0.0" in {
    val left = Money("0.0")
    assert(left.exponent == 1)
    //assert(left.signum == false)
    assert(left.digits.deep == Array('0', '0').deep)
  }

  it should "parse 01.10" in {
    val left = Money("01.10")
    assert(left.exponent == 2)
    //assert(left.signum == false)
    assert(left.digits.deep == Array('0', '1', '1', '0').deep)
  }

  it should "parse 00000000.00000001" in {
    val left = Money("00000000.00000001")
    assert(left.exponent == 8)
    //assert(left.signum == false)
    assert(left.digits.deep == Array('0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1').deep)
  }

  it should "parse 10000000.00000000" in {
    val left = Money("10000000.00000000")
    assert(left.exponent == 8)
    //assert(left.signum == false)
    assert(left.digits.deep == Array('1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0').deep)
  }

  val randomDecimalL = new java.math.BigDecimal(java.lang.Math.random()).divide(new java.math.BigDecimal(10000 + ".0"), java.math.BigDecimal.ROUND_UP).multiply(new java.math.BigDecimal("100000000000.0")).toBigInteger()
  val randomDecimalR = new java.math.BigDecimal(java.lang.Math.random()).divide(new java.math.BigDecimal(10000 + ".0"), java.math.BigDecimal.ROUND_UP).multiply(new java.math.BigDecimal("100000000000.0")).toBigInteger()
  
  val randomDecimal = s"${randomDecimalL}.${randomDecimalR}"


  "Random parsing" should s"parse ${randomDecimal}" in {
    val left = Money(randomDecimal)
    assert(left.exponent == randomDecimal.indexOf('.'))
    //assert(left.signum == false)
    assert(left.digits.deep == randomDecimal.toCharArray.filterNot(_ == '.').deep)
  }
  */

}