package com.github.jancajthaml.money

import Real._

import TypelessMath.{plus, minus}

object Real {

  private val trailingDecimal = ".0".intern

  def normalize(x: Real) = {
    val decimal = x.value.indexOf('.')

    if (x.value.charAt(0) == '-') {
      x.signum = true
      if (decimal > -1) {
        x.decimal = decimal - 1
      } else {
        x.decimal = x.value.length() - 1
        x.value += trailingDecimal
      }
    } else {
      x.signum = false
      if (decimal > -1) {
        x.decimal = decimal
      } else {
        x.decimal = x.value.length()
        x.value += trailingDecimal
      }
    }
  }

  private def _add(l: Real, r: Real) = {
    val native = plus(l.signum, l.value, l.decimal, r.signum, r.value, r.decimal)
    l.signum = native(0).asInstanceOf[Boolean]
    l.decimal = native(1).asInstanceOf[Int]
    l.value = native(2).asInstanceOf[String]
    l
  }

  private def _sub(l: Real, r: Real) = {
    val native = minus(l.signum, l.value, l.decimal, r.signum, r.value, r.decimal)
    l.signum = native(0).asInstanceOf[Boolean]
    l.decimal = native(1).asInstanceOf[Int]
    l.value = native(2).asInstanceOf[String]
    l
  }
}

case class Real(var value: String) extends Cloneable with Comparable[Real] {

  var signum = false
  var decimal = -1

  normalize(this)

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