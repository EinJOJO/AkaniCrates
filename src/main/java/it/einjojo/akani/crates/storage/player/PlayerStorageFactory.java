package it.einjojo.akani.crates.storage.player;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import it.einjojo.akani.core.api.AkaniCoreProvider;
import it.einjojo.akani.crates.util.AkaniUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PlayerStorageFactory {

    public CratePlayerStorage create(JavaPlugin plugin) {
        if (AkaniUtil.isAvailable()) {
            plugin.getSLF4JLogger().info("Creating Akani player storage");
            return createAkaniPlayerStorage();
        }
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("mysql");
        if (section == null) {
            throw new IllegalArgumentException("Missing mysql section in config");
        }
        plugin.getSLF4JLogger().info("Creating Hikari player storage");
        try {
            return createHikariPlayerStorage(section);
        } catch (Exception ex) {
            return new NullCratePlayerStorage();
        }
    }

    public HikariSQLCratePlayerStorage createHikariPlayerStorage(@NotNull ConfigurationSection section) {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + section.getString("host") + ":" + section.getInt("port") + "/" + section.getString("database"));
        config.setUsername(section.getString("username"));
        config.setPassword(section.getString("password"));
        config.setMaximumPoolSize(section.getInt("maxPoolSize"));
        config.setMinimumIdle(section.getInt("minIdle"));
        return new HikariSQLCratePlayerStorage(new HikariDataSource(config));
    }

    public AkaniCratePlayerStorage createAkaniPlayerStorage() {
        return new AkaniCratePlayerStorage(AkaniCoreProvider.get());
    }


}
