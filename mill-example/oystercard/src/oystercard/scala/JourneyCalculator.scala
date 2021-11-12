package oystercard.scala

/**
  * Represents something which can price tube and bus journeys
  */
trait JourneyCalculator {

  /** @return the full amount when a user first touched in
    */
  def maxFare: Amount

  def price(from: TubeStation, to: TubeStation): Amount

  def busFare: Amount
}

object JourneyCalculator {
  def apply(): JourneyCalculator = DefaultJourneyCalculator

  case object DefaultJourneyCalculator extends JourneyCalculator {
    private val prices: List[TubeFare] = List(
      TubeFare("Anywhere in zone 1", Amount.inPence(250), anywhereInZoneOne),
      TubeFare("Any one zone outside zone 1", Amount.inPence(200), oneZoneOutsizeZone1),
      TubeFare("Any two zones including zone 1", Amount.inPence(300), twoZonesIncludingZone1),
      TubeFare("Any two zones excluding zone 1", Amount.inPence(225), twoZonesExcludingZone1),
      TubeFare("Any three zones", Amount.inPence(320), anyThreeZones),
    )

    override def price(from: TubeStation, to: TubeStation): Amount = {
      faresBetween(from.zones, to.zones) match {
        case Nil => Amount.Zero
        case nonEmptyList => // favour the customer by choosing the min fare
          nonEmptyList.minBy(_.valueInPence)
      }
    }

    private def faresBetween(fromZones: Set[Zone], toZones: Set[Zone]): List[Amount] = prices.collect {
      case TubeFare(_, price, applies) if applies(fromZones, toZones) => price
    }


    override lazy val maxFare = Amount.inPence(prices.map(_.price.valueInPence).max)

    override val busFare = Amount.inPence(180)

    private def anywhereInZoneOne(fromZones: Set[Zone], toZones: Set[Zone]) = {
      fromZones.contains(1) && toZones.contains(1)
    }

    private def oneZoneOutsizeZone1(fromZones: Set[Zone], toZones: Set[Zone]): Boolean = {
      val zones = fromZones ++ toZones
      zones.size == 1 && !zones.contains(1)
    }

    // I'm reading 'including zone 1' to mean that zone 1 doesn't have any special treatment for this rule
    private def twoZonesIncludingZone1 = anyTwoZones _
    private def anyTwoZones(fromZones: Set[Zone], toZones: Set[Zone]): Boolean = (fromZones ++ toZones).size == 2
    private def twoZonesExcludingZone1(fromZones: Set[Zone], toZones: Set[Zone]): Boolean = {
      val zones = fromZones ++ toZones
      zones.size == 2 && !zones.contains(1)
    }
    private def anyThreeZones(fromZones: Set[Zone], toZones: Set[Zone]): Boolean = (fromZones ++ toZones).size == 3
  }

}
