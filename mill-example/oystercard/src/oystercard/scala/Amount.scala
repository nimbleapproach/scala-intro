package oystercard.scala

import java.util.Locale

class Amount private(val valueInPence: Int) {
  def -(other: Amount): Amount = Amount.inPence(valueInPence - other.valueInPence)

  def +(other: Amount): Amount = Amount.inPence(valueInPence + other.valueInPence)

  override def toString: String = show(Locale.getDefault)

  def show(locale: Locale): String = Amount.fmt(valueInPence, locale)

  override def hashCode: Zone = valueInPence

  override def equals(other: Any) = other match {
    case Amount(pence) => valueInPence == pence
    case _ => false
  }
}

object Amount {
  val Zero = inPence(0)

  def unapply(amount: Amount) = Option(amount.valueInPence)

  def inPence(pence: Int): Amount = new Amount(pence)

  def inPounds(pounds: Int): Amount = inPence(pounds * 100)

  def fmt(valueInPence: Int, locale: Locale): String = java.text.NumberFormat.getCurrencyInstance(locale).format(valueInPence / 100.0)
}