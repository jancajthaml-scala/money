package com.github.jancajthaml.money

import Real._

//type number = (Boolean, Array[Int], Int)

object Real {

  def loads(x: Real, str: String) {
    var buffer = Array.empty[Int]

    var leftSkip = false
    var rightDrop = -1
    var rightScan = true

    var decimal = str.length

    str.foreach(c => {
      if (c == '-') {
        x.signum = true
      } else if (c == '0') {
        if (leftSkip && rightScan) {
          rightDrop = buffer.size
          buffer :+= 0
          rightScan = false
        } else if (leftSkip){
          buffer :+= 0
        }
      } else if (c == '.') {
        decimal = buffer.size
        leftSkip = true
        rightDrop = -1
      } else {
        buffer :+= (c.toInt - 48)
        leftSkip = true
        rightScan = true
        rightDrop = -1
      }
    })

    x.exponent = decimal - 1

    if (x.exponent < 0 && rightDrop > -1) {
      x.digits = buffer.take(rightDrop).drop(-x.exponent)
      x.exponent -= 1
    } else if (x.exponent < 0) {
      x.digits = buffer.drop(-x.exponent)
      x.exponent -= 1
    } else if (rightDrop > -1) {
      x.digits = buffer.take(rightDrop)
    } else {
      x.digits = buffer
    }
  }

  def dumps(x: Real, precision: Int): String = {
    // TODO/FIXME really optimise
    // TODO/FIXME construct string manually without mkString (multiple O(n))

    var buffer = x.digits
    var dp = precision

    var i = x.exponent + 1
    var str = ""

    if (x.exponent < 0) {
      while (i < 0) {
        i += 1
        buffer +:= 0
      }
      if (x.signum) {
        "-0." + buffer.mkString("")
      } else {
        "0." + buffer.mkString("")
      }
    } else {
      if (i > buffer.size) {
        i -= buffer.size
        while (i > 0) {
          i -= 1
          buffer :+= 0
        }
        if (x.signum) {
          "-" + buffer.mkString("")
        } else {
          buffer.mkString("")
        }
      } else {
        if (x.signum) {
          val remaining = buffer.size - i
          if (remaining > 0) {
            "-" + buffer.take(i).mkString("") + "." + buffer.takeRight(remaining).mkString("")
          } else {
            "-" + buffer.take(i).mkString("")
          }
        } else {
          val remaining = buffer.size - i
          if (remaining > 0) {
            buffer.take(i).mkString("") + "." + buffer.takeRight(remaining).mkString("")
          } else {
            buffer.take(i).mkString("")
          }
        }
      }
    }
  }

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
}

case class Real(str: String) extends Cloneable {

  val precision = 64
  var signum = false
  var digits = Array.empty[Int]
  var exponent = 0

  loads(this, str)

  override def toString(): String = dumps(this, precision - 1)

  def +  (r: Real) = _add(super.clone().asInstanceOf[Real], r)
  def += (r: Real) = _add(this, r)

  def -  (r: Real) = _sub(super.clone().asInstanceOf[Real], r)
  def -= (r: Real) = _sub(this, r)
}