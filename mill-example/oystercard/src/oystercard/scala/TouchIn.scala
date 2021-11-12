package oystercard.scala

/** Represents the data captured when a user touched in at a tube station
  * @param at the tube station touched in at
  * @param fareCharged the maximum amount which was charged at touch-in (which should be refunded if/when they touch out)
  */
private[oystercard] case class TouchIn(at: TubeStation, fareCharged: Amount)
