package com.github.jancajthaml.money

import org.scalatest.{FlatSpec, Matchers}

class ParsingSpecs extends FlatSpec with Matchers {

  val run = false

  if (run) "Trivia" should "parse 1" in {
    val left = Money("1", "EUR")
    assert(left.value == "1.0")
  }

  if (run) it should "parse 2" in {
    val left = Money("2", "EUR")
    assert(left.value == "2.0")
  }

  if (run) it should s"parse ${Long.MaxValue}${Long.MaxValue}" in {
    val left = Money(s"${Long.MaxValue}${Long.MaxValue}", "EUR")
    assert(left.value == s"${Long.MaxValue}${Long.MaxValue}.0")
  }

  if (run) "Normalized" should "parse 1.0" in {
    val left = Money("1.0", "EUR")
    assert(left.value == "1.0")
  }

  if (run) it should "parse 0.1" in {
    val left = Money("0.1", "EUR")
    assert(left.value == "0.1")
  }

  if (run) it should "parse 0.01" in {
    val left = Money("0.01", "EUR")
    assert(left.value == "0.01")
  }

  if (run) it should "parse 10" in {
    val left = Money("10", "EUR")
    assert(left.value == "10.0")
  }

  if (run) it should "parse 10.0" in {
    val left = Money("10.0", "EUR")
    assert(left.value == "10.0")
  }

  if (run) it should "parse 10.1" in {
    val left = Money("10.1", "EUR")
    assert(left.value == "10.1")
  }

  if (run) it should "parse 10.01" in {
    val left = Money("10.01", "EUR")
    assert(left.value == "10.01")
  }

  if (run) it should "parse 10000000.00000001" in {
    val left = Money("10000000.00000001", "EUR")
    assert(left.value == "10000000.00000001")
  }

  if (run) it should "parse 0" in {
    val left = Money("0", "EUR")
    assert(left.value == "0.0")
  }

  if (run) it should "parse -1.0" in {
    val left = Money("-1.0", "EUR")
    assert(left.value == "-1.0")
  }

  if (run) it should "parse -0.1" in {
    val left = Money("-0.1", "EUR")
    assert(left.value == "-0.1")
  }

  if (run) it should "parse -0.01" in {
    val left = Money("-0.01", "EUR")
    assert(left.value == "-0.01")
  }

  if (run) it should "parse -10" in {
    val left = Money("-10", "EUR")
    assert(left.value == "-10.0")
  }

  if (run) it should "parse -10.0" in {
    val left = Money("-10.0", "EUR")
    assert(left.value == "-10.0")
  }

  if (run) it should "parse -10.1" in {
    val left = Money("-10.1", "EUR")
    assert(left.value == "-10.1")
  }

  if (run) it should "parse -10.01" in {
    val left = Money("-10.01", "EUR")
    assert(left.value == "-10.01")
  }

  if (run) it should "parse -10000000.00000001" in {
    val left = Money("-10000000.00000001", "EUR")
    assert(left.value == "-10000000.00000001")
  }

  if (run) it should "parse -0" in {
    val left = Money("-0", "EUR")
    assert(left.value == "-0.0")
  }

  if (run) "Malformed" should "parse 0.0" in {
    val left = Money("0.0", "EUR")
    assert(left.value == "0.0")
  }

  if (run) it should "parse 01.10" in {
    val left = Money("01.10", "EUR")
    assert(left.value == "01.10")
  }

  if (run) it should "parse 00000000.00000001" in {
    val left = Money("00000000.00000001", "EUR")
    assert(left.value == "00000000.00000001")
  }

  if (run) it should "parse 10000000.00000000" in {
    val left = Money("10000000.00000000", "EUR")
    assert(left.value == "10000000.00000000")
  }
}
