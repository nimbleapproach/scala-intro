package oystercard.java;

public final class Zone {
    public final int value;
    public Zone(int z) {
        assert(z > 0);
        value = z;
    }
    @Override
    public boolean equals(Object other) {
        if (other instanceof Zone) {
            return ((Zone)other).value == value;
        } else {
            return false;
        }
    }
    @Override
    public int hashCode() {
        return value;
    }
    public final static Zone One = new Zone(1);
}
