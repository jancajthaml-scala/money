package com.github.jancajthaml.money

class Complex(private var re: Double, private var im: Double) extends Number { // with Cloneable {

  def set(re: Double, im: Double) {
    this.re = re
    this.im = im
  }

  def real(): Double = re

  def aimag(): Double = im

  def arg(): Double = Math.atan2(im, re)

  def conjg(): Complex = new Complex(re, -im)

  def add(r: Complex): Complex = new Complex(re + r.re, im + r.im)

  def negate(): Complex = new Complex(-re, -im)

  def subtract(r: Complex): Complex = new Complex(re - r.re, im - r.im)

  def multiply(r: Complex): Complex = new Complex(re * r.re - im * r.im, re * r.im + im * r.re)

  def divide(r: Complex): Complex = {
    val denom = r.re * r.re + r.im * r.im
    new Complex((re * r.re + im * r.im) / denom, (im * r.re - re * r.im) / denom)
  }

  override def equals(r: Any): Boolean = {
    try {
      (re == r.asInstanceOf[Complex].re && im == r.asInstanceOf[Complex].im)
    } catch {
      case e: ClassCastException => false
    }
  }

  // TODO/FIXME delete and remove Clonable interface
  //override def clone(): AnyRef = new Complex(re, im)

  override def hashCode(): Int = (new java.lang.Double(re / 2 + im / 2)).hashCode

  override def byteValue(): Byte = re.toByte

  def doubleValue(): Double = re

  def floatValue(): Float = re.toFloat

  def intValue(): Int = re.toInt

  def longValue(): Long = re.toLong

  override def shortValue(): Short = re.toShort
}
