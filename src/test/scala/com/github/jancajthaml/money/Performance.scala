package com.github.jancajthaml.money

import org.scalameter.api.{Measurer, Bench, Gen, exec}

/*
object RegressionResources extends Bench.OfflineReport {
  val times = Gen.range("times")(0, 1000, 200)
  
  override def measurer = new Measurer.MemoryFootprint

  performance of "Real" in {
    measure method "serialize" in {
      using(times) config (
        exec.minWarmupRuns -> 2,
        exec.maxWarmupRuns -> 5,
        exec.benchRuns -> 5,
        exec.independentSamples -> 1
      ) in { sz => { 
        Real("1." + ("0" * sz) +"1")
      } }
    }
  }
}*/

object RealPerformance extends Bench.OfflineReport {
  val times = Gen.range("times")(0, 200, 20)
  //val ref = Real(("0" * 10) + "0." + ("0" * 10) + "1" + ("0" * 10))

  performance of "Real" in {
    measure method "loads" in {
      using(times) config (
        exec.benchRuns -> 20,
        exec.independentSamples -> 1,
        exec.outliers.covMultiplier -> 1.5,
        exec.outliers.suspectPercent -> 40
      ) in { sz => { 
        val v = (("0" * sz ) + "1." + ("0" * (sz * 2)) + "1" + ("0" * sz))
        (0 to 100).foreach { x => Real(v) }
      } }
    }

    /*
    measure method "dumps" in {
      using(times) config (
        exec.benchRuns -> 20,
        exec.independentSamples -> 1,
        exec.outliers.covMultiplier -> 1.5,
        exec.outliers.suspectPercent -> 40
      ) in { sz => { 
      (0 to sz).foreach { x => { ref.toString() } } } }
    }*/

  }

  performance of "BigDecimal" in {
    measure method "loads" in {
      using(times) config (
        exec.benchRuns -> 20,
        exec.independentSamples -> 1,
        exec.outliers.covMultiplier -> 1.5,
        exec.outliers.suspectPercent -> 40
      ) in { sz => { 
        val v = (("0" * sz ) + "1." + ("0" * (sz * 2)) + "1" + ("0" * sz))
        (0 to 100).foreach { x => BigDecimal(v) }
      } }
    }

  }
}