package com.github.jancajthaml.money

import org.scalameter.api.{Measurer, Bench, Gen, exec}

/*
object ParsingPerformance extends Bench.OfflineReport {
  val times = Gen.range("times")(0, 10000, 500)

  //val ref1 = Real("1" + ("0" * 100) + "0.1")
  //val ref2 = BigDecimal("1" + ("0" * 100) + "0.1")

  performance of "Real" in {

    measure method "serialization" in {
      using(times) in { sz => { 
        (0 to sz).foreach { x => Real("10000000.00000001").toString() }
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
        (0 to sz).foreach { x => ref1.toString() }
      } }
    }
    */
  }

  performance of "BigDecimal" in {

    measure method "serialization" in {
      using(times) in { sz => { 
        (0 to sz).foreach { x => BigDecimal("10000000.00000001").underlying.toPlainString() }
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
        (0 to sz).foreach { x => ref2.toString() }
      } }
    }
  */
  }
}
*/

/*

object AdditionPerformance extends Bench.OfflineReport {
  val times = Gen.range("times")(0, 1000, 200)

  val ref1 = Real("1" + ("0" * 100) + "0.1")
  val ref2 = BigDecimal("1" + ("0" * 100) + "0.1")

  performance of "Real" in {

    measure method "add" in {
      using(times) in { sz => { 
        val a = Real("1" + ("0" * sz ) + "1." + ("0" * (sz * 2)) + "1" + ("0" * sz))
        val b = Real("1" + ("0" * sz ) + "1." + ("0" * (sz * 2)) + "1" + ("0" * sz))

        val as = a.toString()
        val bs = b.toString()
        (0 to 100).foreach { x => (a + b) }
      } }
    }
  }

  performance of "BigDecimal" in {

    measure method "add" in {
      using(times) in { sz => { 
        val a = BigDecimal("1" + ("0" * sz ) + "1." + ("0" * (sz * 2)) + "1" + ("0" * sz))
        val b = BigDecimal("1" + ("0" * sz ) + "1." + ("0" * (sz * 2)) + "1" + ("0" * sz))

        val as = a.underlying.toPlainString()
        val bs = b.underlying.toPlainString()
        (0 to 100).foreach { x => (a + b) }
      } }
    }
  }
} 

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