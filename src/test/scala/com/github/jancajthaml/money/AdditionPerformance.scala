package com.github.jancajthaml.money

import org.scalameter.api.{Measurer, Bench, Gen, exec}


object AdditionPerformance extends Bench.OfflineReport {
  val times = Gen.range("times")(0, 10000, 1000)

  performance of "addition" in {

    measure method "Money.add" in {
      using(times) config (
        exec.minWarmupRuns -> 10,
        exec.maxWarmupRuns -> 10,
        exec.benchRuns -> 20,
        exec.independentSamples -> 1
      ) in { sz => { 
        val a = Money("10000.00001", "EUR")
        val b = Money("0.0000000957", "EUR")

        val as = a.toString()
        val bs = b.toString()
        (0 to 100).foreach { x => (a + b)toString() }
      } }
    }

    measure method "scala.math.BigDecimal.add" in {
      using(times) config (
        exec.minWarmupRuns -> 10,
        exec.maxWarmupRuns -> 10,
        exec.benchRuns -> 20,
        exec.independentSamples -> 1
      ) in { sz => { 
        val a = BigDecimal("10000.00001")
        val b = BigDecimal("0.0000000957")

        val as = a.underlying.toPlainString()
        val bs = b.underlying.toPlainString()
        (0 to 100).foreach { x => (a + b).underlying.toPlainString() }
      } }
    }

    measure method "java.math.BigDecimal.add" in {
      using(times) config (
        exec.minWarmupRuns -> 10,
        exec.maxWarmupRuns -> 10,
        exec.benchRuns -> 20,
        exec.independentSamples -> 1
      ) in { sz => { 
        val a = new java.math.BigDecimal("10000.00001")
        val b = new java.math.BigDecimal("0.0000000957")

        val as = a.toPlainString()
        val bs = b.toPlainString()
        (0 to 100).foreach { x => a.add(b).toPlainString() }
      } }
    }
  }
}
