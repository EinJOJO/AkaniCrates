package it.einjojo.akani.crates.crate;

import it.einjojo.akani.crates.storage.crate.CrateStorage;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class CrateManager {
    private static final Logger log = LoggerFactory.getLogger(CrateManager.class);
    private final List<Crate> crateList = new LinkedList<>();
    private final CrateStorage storage;
    private final CrateLocationRegistry crateLocationRegistry;

    public CrateManager(CrateStorage storage, CrateLocationRegistry crateLocationRegistry) {
        this.storage = storage;
        this.crateLocationRegistry = crateLocationRegistry;
    }

    public List<Crate> crates() {
        return crateList;
    }


    public @Nullable Crate crate(String id) {
        for (Crate crate : crateList) {
            if (crate.id().equals(id)) {
                return crate;
            }
        }
        return null;
    }

    public CrateStorage storage() {
        return storage;
    }

    public void loadAll() {
        for (Crate crate : storage.loadAllCrates()) {
            crateList.add(crate);
            for (Location location : storage.loadAllLocations(crate.id())) {
                if (location.getWorld() == null) {
                    log.warn("World not found for crate {} - location: {}", crate.id(), location);
                    continue;
                }
                crateLocationRegistry.register(location, crate);
            }
        }
    }

    public void saveAll() {
        for (Crate crate : crateList) {
            storage.saveCrate(crate);
        }
    }

    public void save(Crate crate) {
        storage.saveCrate(crate);
    }

}
