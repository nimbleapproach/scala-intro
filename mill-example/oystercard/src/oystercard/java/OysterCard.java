package oystercard.java;

import java.util.Optional;

/**
 * The main entry point which represents an OysterCard's state.
 *
 *
 * It's a persistent data structure which can be updated via 'update' or explicitly calling 'takeBus', 'loadCard', or 'touchCard',
 * all of which return a new OysterCard.
 *
 * If used within a context which actually will make a charge to some database/disk, one could just take a difference of a
 * before and after balance.
 *
 */
public final class OysterCard {
    private final Optional<TouchIn> lastTouch;
    public final Amount balance;

    public static OysterCard Empty = new OysterCard();

    public OysterCard() {
        this(Amount.inPence(0));
    }
    public OysterCard(Amount startingBalance) {
        this(startingBalance, Optional.empty());
    }
    private OysterCard(Amount startingBalance, Optional<TouchIn> lastTouch) {
        this.lastTouch = lastTouch;
        this.balance = startingBalance;
    }
    public OysterCard update(UserEvent event, JourneyCalculator calculator) {
        if (event.equals(UserEvent.takeBus)) {
            return takeBus(calculator.busFare());
        } else if (event instanceof UserEvent.LoadCard) {
            return loadCard(((UserEvent.LoadCard) event).amount);
        } else if (event instanceof UserEvent.TouchCard) {
            return touchCard(((UserEvent.TouchCard) event).station, calculator);
        } else {
            throw new IllegalArgumentException("Unknown user event " + event);
        }
    }

    @Override
    public String toString() {
        return "OysterCard(" + balance + ", " + lastTouch +")";
    }

    public OysterCard takeBus(Amount busFare) {
        return new OysterCard(balance.minus(busFare));
    }
    public OysterCard loadCard(Amount amount) {
        return new OysterCard(balance.plus(amount));
    }
    public OysterCard touchCard(TubeStation station, JourneyCalculator calculator) {
        return lastTouch.map(touchIn -> touchOut(touchIn, station, calculator)).orElse(touchIn(station, calculator));
    }

    private OysterCard touchIn(TubeStation station, JourneyCalculator calculator) {
        Amount fare = calculator.maxFare();
        return new OysterCard(balance.minus(fare), Optional.of(new TouchIn(station, fare)));
    }

    private OysterCard touchOut(TouchIn touchedIn, TubeStation touchOut, JourneyCalculator calculator) {
        Amount cost = calculator.price(touchedIn.station, touchOut);
        Amount newBalance = balance.plus(touchedIn.fareCharged).minus(cost);
        return new OysterCard(newBalance);
    }
}
