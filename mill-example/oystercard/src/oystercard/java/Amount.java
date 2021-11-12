package oystercard.java;

import java.util.Locale;

public final class Amount {

    public final int valueInPence;

    public int getValueInPence() {
        return valueInPence;
    }
    public static Amount Zero = inPence(0);

    private Amount(int pence) {
        valueInPence = pence;
    }

    public static Amount inPence(int pence) {
        return new Amount(pence);
    }

    public static Amount inPounds(int pounds) {
        return inPence(pounds * 100);
    }

    public Amount plus(Amount other) {
        return new Amount(valueInPence + other.valueInPence);
    }

    public Amount minus(Amount other) {
        return new Amount(valueInPence - other.valueInPence);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Amount) {
            return ((Amount) other).valueInPence == valueInPence;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return valueInPence;
    }

    @Override
    public String toString() {
        return show(Locale.getDefault());
    }

    public String show(Locale locale) {
        return java.text.NumberFormat.getCurrencyInstance(locale).format(valueInPence / 100.0);
    }
}