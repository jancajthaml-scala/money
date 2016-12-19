package com.github.jancajthaml.money

import org.scalameter.api.{Measurer, Bench, Gen, exec}
/*
object ParsingPerformance extends Bench.OfflineReport {
  val times = Gen.range("times")(0, 10000, 500)

  performance of "Real" in {
    measure method "serialization" in {
      using(times) in { sz => { 
        (0 to sz).foreach { x => Real("10000000.00000001").toString() }
      } }
    }
  }

  performance of "BigDecimal" in {
    measure method "serialization" in {
      using(times) in { sz => { 
        (0 to sz).foreach { x => BigDecimal("10000000.00000001").underlying.toPlainString() }
      } }
    }
  }
}
*/