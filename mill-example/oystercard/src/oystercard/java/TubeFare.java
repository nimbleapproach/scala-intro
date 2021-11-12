package oystercard.java;

import java.util.function.BiFunction;
import java.util.Set;

public final class TubeFare {
    public final String ruleName;
    public final Amount price;

    public int getPriceInPence() {
        return price.valueInPence;
    }
    public final BiFunction<Set<Zone>, Set<Zone>, Boolean> ruleApplies;
    public TubeFare(String  name, Amount prc, BiFunction<Set<Zone>, Set<Zone>, Boolean> applies) {
        ruleName = name;
        price = prc;
        ruleApplies = applies;
    }
    public String toString() {
        return ruleName + " is " + price;
    }
}
