package com.github.jancajthaml.money

import Real._

import scala.collection.mutable.{ListBuffer => Buffer}
//import sun.misc.Unsafe

//type number = (Boolean, Array[Int], Int)

object Real {

  private val prefixN = Seq('-', '0', '.')
  private val prefixP = Seq('0' + '.')

  def loads(x: Real) = {
    var i = 0

    if (x.value.charAt(0) == '-') {
      i += 1
      x.signum = true
    }

    var left = Buffer.empty[Char]
    var right = Buffer.empty[Char]

    var decimal = 0

    var decimalFound = false
    var leftOffsetFound = false
    var rightOffsetFound = false

    var leftPass = 0
    var rightPass = 0

    while (!decimalFound && i < x.value.length) {
      val c = x.value.charAt(i)
      i += 1
      if (c == '.') {
        decimal = left.size
        decimalFound = true
        i = x.value.length - 1
      } else if (leftOffsetFound && c == '0') {
        left += '0'
        leftPass += 1
      } else if (c != '0') {
        left += c
        leftOffsetFound = true
      }
    }

    if (decimalFound) {
      while (i > -1) {
        val c = x.value.charAt(i)
        if (c == '.' || c == '-') {
          i = 0
        } else if (rightOffsetFound && c == '0') {
          right += '0'
          rightPass += 1
        } else if (c != '0') {
          right += c
          rightOffsetFound = true
        }
        i -= 1
      }
    }

    if (right.isEmpty) {
      x.exponent = leftPass
      x.digits = left.take(left.size - leftPass)
    } else if (left.isEmpty) {
      x.exponent = -rightPass - 1
      x.digits = right.take(right.size - rightPass).reverse
    } else {
      left ++= right.reverse
      x.exponent = if (left.isEmpty) 0 else decimal - 1
      x.digits = left
    }

    // TODO/FIXME inline don't use dumps here
    x.value = dumps(x)
  }

  def dumps(x: Real): String = {
    val decimal = x.exponent + 1
    if (x.digits.isEmpty) {
      if (x.signum) "-0" else "0"
    } else if (decimal > 1 && decimal > x.digits.size) {
      val dump = x.digits.mkString + ("0" * x.exponent)
      (if (x.signum) "-" else "") + dump
    } else if (decimal <= 1 && decimal < x.digits.size) {
      val dump = ("0" * (-decimal)) + x.digits.mkString
      (if (x.signum) "-0." else "0.") + dump
    } else {
      (if (x.signum) "-" else "") +
      (if (decimal < x.digits.size) (x.digits.take(decimal).mkString + "." + x.digits.drop(decimal).mkString) else x.digits.mkString)
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
  */

  private def _add(l: Real, r: Real) = {

    // TODO/FIXME BUG
    // >> properly padded L: List(1, 0, 0, 0, 0) from List(1) with 2 ... 10

    // Signs differ?
    if (l.signum ^ r.signum) {
      r
    } else {
      var xe = l.exponent
      var xc = l.digits.map(x => (x - 48).toInt)   // TODO/FIXME shortcut
      var ye = r.exponent
      var yc = r.digits.map(x => (x - 48).toInt)   // TODO/FIXME shortcut

      // Either zero?
      if (xc(0) == 0 || yc(0) == 0) {
        Real("0")
      } else {
        var a = (xe - ye)
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
        if (xc.size < yc.size) {
          // TODO/FIXME do this better
          (1 to (yc.size - xc.size)).foreach(x => { xc :+= 0 })
        } else if (yc.size < xc.size) {
          // TODO/FIXME do this better
          (1 to (xc.size - yc.size)).foreach(x => { yc +:= 0 })
        }

        //println(s">> properly padded L: ${xc.toSeq} from ${l.digits.toSeq} with ${l.exponent} ... ${l.value}")
        //println(s">> properly padded R: ${yc.toSeq} from ${r.digits.toSeq} with ${r.exponent} ... ${r.value}")

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
          //println(s"adding ${xc(a)} and ${yc(a)} and ${b}")
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

        //println("happy day")

        l.digits = xc.map(x => (x + 48).toChar) // TODO/FIXME shortcut
        l.exponent = ye
        l.signum = r.signum

        // TODO/FIXME inline don't use dumps here
        l.value = dumps(l)
        l
      }
    }

  }
}

case class Real(var value: String) extends Cloneable {

  var signum = false
  var exponent = 0
  var digits = Buffer.empty[Char]

  loads(this)

  override def toString(): String = value

  def +  (r: Real) = _add(super.clone().asInstanceOf[Real], r)
  def += (r: Real) = _add(this, r)

  /*
  def -  (r: Real) = _sub(super.clone().asInstanceOf[Real], r)
  def -= (r: Real) = _sub(this, r)
  */
}