package it.einjojo.akani.crates;

import co.aikar.commands.PaperCommandManager;
import it.einjojo.akani.crates.commands.CrateCommand;
import it.einjojo.akani.crates.crate.CrateLocationRegistry;
import it.einjojo.akani.crates.crate.CrateManager;
import it.einjojo.akani.crates.listener.CrateBlockOpenListener;
import it.einjojo.akani.crates.player.CratePlayerManager;
import it.einjojo.akani.crates.storage.player.PlayerStorageFactory;
import mc.obliviate.inventory.InventoryAPI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.plugin.java.JavaPlugin;

public class CratesPlugin extends JavaPlugin {
    private static final MiniMessage miniMessage = MiniMessage.builder()
            .tags(TagResolver.builder()
                    .resolver(TagResolver.standard())
                    .tag("prefix", Tag.selfClosingInserting(MiniMessage.miniMessage().deserialize("<gray>Crates</gray>")))
                    .build())
            .build();
    private CratePlayerManager cratePlayerManager;

    public static MiniMessage miniMessage() {
        return miniMessage;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        new InventoryAPI(this).init();
        var storage = new PlayerStorageFactory().create(this);
        storage.init();
        cratePlayerManager = new CratePlayerManager(storage);
        new CrateBlockOpenListener(new CrateLocationRegistry(), this);
        var commandManager = new PaperCommandManager(this);
        new CrateCommand(commandManager, new CrateManager(null, null));
    }

    public CratePlayerManager cratePlayerManager() {
        return cratePlayerManager;
    }
}
