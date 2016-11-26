package com.github.jancajthaml.money

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
