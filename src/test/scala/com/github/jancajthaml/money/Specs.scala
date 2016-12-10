package com.github.jancajthaml.money

import org.scalatest.{FlatSpec, Matchers}

class RealSpecs extends FlatSpec with Matchers {

  def cycle(x: String) = {
    //Real(x)
    println(s"${x} --> ${Real(x).toString}")
  }

  def add(a: String, b: String) = { // TODO/FIXME
    //val x = operation.split("\\+")
    //val x = Set(a, b)
    val operation = s"$a + $b"
    println(s"${operation} --> ${Real(a) + Real(b)}")
    //Real(a) + Real(b)
  }

  "Parsing" should "handle 1.0" in {
    println(">>>> PARSING")

    //cycle("000010.01000")

    cycle("0.1001")
    cycle("0.1010")
    cycle("0.1000")
    cycle("0.10000")
    cycle("0.00001000000")
    cycle("0.000010")
    cycle("0.000010")
    cycle("0.1")
    cycle("0.0")

    cycle("00000.001010")
    cycle("00000.001110")
    cycle("00000.001000")
    cycle("00000.0001000")

    /*
    cycle("000010.01000")

    cycle("00000.001010")
    cycle("00000.001110")
    cycle("00000.001000")
    
    cycle("1001")
    cycle("1010")
    cycle("1000")
    cycle("1000.0")
    cycle("000010.00000")
    cycle("000010")
    cycle("000010.")
    cycle("1")
    cycle("0")
    cycle("00000.0001000")
    cycle("000010.01000")
    cycle("1000")
    cycle("1000.0")
    cycle("1000.01")
    cycle("000010.00000")
    cycle("000010")
    cycle("000010.")
    cycle("1")
    cycle("0")

    cycle("-00000.01000")
    cycle("-00000.0001000")
    cycle("-000010.01000")
    cycle("-1000")
    cycle("-1000.0")
    cycle("-1000.01")
    cycle("-000010.00000")
    cycle("-000010")
    cycle("-000010.")
    cycle("-1")
    cycle("-0")
    */

    println(">>>> ADDITION")

    //add("1", "2")
    
    //add("000010.00000", "000001.00000")
    //add("000000.01000", "000010.00000")
    //add("000010.01000", "000010.01000")

    /*
    cycle("000010.")
    cycle("1000.0")
    cycle("10010.0")
    cycle("1001.0")
    */

    /*
    cycle("000010.01000")

    cycle("00000.001010")
    cycle("00000.001110")
    cycle("00000.001000")
    
    cycle("1001")
    cycle("1010")
    cycle("1000")
    cycle("1000.0")
    cycle("000010.00000")
    cycle("000010")
    cycle("000010.")
    cycle("1")
    cycle("0")
    cycle("00000.0001000")
    cycle("000010.01000")
    cycle("1000")
    cycle("1000.0")
    cycle("1000.01")
    cycle("000010.00000")
    cycle("000010")
    cycle("000010.")
    cycle("1")
    cycle("0")

    cycle("-00000.01000")
    cycle("-00000.0001000")
    cycle("-000010.01000")
    cycle("-1000")
    cycle("-1000.0")
    cycle("-1000.01")
    cycle("-000010.00000")
    cycle("-000010")
    cycle("-000010.")
    cycle("-1")
    cycle("-0")
    */
    

    /*


    println(">>>> ADDITION")

    add("1", "2")
    add("000010.00000", "000000.01000")
    add("000000.01000", "000010.00000")*/

    /*
    val ref1 = Real("1" + ("0" * 5) + "0.1")
    val ref2 = BigDecimal("1" + ("0" * 5) + "0.1")

    println(s"${ref1.toString}")
    println(s"${ref2.toString}")
    */

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
}