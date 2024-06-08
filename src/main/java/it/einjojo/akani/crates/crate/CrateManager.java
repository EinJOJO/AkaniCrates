package it.einjojo.akani.crates.crate;

import com.google.common.base.Preconditions;
import it.einjojo.akani.crates.storage.crate.CrateStorage;
import org.bukkit.Location;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;
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

    public CrateManager(@NotNull CrateStorage storage, @NotNull CrateLocationRegistry crateLocationRegistry) {
        Preconditions.checkNotNull(storage);
        Preconditions.checkNotNull(crateLocationRegistry);
        this.storage = storage;
        this.crateLocationRegistry = crateLocationRegistry;
    }

    public List<Crate> crates() {
        return crateList;
    }

    public void register(Crate crate) {
        crateList.add(crate);
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

    @Blocking
    public void loadAll() {
        crateList.clear();
        crateLocationRegistry.clear();
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
}
