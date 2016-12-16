package com.github.jancajthaml.prototypes

import org.scalameter.api.{Measurer, Bench, Gen, exec}

object SerializationPerformance extends Bench.OfflineReport {
  val run = false
  if (run) {
    val times = Gen.range("times")(0, 10000, 1000)

    val signum = false
    val digits = Array('1','0','0','0','0','0','0','0','0', '0','0','0','0','0','0','0','0','1')
    val exponent = 9

    performance of "toString" in {

      measure method "charArrayCopyInsert" in {
        using(times) config (
          exec.benchRuns -> 20,
          exec.independentSamples -> 1
        ) in { sz => { 
          (0 to sz).foreach { x => toStringPrototypes.charArrayCopyInsert(signum, digits, exponent) }
        } }
      }

      measure method "charArrayCopyTwice" in {
        using(times) config (
          exec.benchRuns -> 20,
          exec.independentSamples -> 1
        ) in { sz => { 
          (0 to sz).foreach { x => toStringPrototypes.charArrayCopyTwice(signum, digits, exponent) }
        } }
      }

      measure method "StringBuilderAppend" in {
        using(times) config (
          exec.benchRuns -> 20,
          exec.independentSamples -> 1
        ) in { sz => { 
          (0 to sz).foreach { x => toStringPrototypes.StringBuilderAppend(signum, digits, exponent) }
        } }
      }

      measure method "StringConcat" in {
        using(times) config (
          exec.benchRuns -> 20,
          exec.independentSamples -> 1
        ) in { sz => { 
          (0 to sz).foreach { x => toStringPrototypes.StringConcat(signum, digits, exponent) }
        } }
      }
    }
  }
}
