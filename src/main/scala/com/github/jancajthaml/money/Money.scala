package com.github.jancajthaml.money

/**
  * The money representation
  *
  * @author jan.cajthaml
  *
  * @param amount
  * @param currency
  */
case class Money(val amount: Real, val currency: String) extends Comparable[Money] {

  val add = (r: Money) => this.+(r)

  def +(r: Money): Money = {
    val ret = amount + (check(r).amount)
    Money(ret, currency)
  }

  /*
  val substract = (r: Money) => this.-(r)

  def -(r: Money): Money = {
    val ret = amount - (check(r).amount)
    Money(ret, currency)
  }
  */

  /*
  val multiply = (r: Money) => this.*(r)

  def *(r: Money): Money = {
    val ret = amount * (check(r).amount)
    Money(ret, currency)
  }
  */

  /*
  val divide = (r: Money) => this./(r)

  def /(r: Money): Money = {
    val ret = amount / (check(r).amount)
    Money(ret, currency)
  }
  */

  def unary_- : Money = Money(-this.amount, this.currency)

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
}
