package oystercard.java;

import java.util.Objects;

/**
 * Represents events which can update an OysterCard.
 *
 * They could be taken from a queue or perhaps unmarshalled from an Http request
 */
public abstract class UserEvent {
    // mirror the 'sealed' keyword in scala - only allow a set/fixed number of UserEvent implementations
    private UserEvent() { }

    static final TakeBus takeBus = new TakeBus();
    static final TouchCard touchCard(TubeStation station) {
        return new TouchCard(station);
    }
    static final LoadCard loadCard(Amount amount) {
        return new LoadCard(amount);
    }
    public final static class TouchCard extends UserEvent {
        public final TubeStation station;
        private TouchCard(TubeStation station) {
            this.station = station;
        }
        @Override
        public boolean equals(Object other) {
            if (other instanceof TouchCard) {
                return ((TouchCard) other).station.equals(station);
            } else {
                return false;
            }
        }
        @Override
        public String toString() {
            return "TouchCard(" + station + ")";
        }
        @Override
        public int hashCode() {
            return station.ordinal();
        }
    }

    public final static class TakeBus extends UserEvent {
        private TakeBus() {

        }
        @Override
        public String toString() {
            return "TakeBus";
        }
        @Override
        public boolean equals(Object other) {
            return other instanceof TakeBus;
        }
        @Override
        public int hashCode() {
            return 17;
        }
    }

    public final static class LoadCard extends UserEvent {
        public final Amount amount;
        private LoadCard(Amount amount) {
            this.amount = amount;
        }

        @Override
        public String toString() {
            return "LoadCard(" + amount + ")";
        }
        @Override
        public boolean equals(Object other) {
            if (other instanceof LoadCard) {
                return ((LoadCard) other).amount.equals(amount);
            } else {
                return false;
            }
        }
        @Override
        public int hashCode() {
            return Objects.hash(amount);
        }
    }
}
