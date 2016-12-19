package com.github.jancajthaml.money

import Real._

object Real {

  private def assertUnderlying(x: Real) = {
    if (x.underlying == null) {
      x.underlying = new java.math.BigDecimal(x.repr)
    }
  }

  private def _add(l: Real, r: Real) = {
    l.underlying = l.underlying.add(r.underlying)
    l.repr = l.underlying.toPlainString()
    l
  }

  private def _sub(l: Real, r: Real) = {
    l.underlying = l.underlying.subtract(r.underlying)
    l.repr = l.underlying.toPlainString()
    l
  }

  private def _mul(l: Real, r: Real) = {
    l.underlying = l.underlying.multiply(r.underlying)
    l.repr = l.underlying.toPlainString()
    l
  }

  private def _div(l: Real, r: Real) = {
    l.underlying = l.underlying.divide(r.underlying)
    l.repr = l.underlying.toPlainString()
    l
  }
}

case class Real(private var repr: String, private val currency: String) extends Cloneable with Comparable[Real] {

  def value() = repr

  var underlying: java.math.BigDecimal = null

  override def toString() = repr

  override def compareTo(r: Real) = {
    if (currency != r.currency) {
      throw new UnsupportedOperationException(s"cannot compareTo ${this} with ${r}")
    } else {
      assertUnderlying(this)
      assertUnderlying(r)
      underlying.compareTo(r.underlying)
    }
  }

  private def check(r: Real) = {
    assertUnderlying(this)
    assertUnderlying(r)
    if (currency != r.currency) {
      throw new UnsupportedOperationException(s"${this} and ${r} are of different currencies")
    } else r
  }

  val compare = (r: Real) => compareTo(r)

  def <=(r: Real): Boolean = compare(r) <= 0
  def <(r: Real): Boolean = compare(r) < 0

  def >=(r: Real): Boolean = compare(r) >= 0
  def >(r: Real): Boolean = compare(r) > 0

  def += (r: Real) = { check(r); _add(this, r) }
  def +  (r: Real) = super.clone().asInstanceOf[Real] += r

  def -= (r: Real) = { check(r); _sub(this, r) }
  def -  (r: Real) = super.clone().asInstanceOf[Real] -= r

  def *= (r: Real) = { check(r); _mul(this, r) }
  def *  (r: Real) = super.clone().asInstanceOf[Real] *= r

  def /= (r: Real) = { check(r); _div(this, r) }
  def /  (r: Real) = super.clone().asInstanceOf[Real] /= r

  def unary_- = {
    assertUnderlying(this)
    val x = super.clone().asInstanceOf[Real]
    x.underlying = x.underlying.negate()
    x
  }
}