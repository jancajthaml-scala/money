package com.github.jancajthaml.money

import Real._

//type number = (Boolean, Array[Int], Int)

object Real {

  def loads(x: Real, str: String) {
    var buffer = str.toCharArray()

    if (buffer(0) == '-') {
      buffer = buffer.drop(1)
      x.signum = true
    }

    // TODO/FIXME merge this
    var decimal = buffer.indexOf('.')

    if (decimal > -1) {
      // TODO/FIXME with this 
      buffer = buffer.filterNot(_ == '.')
    } else {
      decimal = buffer.size
    }

    // TODO/FIXME remove mutability
    var i = 0

    while ((i < buffer.size - 1) && buffer(i) == '0') {
      i += 1
    }

    var nL = buffer.size

    if (i == nL) {
      x.digits :+= 0
    } else {
      nL -= 1
      while ((nL < buffer.size - 1) && buffer(nL) == '0') {
        nL -= 1
      }

      x.exponent = decimal - i - 1

      while (i <= nL) {
        x.digits :+= buffer(i).asDigit
        i += 1
      }
    }
  }

  def dumps(x: Real, precision: Int): String = {
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
          l.signum = false  // n - n = +0
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

// TODO/FIXME add cloneable
case class Real(str: String) {

  val precision = 64
  var signum = false
  var digits = Array.empty[Int]
  var exponent = 0

  loads(this, str)

  override def toString(): String = dumps(this, precision - 1)

  def +  (x: Real) = _add(Real(toString()), x)
  def += (x: Real) = _add(this, x)

  def -  (x: Real) = _sub(Real(toString()), x)
  def -= (x: Real) = _sub(this, x)
}