package it.einjojo.akani.crates.player;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import it.einjojo.akani.crates.storage.player.CratePlayerStorage;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class CratePlayerManager {
    private final CratePlayerStorage storage;
    private final AsyncLoadingCache<UUID, CratePlayer> cachedPlayers = Caffeine.newBuilder()
            .expireAfterAccess(2, TimeUnit.MINUTES)
            .buildAsync(this::loadCratePlayer);

    public CratePlayerManager(CratePlayerStorage storage) {
        this.storage = storage;
    }


    public CompletableFuture<CratePlayer> loadCratePlayer(UUID player, Executor executor) {
        return CompletableFuture.supplyAsync(() -> storage.loadPlayer(player), executor);
    }

    public CratePlayer cratePlayer(UUID player) {
        return cachedPlayers.synchronous().get(player);
    }

    public CompletableFuture<CratePlayer> cratePlayerAsync(UUID player) {
        return cachedPlayers.get(player);
    }

    /**
     * Updates Cached entry and writes the crate player to the database.
     * @param player The player to update
     */
    public void update(CratePlayer player) {
        cachedPlayers.synchronous().put(player.playerUuid(), player);
        storage.savePlayer(player);
    }

    public void invalidateCache(UUID player) {
        cachedPlayers.synchronous().invalidate(player);
    }




}
