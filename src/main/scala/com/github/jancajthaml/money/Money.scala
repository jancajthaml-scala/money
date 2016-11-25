package com.github.jancajthaml.money

import scala.language.implicitConversions

/**
  * @author jan.cajthaml
  */
object PimpMoney {

  class MoneyMath(l: Money) extends Comparable[Money] {

    val add = (r: Money) => this.+(r)

    def +(r: Money): Money = {
      val ret = l.amount.add(check(r).amount)
      Money(ret, l.currency)
    }

    val substract = (r: Money) => this.-(r)

    def -(r: Money): Money = {
      val ret = l.amount.subtract(check(r).amount)
      Money(ret, l.currency)
    }

    val multiply = (r: Money) => this.*(r)

    def *(r: Money): Money = {
      val ret = l.amount.multiply(check(r).amount)
      Money(ret, l.currency)
    }

    val divide = (r: Money) => this./(r)

    def /(r: Money): Money = {
      val ret = l.amount.divide(check(r).amount)
      Money(ret, l.currency)
    }

    def unary_- : Money = Money(this.l.amount.negate(), this.l.currency)

    def <=(r: Money): Boolean = compare(r) <= 0

    def >=(r: Money): Boolean = compare(r) >= 0

    def <(r: Money): Boolean = compare(r) < 0

    def >(r: Money): Boolean = compare(r) > 0

    val compare = (r: Money) => compareTo(r)

    override def compareTo(r: Money): Int = {
      if (l.currency != r.currency) {
        throw new UnsupportedOperationException(s"cannot compareTo ${this} with ${r}")
      } else l.amount.compareTo(r.amount)
    }

    private def check(r: Money) = if (l.currency != r.currency) {
      throw new UnsupportedOperationException(s"${l} and ${r} are of different currencies")
    } else r
  }

  implicit def pimp(p: Money) = new MoneyMath(p)
}

/**
  * The money representation
  *
  * @author jan.cajthaml
  *
  * @param amount
  * @param currency
  */
case class Money(amount: RealNumber, val currency: String)
