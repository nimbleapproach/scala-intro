package oystercard.java;

import java.util.HashSet;
import java.util.Set;

/**
 * Contains our static reference data for all known oystercard stations and a means to find a station based on its name
 */
public enum TubeStation {

    EarlsCourt("Earl’s Court", 1),
    Holborn("Holborn", 1, 2),
    Wimbledon("Wimbledon", 3),
    Hammersmith("Hammersmith", 2);

    public final Set<Zone> zones;
    public final String stationName;

    TubeStation(String name, int firstZone, int ... theRest) {
        Set<Zone> zoneSet = new HashSet<>();
        zoneSet.add(new Zone(firstZone  ));
        for (int z : theRest) {
            zoneSet.add(new Zone(z));
        }
        stationName = name;

        zones = java.util.Collections.unmodifiableSet(zoneSet);
    }
}
