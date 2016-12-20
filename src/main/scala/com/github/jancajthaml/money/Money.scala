package com.github.jancajthaml.money

//import com.github.jancajthaml.number.RealNumber

import Money._

object Money {

  private def assertUnderlying(x: Money) = {
    if (x.underlying == null) {
      x.underlying = new java.math.BigDecimal(x.repr, java.math.MathContext.DECIMAL128)
    }
  }

  private def _add(l: Money, r: Money) = {
    l.dirty = true
    l.underlying = l.underlying.add(r.underlying)
    l
  }

  private def _sub(l: Money, r: Money) = {
    l.dirty = true
    l.underlying = l.underlying.subtract(r.underlying)
    l
  }

  private def _mul(l: Money, r: Money) = {
    l.dirty = true
    l.underlying = l.underlying.multiply(r.underlying)
    l
  }

  private def _div(l: Money, r: Money) = {
    l.dirty = true
    l.underlying = l.underlying.divide(r.underlying)
    l
  }
}

case class Money(private var repr: String, private val currency: String) extends Cloneable with Comparable[Money] {

  var dirty = false

  def value() = {
    if (dirty) {
      underlying = underlying.stripTrailingZeros()
      repr = underlying.toPlainString()
      dirty = false
    }
    repr
  }

  var underlying: java.math.BigDecimal = null

  override def toString() = repr

  override def compareTo(r: Money) = {
    if (currency != r.currency) {
      throw new UnsupportedOperationException(s"cannot compareTo ${this} with ${r}")
    } else {
      assertUnderlying(this)
      assertUnderlying(r)
      underlying.compareTo(r.underlying)
    }
  }

  private def check(r: Money) = {
    assertUnderlying(this)
    assertUnderlying(r)
    if (currency != r.currency) {
      throw new UnsupportedOperationException(s"${this} and ${r} are of different currencies")
    } else r
  }

  val compare = (r: Money) => compareTo(r)

  def <=(r: Money): Boolean = compare(r) <= 0
  def <(r: Money): Boolean = compare(r) < 0

  def >=(r: Money): Boolean = compare(r) >= 0
  def >(r: Money): Boolean = compare(r) > 0

  def +=(r: Money) = { check(r); _add(this, r) }
  def +(r: Money) = super.clone().asInstanceOf[Money] += r

  def -=(r: Money) = { check(r); _sub(this, r) }
  def -(r: Money) = super.clone().asInstanceOf[Money] -= r

  def *=(r: Money) = { check(r); _mul(this, r) }
  def *(r: Money) = super.clone().asInstanceOf[Money] *= r

  def /=(r: Money) = { check(r); _div(this, r) }
  def /(r: Money) = super.clone().asInstanceOf[Money] /= r

  def unary_- = {
    assertUnderlying(this)
    val x = super.clone().asInstanceOf[Money]
    x.underlying = x.underlying.negate()
    x
  }
}