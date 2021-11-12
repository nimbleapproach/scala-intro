package oystercard.scala

import java.util.Locale

import org.scalatest.{Matchers, WordSpec}

class AmountTest extends WordSpec with Matchers {

  "Amount.show" should {
    "format pence properly" in {

      Amount.inPounds(3).show(Locale.UK) shouldBe "£3.00"
      Amount.inPence(123).show(Locale.UK) shouldBe "£1.23"
      Amount.inPence(123456).show(Locale.UK) shouldBe "£1,234.56"
      Amount.inPence(1).show(Locale.UK) shouldBe "£0.01"
      Amount.inPence(-1).show(Locale.UK) shouldBe "-£0.01"
    }
  }
}
