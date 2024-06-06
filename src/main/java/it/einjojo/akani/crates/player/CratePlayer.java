package it.einjojo.akani.crates.player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Map;
import java.util.UUID;

public interface CratePlayer {
    @NotNull
    UUID playerUuid();

    @NotNull
    Map<String, Integer> crateMap();

    /**
     * Get the amount of keys the player has for a crate
     *
     * @param crateId The id of the crate
     * @return The amount of keys the player has for the crate. 0 if the player has no keys for the crate
     */
    int keys(String crateId);

    /**
     * Give the player keys for a crate
     *
     * @param crateId The id of the crate
     * @param amount  The amount of keys to give
     */
    void giveKeys(@NotNull String crateId, @Range(from = 1, to = Integer.MAX_VALUE) int amount);

    void removeKeys(@NotNull String crateId, @Range(from = 0, to = Integer.MAX_VALUE) int amount);

}
