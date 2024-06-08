package it.einjojo.akani.crates.storage.crate;

import it.einjojo.akani.crates.crate.Crate;
import org.bukkit.Location;

import java.util.*;

public class NullCrateStorage implements CrateStorage {
    private final List<Crate> crateList = new LinkedList<>();
    private final Map<String, List<Location>> locationMap = new HashMap<>();

    @Override
    public void init() {
        
    }

    @Override
    public Collection<Crate> loadAllCrates() {
        return crateList;
    }

    @Override
    public void saveCrate(Crate crate) {
        crateList.add(crate);
    }

    @Override
    public Collection<Location> loadAllLocations(String crateId) {
        return locationMap.get(crateId);
    }

    @Override
    public void saveLocation(String crateId, Location location) {
        locationMap.computeIfAbsent(crateId, k -> new LinkedList<>()).add(location);
    }
}
