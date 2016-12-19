package com.github.jancajthaml.money

import org.scalameter.api.{Measurer, Bench, Gen, exec}

/*
object SubtractionPerformance extends Bench.OfflineReport {
  val times = Gen.range("times")(0, 1000, 200)

  val ref1 = Real("1" + ("0" * 100) + "0.1")
  val ref2 = BigDecimal("1" + ("0" * 100) + "0.1")

  performance of "Real" in {

    measure method "subtract" in {
      using(times) in { sz => { 
        val a = Real("1" + ("0" * sz ) + "1." + ("0" * (sz * 2)) + "1" + ("0" * sz))
        val b = Real("1" + ("0" * sz ) + "1." + ("0" * (sz * 2)) + "1" + ("0" * sz))

        val as = a.toString()
        val bs = b.toString()
        (0 to 100).foreach { x => (a - b) }
      } }
    }
  }

  performance of "BigDecimal" in {

    measure method "subtract" in {
      using(times) in { sz => { 
        val a = BigDecimal("1" + ("0" * sz ) + "1." + ("0" * (sz * 2)) + "1" + ("0" * sz))
        val b = BigDecimal("1" + ("0" * sz ) + "1." + ("0" * (sz * 2)) + "1" + ("0" * sz))

        val as = a.underlying.toPlainString()
        val bs = b.underlying.toPlainString()

        (0 to 100).foreach { x => (a - b).underlying.toPlainString() }
      } }
    }
  }
}
*/