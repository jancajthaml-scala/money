package com.github.jancajthaml.money

import Real._

//import sun.misc.Unsafe

//type number = (Boolean, Array[Int], Int)

object Real {

  private val prefixN = Seq('-', '0', '.')
  private val prefixP = Seq('0' + '.')

  def loads(x: Real) = {
    var i = 0

    if (x.digits.charAt(0) == '-') {
      i += 1
      x.signum = true
    }

    // TODO/FIXME rework that pointers works over original buffer
    var left = ""
    var right = ""

    var decimal = 0

    var decimalFound = false
    var leftOffsetFound = false
    var rightOffsetFound = false

    var leftPass = 0
    var rightPass = 0

    while (!decimalFound && i < x.digits.length - 1) {
      val c = x.digits.charAt(i)
      i += 1
      if (c == '.') {
        decimal = left.length
        decimalFound = true
        i = x.digits.length - 1
      } else if (c == '0' && leftOffsetFound) {
        left += "0"
        leftPass += 1
      } else if (c != '0') {
        left += c
        leftOffsetFound = true
      }
    }

    while (i > -1) {
      val c = x.digits.charAt(i)
      if (c == '.') {
        i = 0
      } else if (c == '0' && rightOffsetFound) {
        right = "0" + right
        rightPass += 1
      } else if (c != '0') {
        right = c + right
        rightOffsetFound = true
      }
      i -= 1
    }
    if (right.length == 0) {
      x.exponent = leftPass + 1
      x.digits = left.substring(0, left.length - leftPass)
    } else if (left.length == 0) {
      x.exponent = -rightPass - 1
      x.digits = right.substring(right.length - rightPass, right.length)
    } else {
      val buffer = left + right
      x.exponent = if (buffer.length > 0) decimal - 1 else 0
      x.digits = buffer
    }
  }

  def dumps(x: Real): String = {
    if (x.digits.isEmpty) {
      if (x.signum) "-0" else "0"
    } else if (x.exponent > 0 && x.exponent > x.digits.length) {
      val dump = x.digits + ("0" * (x.exponent - 1))
      (if (x.signum) "-" else "") + dump
    } else if (x.exponent <= 0 && x.exponent < x.digits.length) {
      val dump = ("0" * (-x.exponent - 1)) + x.digits
      (if (x.signum) "-0." else "0.") + dump
    } else {
      val dump = x.digits
      val decimal = x.exponent + 1
      (if (x.signum) "-" else "") +
      (if (decimal < x.digits.length) (dump.substring(0, decimal) + "." + dump.substring(dump.length - decimal, dump.length)) else dump)
    }
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

  private def _add(l: Real, r: Real) = {
    // Signs differ?
    if (l.signum ^ r.signum) {
      r
    } else {
      var xe = l.exponent
      var xc = l.digits
      var ye = r.exponent
      var yc = r.digits

      // Either zero?
      if (xc(0) == 0 || yc(0) == 0) {
        Real("0")
      } else {
        var a = xe - ye
        if (a > 0) {
          ye = xe
          while (a > 0) {
            yc +:= 0
            a -= 1
          }
        } else {
          a = -a
          while (a > 0) {
            xc +:= 0
            a -= 1
          }
        }

        // Point left digits to the longer array.
        if (l.digits.size < r.digits.size) {
          val swap = yc
          yc = xc
          xc = swap
        }

        a = yc.size

        var b = 0

        /*
         * Only start adding at yc.length - 1 as the further digits of xc can be
         * left as they are.
         */
        while (a != 0) {
          a -= 1
          xc(a) = xc(a) + yc(a) + b
          b = xc(a) / 10 | 0
          xc(a) %= 10
        }

        // No need to check for zero, as +x + +y != 0 && -x + -y != 0
        if (b != 0) {
          xc +:= b
          ye += 1
        }

        a = xc.size - 1

        // Remove trailing zeros.
        while (xc(a) == 0) {
          xc = xc.dropRight(1)
          a -= 1
        }

        l.digits = xc
        l.exponent = ye
        l.signum = r.signum
        l
      }
    }

  }
  */
}

case class Real(var digits: String) extends Cloneable {

  var signum = false
  var exponent = 0

  loads(this)

  override def toString(): String = dumps(this)

  /*
  def +  (r: Real) = _add(super.clone().asInstanceOf[Real], r)
  def += (r: Real) = _add(this, r)

  def -  (r: Real) = _sub(super.clone().asInstanceOf[Real], r)
  def -= (r: Real) = _sub(this, r)
  */
}