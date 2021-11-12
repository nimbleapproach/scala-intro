package oystercard.scala

/** Represents the valid actions an oyster card user can take
  */
sealed trait UserEvent

case class TouchCard(station: TubeStation) extends UserEvent

case object TakeBus extends UserEvent

case class LoadCard(amount: Amount) extends UserEvent {
  require(amount.valueInPence >= 0)
}