package com.github.jancajthaml.money

import org.scalatest.{FlatSpec, Matchers}

class AdditionSpecs extends FlatSpec with Matchers {

  val run = true

  if (run) "Trivia" should "add 1 + 1 = 2.0" in {
    val left = Money("1", "EUR")
    val right = Money("1", "EUR")
    val result = left + right
    //assert(result.decimal == 1)
    //assert(result.signum == false)
    assert(result.value == "2")
  }

  if (run) it should "add 1 + 1 - 100 + 99 + 2 - 3 = 0.0" in {
    val a = Money("1", "EUR")
    val b = Money("1", "EUR")
    val c = Money("100", "EUR")
    val d = Money("99", "EUR")
    val e = Money("2", "EUR")
    val f = Money("3", "EUR")

    val result = a + b - c + d + e - f
    assert(result.value == "0")
  }

  if (run) it should "add 0 + 10001 = 10001.0" in {
    val left = Money("0", "EUR")
    val right = Money("10001", "EUR")
    val result = left + right
    //assert(result.decimal == 5)
    //assert(result.signum == false)
    assert(result.value == "10001")
  }

  if (run) it should "add 10001 + 0 = 10001.0" in {
    val left = Money("10001", "EUR")
    val right = Money("0", "EUR")
    val result = left + right
    //assert(result.decimal == 5)
    //assert(result.signum == false)
    assert(result.value == "10001")
  }

  if (run) it should "add -1 + -1 = -2.0" in {
    val left = Money("-1", "EUR")
    val right = Money("-1", "EUR")
    val result = left + right
    //assert(result.decimal == 1)
    //assert(result.signum == true)
    assert(result.value == "-2")
  }

  if (run) it should "add -0 + -10001 = -10001.0" in {
    val left = Money("-0", "EUR")
    val right = Money("-10001", "EUR")
    val result = left + right
    //assert(result.decimal == 5)
    //assert(result.signum == true)
    assert(result.value == "-10001")
  }

  if (run) it should "add 1 + 9 = 10.0" in {
    val left = Money("1", "EUR")
    val right = Money("9", "EUR")
    val result = left + right
    //assert(result.decimal == 2)
    //assert(result.signum == false)
    assert(result.value == "10")
  }

  if (run) it should "add 9 + 9 = 18.0" in {
    val left = Money("9", "EUR")
    val right = Money("9", "EUR")
    val result = left + right
    //assert(result.decimal == 2)
    //assert(result.signum == false)
    assert(result.value == "18")
  }

  if (run) "Normalized" should "add 1.0 + 0.1 = 1.1" in {
    val left = Money("1.0", "EUR")
    val right = Money("0.1", "EUR")
    val result = left + right
    //assert(result.decimal == 1)
    //assert(result.signum == false)
    assert(result.value == "1.1")
  }

  if (run) "Normalized" should "add 0.001 + 1000.0 = 1000.001" in {
    val left = Money("0.001", "EUR")
    val right = Money("1000.0", "EUR")
    val result = left + right
    //assert(result.decimal == 4)
    //assert(result.signum == false)
    assert(result.value == "1000.001")
  }

  if (run) it should "add 1000.0 + 0.001 = 1000.001" in {
    val left = Money("1000.0", "EUR")
    val right = Money("0.001", "EUR")
    val result = left + right
    //assert(result.decimal == 4)
    //assert(result.signum == false)
    assert(result.value == "1000.001")
  }

  if (run) it should "add 1.01 + 1.00001 = 11.010012" in {
    val left =  Money("1.010002", "EUR")
    val right = Money("10.00001", "EUR")
    val result = left + right
    //assert(result.decimal == 2)
    //assert(result.signum == false)
    assert(result.value == "11.010012")
  }

  if (run) it should "add 0.0000091 + 0.9999909 = 1" in {
    val left =  Money("0.0000091", "EUR")
    val right = Money("0.9999909", "EUR")
    val result = left + right
    //assert(result.decimal == 1)
    //assert(result.signum == false)
    assert(result.value == "1")
  }

}