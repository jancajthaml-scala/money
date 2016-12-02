package com.github.jancajthaml.money

import org.scalameter.api.{Measurer, Bench, Gen, exec}

object RegressionResources extends Bench.OfflineReport {

  override def measurer = new Measurer.MemoryFootprint

  val times = Gen.range("times")(0, 10, 2)
  var i = 0

  performance of "Real" in {
    measure method "serialize" in {
      using(times) config (
        exec.minWarmupRuns -> 2,
        exec.maxWarmupRuns -> 5,
        exec.benchRuns -> 5,
        exec.independentSamples -> 1
      ) in { sz => { (0 to sz).foreach { x => {
        i += 1
        Real("1." + ("0" * i) +"1")
      } } } }
    }
  }
}

object RegressionPerformance extends Bench.OfflineReport {

  val times = Gen.range("times")(0, 10, 2)
  var i = 0

  performance of "Real" in {
    measure method "serialize" in {
      using(times) config (
        exec.benchRuns -> 20,
        exec.independentSamples -> 1,
        exec.outliers.covMultiplier -> 1.5,
        exec.outliers.suspectPercent -> 40
      ) in { sz => { (0 to sz).foreach { x => {
        i += 1
        Real("1." + ("0" * i) +"1")
      } } } }
    }
  }

}