package oystercard.java;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public final class DefaultJourneyCalculator implements JourneyCalculator {

    private final List<TubeFare> fares;
    private final Amount busPrice = Amount.inPence(180);
    private final Amount maxFare;

    public DefaultJourneyCalculator() {
        final List<TubeFare> fareBuffer = new ArrayList<>();
        fareBuffer.add(new TubeFare("Anywhere in zone 1", Amount.inPence(250), anywhereInZoneOne));
        fareBuffer.add(new TubeFare("Any one zone outside zone 1", Amount.inPence(200), oneZoneOutsizeZone1));
        fareBuffer.add(new TubeFare("Any two zones including zone 1", Amount.inPence(300), twoZonesIncludingZone1));
        fareBuffer.add(new TubeFare("Any two zones excluding zone 1", Amount.inPence(225), twoZonesExcludingZone1));
        fareBuffer.add(new TubeFare("Any three zones", Amount.inPence(320), anyThreeZones));

        fares = Collections.unmodifiableList(fareBuffer);

        int maxPence = fares.stream().mapToInt(TubeFare::getPriceInPence).max().getAsInt();
        maxFare = Amount.inPence(maxPence);
    }

    @Override
    public Amount maxFare() {
        return maxFare;
    }

    @Override
    public Amount price(TubeStation from, TubeStation to) {
        final Stream<TubeFare> applicableFares = fares.stream().filter(f -> f.ruleApplies.apply(from.zones, to.zones));
        final Optional<Amount> cheapestFare = applicableFares.map(f -> f.price).min(Comparator.comparingInt(Amount::getValueInPence));
        return cheapestFare.orElse(maxFare);
    }

    static BiFunction<Set<Zone>, Set<Zone>, Boolean> anywhereInZoneOne = (fromZones, toZones) -> fromZones.contains(new Zone(1)) && toZones.contains(Zone.One);

    static BiFunction<Set<Zone>, Set<Zone>, Boolean> oneZoneOutsizeZone1 = (fromZones, toZones) -> {
        Set<Zone> zones = concat(fromZones, toZones);
        return zones.size() == 1 && !zones.contains(Zone.One);
    };

    static BiFunction<Set<Zone>, Set<Zone>, Boolean> twoZonesIncludingZone1 = (fromZones, toZones) -> concat(fromZones, toZones).size() == 2;

    static BiFunction<Set<Zone>, Set<Zone>, Boolean> twoZonesExcludingZone1 = (fromZones, toZones) -> {
        Set<Zone> zones = concat(fromZones, toZones);
        return zones.size() == 2 && !zones.contains(Zone.One);
    };
    static BiFunction<Set<Zone>, Set<Zone>, Boolean> anyThreeZones = (fromZones, toZones) -> concat(fromZones, toZones).size() == 3;

    private static <T> Set<T> concat(Set<T> lhs, Set<T> rhs) {
        Set<T> copy = new HashSet<>(lhs);
        copy.addAll(rhs);
        return copy;
    }

    @Override
    public Amount busFare() {
        return busPrice;
    }
}
