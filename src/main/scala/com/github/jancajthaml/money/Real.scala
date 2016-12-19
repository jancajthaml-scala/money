package com.github.jancajthaml.money

import Real._

import TypelessMath.{plus, minus}

//import scala.collection.mutable.{ListBuffer => Buffer}
//import sun.misc.Unsafe

object Real {

  def loads(x: Real): Unit = {
    if (x.value == "0") {
      x.signum = false
      x.exponent = 1
      x.digits = Array('0', '0')
      x.value = "0.0"
    }

    val characters = x.value.toCharArray()
    var swap: Array[Char] = null
    val len = characters.length

    if (characters(0) == '-') {
      var i = 1
      while (i < len) {
        if (characters(i) == '.') {
          val left = i
          val right = characters.length - i - 2
          swap = Array.ofDim[Char](left + right)
          System.arraycopy(characters, 1, swap, 0, left - 1)
          System.arraycopy(characters, left + 1, swap, left - 1, right + 1)
          x.signum = true
          x.exponent = i - 1
          x.digits = swap
          return ()
        }
        i += 1
      }
      swap = Array.ofDim[Char](characters.length)
      swap(swap.length - 1) = '0'
      System.arraycopy(characters, 1, swap, 0, characters.length - 1)
      x.signum = true
      x.exponent = swap.length - 1
      x.digits = swap
      ()
    } else {
      var i = 0
      while (i < len) {
        if (characters(i) == '.') {
          val left = i
          val right = characters.length - i - 1
          swap = Array.ofDim[Char](left + right)
          System.arraycopy(characters, 0, swap, 0, left)
          System.arraycopy(characters, left + 1, swap, left, right)
          x.signum = false
          x.exponent = i
          x.digits = swap
          x.value = dumps(x)
          return ()
        }
        i += 1
      }
      swap = Array.ofDim[Char](characters.length + 1)
      swap(swap.length - 1) = '0'
      System.arraycopy(characters, 0, swap, 0, characters.length)
      x.signum = false
      x.exponent = swap.length - 1
      x.digits = swap
      x.value = dumps(x)
      ()
    }
  }

  def dumps(x: Real) = {
    val swap = if (x.signum) {
      val buf = Array.ofDim[Char](x.digits.length + 2)
      System.arraycopy(x.digits, 0, buf, 1, x.exponent)
      System.arraycopy(x.digits, x.exponent - 1, buf, x.exponent + 1, x.digits.length - x.exponent + 1)
      buf(0) = '-'
      buf
    } else {
      val buf = Array.ofDim[Char](x.digits.length + 1)
      System.arraycopy(x.digits, 0, buf, 0, x.exponent)
      System.arraycopy(x.digits, x.exponent - 1, buf, x.exponent, x.digits.length - x.exponent + 1)
      buf
    }
    swap(x.exponent) = '.'
    new String(swap, 0, swap.length)
  }

  private def _add(l: Real, r: Real) = {
    val native = plus(l.signum, l.digits, l.exponent, r.signum, r.digits, r.exponent)
    l.signum = native(0).asInstanceOf[Boolean]
    l.exponent = native(1).asInstanceOf[Int]
    l.digits = native(2).asInstanceOf[Array[Char]]
    l.value = dumps(l)
    l
  }

  private def _sub(l: Real, r: Real) = {
    val native = minus(l.signum, l.digits, l.exponent, r.signum, r.digits, r.exponent)
    l.signum = native(0).asInstanceOf[Boolean]
    l.exponent = native(1).asInstanceOf[Int]
    l.digits = native(2).asInstanceOf[Array[Char]]
    l.value = dumps(l)
    l
  }

}

case class Real(var value: String) extends Cloneable with Comparable[Real] {

  var signum = false
  var exponent = 0
  var digits = Array.empty[Char]

  loads(this)

  override def toString() = value

  override def compareTo(r: Real) = ???

  def +  (r: Real) = _add(super.clone().asInstanceOf[Real], r)
  def += (r: Real) = _add(this, r)

  def -  (r: Real) = _sub(super.clone().asInstanceOf[Real], r)
  def -= (r: Real) = _sub(this, r)


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