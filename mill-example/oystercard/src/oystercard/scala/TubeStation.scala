package oystercard.scala

/**
  * @param name  the station name
  * @param zones the zone(s) the station is in
  */
class TubeStation private[oystercard](val name: String, zoneIds: Int*) {
  require(zoneIds.nonEmpty)
  val zones: Set[Zone] = zoneIds.toSet
}

/**
  * Contains our static reference data for all known oystercard stations and a means to find a station based on its name
  */
object TubeStation {
  def apply(station: String, firstZone: Int, theRest: Int*): TubeStation = {
    new TubeStation(station, (firstZone +: theRest): _*)
  }

  def unapply(station: String): Option[TubeStation] = stations.find(_.name == station)

  case object EarlsCourt extends TubeStation("Earl’s Court", 1)

  case object Holborn extends TubeStation("Holborn", 1, 2)

  case object Wimbledon extends TubeStation("Wimbledon", 3)

  case object Hammersmith extends TubeStation("Hammersmith", 2)

  def stations = Set(EarlsCourt, Holborn, Wimbledon, Hammersmith)
}
