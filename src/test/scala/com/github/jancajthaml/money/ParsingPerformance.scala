package com.github.jancajthaml.money

import org.scalameter.api.{Measurer, Bench, Gen, exec}

object ParsingPerformance extends Bench.OfflineReport {
  val times = Gen.range("times")(0, 10000, 1000)

  performance of "Money" in {
    measure method "construct + compact + serialize" in {
      using(times) config (
        exec.minWarmupRuns -> 10,
        exec.maxWarmupRuns -> 10,
        exec.benchRuns -> 20,
        exec.independentSamples -> 1
      ) in { sz => { 
        (0 to sz).foreach { x => Money("10000000.00000001", "EUR").toString() }
      } }
    }
  }

  performance of "scala.math.BigDecimal" in {
    measure method "construct + serialize" in {
      using(times) config (
        exec.minWarmupRuns -> 10,
        exec.maxWarmupRuns -> 10,
        exec.benchRuns -> 20,
        exec.independentSamples -> 1
      ) in { sz => { 
        (0 to sz).foreach { x => BigDecimal("10000000.00000001").underlying.toPlainString() }
      } }
    }
  }

  performance of "java.math.BigDecimal" in {
    measure method "construct + serialize" in {
      using(times) config (
        exec.minWarmupRuns -> 10,
        exec.maxWarmupRuns -> 10,
        exec.benchRuns -> 20,
        exec.independentSamples -> 1
      ) in { sz => { 
        (0 to sz).foreach { x => new java.math.BigDecimal("10000000.00000001").toPlainString() }
      } }
    }
  }

}