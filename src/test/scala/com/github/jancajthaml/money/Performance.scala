package com.github.jancajthaml.money

import org.scalameter.api.{Measurer, Bench, Gen, exec}

/*
object ParsingPerformance extends Bench.OfflineReport {
  val times = Gen.range("times")(0, 1000, 50)

  val ref1 = Real("1" + ("0" * 100) + "0.1")
  val ref2 = BigDecimal("1" + ("0" * 100) + "0.1")

  performance of "Real" in {

    measure method "loads" in {
      using(times) config (
        exec.benchRuns -> 20,
        exec.independentSamples -> 1,
        exec.outliers.covMultiplier -> 1.5,
        exec.outliers.suspectPercent -> 40
      ) in { sz => { 
        val v = ("1" + ("0" * sz ) + "1." + ("0" * (sz * 2)) + "1" + ("0" * sz))
        (0 to 100).foreach { x => Real(v) }
      } }
    }

    measure method "dumps" in {
      using(times) config (
        exec.benchRuns -> 20,
        exec.independentSamples -> 1,
        exec.outliers.covMultiplier -> 1.5,
        exec.outliers.suspectPercent -> 40
      ) in { sz => { 
        (0 to sz).foreach { x => ref1.toString() }
      } }
    }

  }

  performance of "BigDecimal" in {

    measure method "loads" in {
      using(times) config (
        exec.benchRuns -> 20,
        exec.independentSamples -> 1,
        exec.outliers.covMultiplier -> 1.5,
        exec.outliers.suspectPercent -> 40
      ) in { sz => { 
        val v = ("1" + ("0" * sz ) + "1." + ("0" * (sz * 2)) + "1" + ("0" * sz))
        (0 to 100).foreach { x => BigDecimal(v) }
      } }
    }

    measure method "dumps" in {
      using(times) config (
        exec.benchRuns -> 20,
        exec.independentSamples -> 1,
        exec.outliers.covMultiplier -> 1.5,
        exec.outliers.suspectPercent -> 40
      ) in { sz => { 
        (0 to sz).foreach { x => ref2.toString() }
      } }
    }

  }
}
*/