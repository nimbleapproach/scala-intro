package oystercard.scala

import org.scalatest.{Matchers, WordSpec}

class JourneyCalculatorTest extends WordSpec with Matchers {

  val calculator = JourneyCalculator()
  "JourneyCalculator.maxFare" should {
    "be 3.20 by default" in {
      calculator.maxFare shouldBe Amount.inPence(320)
    }
  }
  "JourneyCalculator.price" should {
    import TubeStation._

    "price a journey between holborn to earl's court at the best price of £2.50" in {
      calculator.price(Holborn, EarlsCourt) shouldBe Amount.inPence(250)
    }

    "price any one zone outside zone 1" in {
      calculator.price(TubeStation("foo", 6), TubeStation("bar", 6)) shouldBe Amount.inPounds(2)
    }
    "price any two zones including zone 1" in {
      calculator.price(TubeStation("one", 1), TubeStation("three", 3)) shouldBe Amount.inPounds(3)
    }
    "price any two zones excluding zone 1" in {
      calculator.price(TubeStation("two", 2), TubeStation("three", 3)) shouldBe Amount.inPence(225)
    }
    "price any three zones" in {
      calculator.price(TubeStation("two and three", 2, 3), TubeStation("four", 4)) shouldBe Amount.inPence(320)
    }
    "price a bus journey" in {
      calculator.busFare shouldBe Amount.inPence(180)
    }
  }

}
