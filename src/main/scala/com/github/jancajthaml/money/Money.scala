package com.github.jancajthaml.money

import scala.language.implicitConversions

/**
  * @author jan.cajthaml
  */
//object Money

// TODO/FIXME remove RealNumber and use PreciseNumber instead

/**
  * The money representation
  *
  * @author jan.cajthaml
  *
  * @param amount
  * @param currency
  */
case class Money(val amount: PreciseNumber, val currency: String) extends Comparable[Money] {

  //class MoneyMath(l: Money) extends Comparable[Money] {

  val add = (r: Money) => this.+(r)

  def +(r: Money): Money = {
    val ret = amount.add(check(r).amount)
    Money(ret, currency)
  }

  val substract = (r: Money) => this.-(r)

  def -(r: Money): Money = {
    val ret = amount.subtract(check(r).amount)
    Money(ret, currency)
  }

  val multiply = (r: Money) => this.*(r)

  def *(r: Money): Money = {
    val ret = amount.multiply(check(r).amount)
    Money(ret, currency)
  }

  val divide = (r: Money) => this./(r)

  def /(r: Money): Money = {
    val ret = amount.divide(check(r).amount)
    Money(ret, currency)
  }

  def unary_- : Money = Money(this.amount.negate(), this.currency)

  def <=(r: Money): Boolean = compare(r) <= 0

  def >=(r: Money): Boolean = compare(r) >= 0

  def <(r: Money): Boolean = compare(r) < 0

  def >(r: Money): Boolean = compare(r) > 0

  val compare = (r: Money) => compareTo(r)

  override def compareTo(r: Money): Int = {
    if (currency != r.currency) {
      throw new UnsupportedOperationException(s"cannot compareTo ${this} with ${r}")
    } else amount.compareTo(r.amount)
  }

  private def check(r: Money) = if (currency != r.currency) {
    throw new UnsupportedOperationException(s"${this} and ${r} are of different currencies")
  } else r
  //}

  //implicit def pimp(p: Money) = new MoneyMath(p)
}
