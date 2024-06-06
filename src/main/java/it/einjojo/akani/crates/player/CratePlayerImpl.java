package it.einjojo.akani.crates.player;

import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CratePlayerImpl implements CratePlayer {
    private final UUID playerUuid;
    private final Map<String, Integer> crates;
    private final Set<String> changedSet = new HashSet<>();

    public CratePlayerImpl(UUID playerUuid, Map<String, Integer> crates) {
        this.playerUuid = playerUuid;
        this.crates = crates;
    }

    @Override
    public @NotNull UUID playerUuid() {
        return playerUuid;
    }

    @Override
    public @NotNull Map<String, Integer> crateMap() {
        return ImmutableMap.copyOf(crates);
    }

    @Override
    public int keys(@NotNull String crateId) {
        return crates.getOrDefault(crateId, 0);
    }

    @Override
    public void giveKeys(@NotNull String crateId, @Range(from = 1, to = Integer.MAX_VALUE) int amount) {
        crates.put(crateId, keys(crateId) + amount);
        changedSet.add(crateId);
    }

    public boolean hasChanged() {
        return !changedSet.isEmpty();
    }

    public Set<String> changedSet() {
        return changedSet;
    }


    @Override
    public void removeKeys(@NotNull String crateId, @Range(from = 0, to = Integer.MAX_VALUE) int amount) {
        int current = keys(crateId);
        if (current < amount) {
            throw new IllegalArgumentException("Amount exceeds current keys");
        }
        crates.put(crateId, current - amount);
        changedSet.add(crateId);
    }
}
