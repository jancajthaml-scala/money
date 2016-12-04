package com.github.jancajthaml.money

import org.scalatest.{FlatSpec, Matchers}

class RealSpecs extends FlatSpec with Matchers {

  "Parsing with trailing zeroes" should "handle 0000.1" in {
    val x = Real("0000.1")
    x.repr should === ("1.E-1")
    x.toString should === ("0.1")
  }

  it should "handle 1000.1" in {
    val x = Real("1000.1")
    x.repr should === ("1.0001E3")
    x.toString should === ("1000.1")
  }

  it should "handle 000.1000" in {
    val x = Real("000.1000")
    x.repr should === ("1.E-1")
    x.toString should === ("0.1")
  }

  it should "handle 1000.1000" in {
    val x = Real("1000.1000")
    x.repr should === ("1.0001E3")
    x.toString should === ("1000.1")
  }

  it should "handle 0001000.1000" in {
    val x = Real("0001000.1000")
    x.repr should === ("1.0001E3")
    x.toString should === ("1000.1")
  }

  it should "handle 0001000.0001" in {
    val x = Real("0001000.0001")
    x.repr should === ("1.0000001E3")
    x.toString should === ("1000.0001")
  }

  it should "handle 0001" in {
    val x = Real("0001")
    x.repr should === ("1.E0")
    x.toString should === ("1")
  }

  it should "handle 0001000" in {
    val x = Real("0001000")
    x.repr should === ("1.E3")      // TODO/FIXME make this work
    x.toString should === ("1000")  // TODO/FIXME make this work
  }

  it should "handle 0" in {
    val x = Real("0")
    x.repr should === ("0")
    x.toString should === ("0")
  }

  "Parsing of signum" should "handle -0000.1" in {
    val x = Real("-0000.1")
    x.repr should === ("-1.E-1")
    x.toString should === ("-0.1")
  }

  it should "handle -0" in {
    val x = Real("-0")
    x.repr should === ("-0")
    x.toString should === ("-0")
  }
}