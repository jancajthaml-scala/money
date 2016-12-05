package com.github.jancajthaml.money

import org.scalatest.{FlatSpec, Matchers}

class RealSpecs extends FlatSpec with Matchers {

  def cycle(x: String) = println(s"${x} --> ${Real(x).toString}")

  "Parsing" should "handle 1.0" in {
    //val c = Real("000010.00000").toString()

    //println(s"000010.00000 --> ${c}")

    cycle("000010.00000")
    cycle("000010")
    cycle("000010.")
    cycle("00000.01000")
    cycle("000010.01000")
    cycle("1")
    cycle("0")

    cycle("-000010.00000")
    cycle("-000010")
    cycle("-000010.")
    cycle("-00000.01000")
    cycle("-000010.01000")
    cycle("-1")
    cycle("-0")

    //cycle("00000.01000")
    /*
    val a = Real("000010.020000")
    //println(s"000010.020000 >>> ${a}")

    val b = Real("0.020000")
    //println(s"0.020000 >>> ${b}")

    val c = Real("000010.00000")
    //println(s"000010.00000 >>> ${c}")

    val d = Real("000000.00000")
    //println(s"000000.00000 >>> ${d}")

    val e = Real("000010")
    //println(s"000010 >>> ${e}")

    val f = Real("0")
    */
    //println(s"0 >>> ${f}")

    //println()
    //x.repr should === ("1.0001E3")
    //x.toString should === ("1000.1")
    1 should === (1)
  }
  /*
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
  */
}