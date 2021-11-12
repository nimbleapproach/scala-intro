package oystercard.scala

import org.scalatest.{Matchers, WordSpec}
import oystercard.scala.TubeStation._

/**
  * This demos the OysterCard itself.
  *
  * By only exposing valid methods on certain states and using strongly-typed inputs/outputs with checked params,
  * we should have less to test
  */
class OysterCardTest extends WordSpec with Matchers {

  type Actions = List[UserEvent]
  type ExpectedBalance = Amount
  type Scenario = (Actions, ExpectedBalance)

  "OysterCard.touchCard" should {
    "Initially charge the max fare" in {
      implicit object EverythingIsAFiver extends JourneyCalculator {
        override def maxFare: ExpectedBalance = Amount.inPounds(5)
        override def price(from: TubeStation, to: TubeStation): ExpectedBalance = Amount.inPounds(5)
        override def busFare: ExpectedBalance = Amount.inPounds(5)
      }
      val initial = OysterCard()
      initial.balance shouldBe Amount.Zero

      initial.touchCard(Hammersmith).balance shouldBe Amount.inPence(-500)
      initial.topUpCard(Amount.inPounds(20)).touchCard(Hammersmith).balance shouldBe Amount.inPounds(15)
      withClue("The original card should be unchanged") {
        initial.balance shouldBe Amount.Zero
      }
    }
  }
  "OysterCard.update" should {

    // out test scenarios - user events and expected balances
    val scenarios: List[Scenario] = List(
      Nil -> Amount.Zero,
      List(LoadCard(Amount.inPence(123))) -> Amount.inPence(123),
      List(LoadCard(Amount.inPounds(123)), LoadCard(Amount.inPence(456))) -> Amount.inPence(12756),
      //
      // load, touch in
      List(
        LoadCard(Amount.inPounds(30)), // load the card w/ £30,
        TouchCard(Holborn) // start a journey, which should be charged at max fare of £3.20
      ) -> Amount.inPence(2680),
      //
      // load, touch in/out
      List(
        LoadCard(Amount.inPounds(30)), // load the card w/ £30,
        TouchCard(Holborn), // start a journey, which should be charged at max fare of £3.20
        TouchCard(EarlsCourt) // touch out, so have the £0.70 refunded as anywhere in zone 1 is only £2.50
      ) -> Amount.inPence(2750),
      //
      // load, takes the bus
      List(
        LoadCard(Amount.inPence(190)),
        TakeBus // £1.80, so 0.10 remaining
      ) -> Amount.inPence(10),

      // the specified test scenario
      List(
        LoadCard(Amount.inPounds(30)), // load the card w/ £30,
        TouchCard(Holborn), TouchCard(EarlsCourt), // take Tube Holborn to Earl’s Court, which is anywhere in zone 1, e.g. £30 - £2.50 = £27.50
        TakeBus, // 328 bus from Earl’s Court to Chelsea, e.g. £27.50 - £1.80 bus fare or £25.70
        TouchCard(EarlsCourt), TouchCard(Hammersmith) // Tube Earl’s court to Hammersmith, which is any 2 zones including 1, e.g. £25.70 - £3 = £22.70
      ) -> Amount.inPence(2270)
    )

    scenarios.foreach {
      case (actions, expected) =>
        s"Have a balance of $expected after a user ${pretty(actions)}" in {
          implicit val calculator = JourneyCalculator()
          OysterCard().updateAll(actions).balance shouldBe expected
        }
    }

  }


  def fmt(action: UserEvent) = action match {
    case TouchCard(station) => s"touches the card at '$station'"
    case TakeBus => "takes the bus"
    case LoadCard(amount) => s"loads ${amount} onto the card"
  }

  def pretty(actions: Actions) = {
    actions match {
      case Nil => "does nothing"
      case head :: Nil => fmt(head)
      case many => many.init.map(fmt).mkString("", ", ", s" and then ${fmt(many.last)}")
    }
  }
}
