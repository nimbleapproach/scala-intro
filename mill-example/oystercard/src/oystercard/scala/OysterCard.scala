package oystercard.scala

/**
  * A persistent (immutable) data structure representing a user's oyster card.
  *
  * An OysterCard itself is immutable, and can be updating using the DSL-like functions [[touchIn]], [[topUpCard(Int)]] or [[takeTheBus]]
  * if it is being accessed programatically:
  *
  * {{{
  *   val initial : OysterCard = ...
  *   val updatedCard : OysterCard = initial.topUpCard(2000).touchCard(Hammersmith)
  *
  *   // the 'initial' and 'updatedCard' balances will differ as 'initial' is unchanged
  * }}}
  *
  * It can also be updated via the [[update]] function which takes a [[UserEvent]]s.
  *
  * For example, if we had a stream (or observable) of user inputs, perhaps from a message queue or REST endpoint:
  *
  * {{{
  *   val userInputs : Observable[UserEvent] = ... // some user inputs
  *   implicit val calculator : JourneyCalculator = JourneyCalculator()
  *   val updatedBalances : Observable[Amount] = userInputs.foldLeft(OysterCard()) {
  *     case (card, userEvent) => card.update(userEvent)
  *   }.map(_.balance)
  * }}}
  *
  * All actions other than 'topUpCard' require a [[JourneyCalculator]] parameter which is used to provide the fare prices for journeys.
  *
  *
  * @param balance   the current balance
  * @param lastTouch the last tube station touched in if one exists
  */
final case class OysterCard(balance: Amount, lastTouch: Option[TouchIn] = None) {

  /** This function will return a new OysterCard representing the updated state
    *
    * @param event the user event -- this could have been unmarshalled from some POST request or from some input stream
    * @param calculator a calculator used to price journeys
    * @return an updated oyster card
    */
  def update(event: UserEvent)(implicit calculator: JourneyCalculator): OysterCard = {
    event match {
      case TouchCard(station) => touchCard(station)
      case TakeBus => takeTheBus
      case LoadCard(amount) => topUpCard(amount)
    }
  }

  def topUpCard(amount: Amount) = copy(balance = balance + amount)

  def touchCard(station: TubeStation)(implicit calculator: JourneyCalculator): OysterCard = {
    lastTouch match {
      case None => touchIn(station)
      case Some(lastTouch) => touchOut(lastTouch, station)
    }
  }

  def takeTheBus(implicit calculator: JourneyCalculator): OysterCard = copy(balance = balance - calculator.busFare)

  // useful for tests - updates a card w/ all the given events
  def updateAll(events: Iterable[UserEvent])(implicit calculator: JourneyCalculator): OysterCard = {
    events.foldLeft(this) {
      case (card, action) => card.update(action)
    }
  }

  private def touchIn(station: TubeStation)(implicit calculator: JourneyCalculator): OysterCard = {
    copy(balance = balance - calculator.maxFare, lastTouch = Option(TouchIn(station, calculator.maxFare)))
  }

  private def touchOut(stationTouchedIn: TouchIn, stationTouchedOut: TubeStation)(implicit calculator: JourneyCalculator): OysterCard = {
    val newBalance = balance + stationTouchedIn.fareCharged - calculator.price(stationTouchedIn.at, stationTouchedOut)
    copy(balance = newBalance, lastTouch = None)
  }

}

object OysterCard {
  def apply() = new OysterCard(Amount.Zero, None)
}