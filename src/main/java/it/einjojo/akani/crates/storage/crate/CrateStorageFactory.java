package it.einjojo.akani.crates.storage.crate;

import org.bukkit.plugin.java.JavaPlugin;

public class CrateStorageFactory {

    public CrateStorage createCrateStorage(JavaPlugin plugin) {
        return new NullCrateStorage();
    }

}
