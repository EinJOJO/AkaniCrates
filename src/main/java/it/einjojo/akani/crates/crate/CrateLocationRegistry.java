package it.einjojo.akani.crates.crate;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrateLocationRegistry {

    private final Map<Location, Crate> locationCrateMap = new HashMap<>();

    public void register(@NotNull Location location, @NotNull Crate crate) {
        locationCrateMap.put(location, crate);
    }

    @Nullable
    public Crate crateByLocation(@NotNull Location location) {
        return locationCrateMap.get(location);
    }

    public List<Location> locationsByCrate(@NotNull Crate crate) {
        return locationCrateMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(crate))
                .map(Map.Entry::getKey)
                .toList();
    }

    public void unregister(Location location) {
        locationCrateMap.remove(location);
    }


}
