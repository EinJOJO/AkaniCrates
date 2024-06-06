package it.einjojo.akani.crates;

import it.einjojo.akani.crates.crate.CrateLocationRegistry;
import it.einjojo.akani.crates.listener.CrateBlockOpenListener;
import it.einjojo.akani.crates.player.CratePlayerManager;
import it.einjojo.akani.crates.storage.player.PlayerStorageFactory;
import mc.obliviate.inventory.InventoryAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.plugin.java.JavaPlugin;

public class CratesPlugin extends JavaPlugin {
    private CratePlayerManager cratePlayerManager;
    private static final MiniMessage miniMessage = MiniMessage.builder()
            .tags(TagResolver.builder()
                    .resolver(TagResolver.standard())
                    .tag("prefix", Tag.selfClosingInserting(MiniMessage.miniMessage().deserialize("<gray>Crates</gray>")))
                    .build())
            .build();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        new InventoryAPI(this).init();
        cratePlayerManager = new CratePlayerManager(new PlayerStorageFactory().create(this));
        new CrateBlockOpenListener(new CrateLocationRegistry(), this);
    }

    public CratePlayerManager cratePlayerManager() {
        return cratePlayerManager;
    }

    public static MiniMessage miniMessage() {
        return miniMessage;
    }
}
