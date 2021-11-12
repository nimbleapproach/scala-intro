package oystercard.scala

/** Represents a tube fare rule
  *
  * @param name the rule name (e.g. 'Anywhere in Zone 1')
  * @param price the fare cost
  * @param applies a predicate which checks if the fare is applicable
  */
final case class TubeFare private(name: String, price: Amount, applies: TubeFare.Applies)

object TubeFare {
  type FromZones = Set[Zone]
  type ToZones = Set[Zone]
  type Applies = (FromZones, ToZones) => Boolean
}
