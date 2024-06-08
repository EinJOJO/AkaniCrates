package it.einjojo.akani.crates.storage.player;

import it.einjojo.akani.crates.player.CratePlayer;
import it.einjojo.akani.crates.player.CratePlayerImpl;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.UUID;

public class NullCratePlayerStorage implements CratePlayerStorage {
    private static final Logger log = LoggerFactory.getLogger(NullCratePlayerStorage.class);
    private final HashMap<UUID, CratePlayerImpl> storage = new HashMap<>();

    @Override
    public void init() {
        log.warn("Using NullCratePlayerStorage, player data will not be saved!");
        log.warn("Using NullCratePlayerStorage, player data will not be saved!");
        log.warn("Using NullCratePlayerStorage, player data will not be saved!");
        log.warn("Using NullCratePlayerStorage, player data will not be saved!");
        log.warn("Using NullCratePlayerStorage, player data will not be saved!");
    }

    @Override
    public @NotNull CratePlayerImpl loadPlayer(@NotNull UUID uuid) {
        return storage.computeIfAbsent(uuid, (key) -> new CratePlayerImpl(key, new HashMap<>()));
    }

    @Override
    public void savePlayer(@NotNull CratePlayer player) {
        storage.put(player.playerUuid(), (CratePlayerImpl) player);
    }
}
