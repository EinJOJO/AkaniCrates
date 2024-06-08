package it.einjojo.akani.crates.storage.crate;

import it.einjojo.akani.crates.crate.Crate;
import org.bukkit.Location;

import java.util.Collection;

public interface CrateStorage {

    void init();

    Collection<Crate> loadAllCrates();

    void saveCrate(Crate crate);

    Collection<Location> loadAllLocations(String crateId);

    void saveLocation(String crateId, Location location);

}
