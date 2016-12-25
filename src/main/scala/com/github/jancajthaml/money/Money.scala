package com.github.jancajthaml.money

import Money._

import java.math.{BigDecimal => BigDec, MathContext}

object Money {

  private def assertUnderlying(x: Money) = {
    if (x.underlying == null) {
      x.underlying = new BigDec(x.repr, MathContext.DECIMAL128)
    }
  }

  private def add(l: Money, r: Money) = {
    l.dirty = true
    l.underlying = l.underlying.add(r.underlying)
    l
  }

  private def sub(l: Money, r: Money) = {
    l.dirty = true
    l.underlying = l.underlying.subtract(r.underlying)
    l
  }

  private def mul(l: Money, r: Money) = {
    l.dirty = true
    l.underlying = l.underlying.multiply(r.underlying)
    l
  }

  private def div(l: Money, r: Money) = {
    l.dirty = true
    l.underlying = l.underlying.divide(r.underlying)
    l
  }
}

case class Money(private var repr: String, val currency: String) extends Comparable[Money] {

  private var dirty = false

  private var underlying: BigDec = null

  def value() = {
    if (dirty) {
      underlying = underlying.stripTrailingZeros()
      repr = underlying.toPlainString()
      dirty = false
    }
    repr
  }

  override def toString() = value

  override def compareTo(r: Money) = {
    if (currency != r.currency) {
      throw new UnsupportedOperationException(s"cannot compareTo ${this} with ${r}")
    } else {
      assertUnderlying(this)
      assertUnderlying(r)
      underlying.compareTo(r.underlying)
    }
  }

  def compareTo(r: BigDecimal) = {
    assertUnderlying(this)
    underlying.compareTo(r.underlying)
  }

  private def check(r: Money) = {
    assertUnderlying(this)
    assertUnderlying(r)
    if (currency != r.currency) {
      throw new UnsupportedOperationException(s"${this} and ${r} are of different currencies")
    } else r
  }

  val compare = (r: Money) => compareTo(r)

  def <=(r: Money) = compare(r) <= 0
  def <(r: Money) = compare(r) < 0

  def >=(r: Money) = compare(r) >= 0
  def >(r: Money) = compare(r) > 0

  def +(r: Money) = {
    val l = Money(value(), currency)
    check(l)
    check(r)
    add(l, r)
  }
  def -(r: Money) = {
    val l = Money(value(), currency)
    check(l)
    check(r)
    sub(l, r)
  }
  def *(r: Money) = {
    val l = Money(value(), currency)
    check(l)
    check(r)
    mul(l, r)
  }
  def /(r: Money) = {
    val l = Money(value(), currency)
    check(l)
    check(r)
    div(l, r)
  }

  def unary_- = {
    val x = Money(value(), currency)
    x.underlying = new BigDec(x.repr, MathContext.DECIMAL128).negate().stripTrailingZeros()
    x.repr = x.underlying.toPlainString()
    x.dirty = false
    x
  }
}