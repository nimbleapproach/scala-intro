package oystercard.java;

/**
 * Represents somethign which can price journies
 */
interface JourneyCalculator {

    /** @return the amount charged when a user first touches in
     */
    Amount maxFare();

    /** @param from the touch-in station
     * @param to the touch-out station
     * @return the price for that journey
     */
    Amount price(TubeStation from, TubeStation to);

    /** @return the single price which applies to all bus routes
     */
    Amount busFare();
}
