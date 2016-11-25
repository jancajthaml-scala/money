package com.github.jancajthaml.money

/**
  * @author jan.cajthaml
  */
object Chunk {

  def dpdec(a: Chunk, b: Chunk) {
    if (a.a != 0.0) {
      val t1 = 0.3010299956639812 * a.n + (Math.log(Math.abs(a.a)) / 2.302585092994046)
      b.n = t1.toInt
      if (t1 < 0.0) {
        b.n -= 1
      }
      val sig = Math.pow(10.0, (t1 - b.n))
      b.a = (if (a.a >= 0) Math.abs(sig) else -Math.abs(sig))
    } else {
      b.a = 0.0
      b.n = 0
    }
  }
}

/**
  * @author jan.cajthaml
  */
class Chunk(var a: Double, var n: Int){

  def value(): Double = a * Math.pow(2, n)

  // INFO below transient getters and setter for private modifier bypass in java/scala hybrid code
  def ga() = a
  def gn() = n
  def sa(_a: Double) = a = _a
  def sn(_n: Int) = n = _n
}
