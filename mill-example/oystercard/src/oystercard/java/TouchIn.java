package oystercard.java;

final class TouchIn {
    final TubeStation station;
    final Amount fareCharged;

    TouchIn(TubeStation station, Amount fareCharged) {
        this.station = station;
        this.fareCharged = fareCharged;
    }

    @Override
    public String toString() {
        return "TouchIn(" + station+ ", " + fareCharged + ")";
    }
}
