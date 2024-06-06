package it.einjojo.akani.crates.storage.player;

import it.einjojo.akani.crates.player.CratePlayer;
import it.einjojo.akani.crates.player.CratePlayerImpl;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface CratePlayerStorage {

    void init();

    @NotNull
    CratePlayerImpl loadPlayer(@NotNull UUID uuid);

    void savePlayer(@NotNull CratePlayer player);


}
