package com.github.jancajthaml.money

import Real._
import Mapping._
import Serialization.{fromString => _loads}
import Math.{__add}

//import scala.collection.mutable.{ListBuffer => Buffer}
//import sun.misc.Unsafe

//type number = (Boolean, Array[Int], Int)

object Real {

  def loads(x: Real) = {
    val native = _loads(x.value)
    x.signum = native(0).asInstanceOf[Boolean]
    x.exponent = native(1).asInstanceOf[Int]
    x.digits = native(2).asInstanceOf[Array[Int]]
    x.value = native(3).asInstanceOf[String]
  }

  def dumps(x: Real): String = {
    // TODO/FIXME not ideally performant
    val decimal = x.exponent + 1
    if (x.digits.isEmpty) {
      if (x.signum) "-0" else "0"
    } else if (decimal > 1 && decimal > x.digits.size) {
      val dump = x.digits.map(int2char(_)).mkString + ("0" * x.exponent)
      (if (x.signum) "-" else "") + dump
    } else if (decimal <= 1 && decimal < x.digits.size) {
      val dump = ("0" * (-decimal)) + x.digits.map(int2char(_)).mkString
      (if (x.signum) "-0." else "0.") + dump
    } else {
      (if (x.signum) "-" else "") +
      (if (decimal < x.digits.size) (x.digits.take(decimal).map(int2char(_)).mkString + "." + x.digits.drop(decimal).map(int2char(_)).mkString) else x.digits.map(int2char(_)).mkString)
    }
  }

  private def _add(l: Real, r: Real) = {
    val native = __add(l.signum, l.digits, l.exponent, r.signum, r.digits, r.exponent)

    //return new Object[]{ ls, le, ld };
    l.signum = native(0).asInstanceOf[Boolean]
    l.exponent = native(1).asInstanceOf[Int]
    l.digits = native(2).asInstanceOf[Array[Int]]

    l.value = dumps(l) // TODO/FIXME HUUUUGE performance bottleneck

    l
  }
  /*
  private def _sub(l: Real, r: Real) = {
    // Signs differ?
    if (l.signum ^ r.signum) {
      r
    } else {
      var xc = l.digits
      var xe = l.exponent
      var yc = r.digits
      var ye = r.exponent

      // Either zero?
      if (xc(0) == 0 || yc(0) == 0) {
        Real("0")
      } else {
        // TODO/FIXME problem with different array lengths

        // Determine which is the bigger number.
        // Prepend zeros to equalise exponents.
        var a = xe - ye
        var xLTy = false
        var b = 0

        if (a != 0) {
          xLTy = a < 0
          if (xLTy) {
            a = -a
            b = a
            while (b > 0) { xc +:= 0; b -= 1 }
          } else {
            ye = xe
            b = a
            while (b > 0) { yc +:= 0; b -= 1 }
          }
        } else {
          // Exponents equal. Check digit by digit.
          xLTy = (xc.size < yc.size)
          var j = (if (xLTy) xc else yc).size
          b = 0
          a = 0

          while (b < j && xc(b) == yc(b)) {
            b += 1

            if (b < j && xc(b) != yc(b)) {
              xLTy = xc(b) < yc(b)
            }
          }
        }

        // x < y? Point xc to the array of the bigger number.
        if (xLTy) {
          val swap = xc
          xc = yc
          yc = swap
          l.signum = !l.signum
        }

        /*
         * Append zeros to xc if shorter. No need to add zeros to yc if shorter
         * as subtraction only needs to start at yc.length.
         */
        var i = xc.size
        var j = yc.size
        
        if (j > i) {
          b = j - i

          while (b > 0 && i < xc.size) {
            b -= 1
            xc(i) = 0
            i += 1
          }
        }

        // Subtract yc from xc.
        b = i
        while (j > a) {
          j -= 1
          if (j < xc.size && j < yc.size) {
            if (xc(j) < yc(j)) {
              i = j

              while (i != 0 && xc(i - 1) == 0) {
                i -= 1
                xc(i) = 9
              }
              i -= 1
              xc(i) -= 1
              xc(j) += 10
            }
            xc(j) -= yc(j)
          }
        }

        b -= 1

        // Remove trailing zeros.
        while (xc(b) == 0) {
          xc = xc.dropRight(1)
          b -= 1
        }

        // Remove leading zeros and adjust exponent accordingly.
        while (xc(0) == 0) {
          xc = xc.drop(1)
          ye -= 1
        }

        if (xc(0) == 0) {
          l.signum = false
          l.digits = Array(0)
          l.exponent = ye
        } else {
          l.digits = xc
          l.exponent = ye
        }

        l
      }
    }
  }
  */
}

case class Real(var value: String) extends Cloneable with Comparable[Real] {

  var signum = false
  var exponent = 0
  var digits = Array.empty[Int]

  loads(this)

  override def toString() = value

  override def compareTo(r: Real) = ???

  def +  (r: Real) = _add(super.clone().asInstanceOf[Real], r)
  def += (r: Real) = _add(this, r)

  /*
  def -  (r: Real) = _sub(super.clone().asInstanceOf[Real], r)
  def -= (r: Real) = _sub(this, r)
  */

  /*
  def *  (r: Real) = _mul(super.clone().asInstanceOf[Real], r)
  def *= (r: Real) = _mul(this, r)
  */

  /*
  def /  (r: Real) = _div(super.clone().asInstanceOf[Real], r)
  def /= (r: Real) = _div(this, r)
  */

  def unary_- = {
    val x = super.clone().asInstanceOf[Real]
    x.signum = !x.signum
    x
  }
}