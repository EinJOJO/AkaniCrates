package it.einjojo.akani.crates;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.RegisteredCommand;
import it.einjojo.akani.crates.commands.CrateCommand;
import it.einjojo.akani.crates.crate.CrateLocationRegistry;
import it.einjojo.akani.crates.crate.CrateManager;
import it.einjojo.akani.crates.listener.CrateBlockOpenListener;
import it.einjojo.akani.crates.player.CratePlayerManager;
import it.einjojo.akani.crates.storage.crate.CrateStorageFactory;
import it.einjojo.akani.crates.storage.player.PlayerStorageFactory;
import mc.obliviate.inventory.InventoryAPI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.NoSuchElementException;

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
        // player
        var playerStorage = new PlayerStorageFactory().create(this);
        playerStorage.init();
        cratePlayerManager = new CratePlayerManager(playerStorage);
        var crateLocationRegistry = new CrateLocationRegistry();
        new CrateBlockOpenListener(crateLocationRegistry, this);
        // crate
        var crateManager = new CrateManager(new CrateStorageFactory().createCrateStorage(this),
                crateLocationRegistry);

        var commandManager = new PaperCommandManager(this);
        commandManager.setDefaultExceptionHandler(this::handleCommandException);
        new CrateCommand(commandManager, crateManager);
    }

    public CratePlayerManager cratePlayerManager() {
        return cratePlayerManager;
    }

    private boolean handleCommandException(BaseCommand command, RegisteredCommand<?> registeredCommand, CommandIssuer sender, List<String> args, Throwable t) {
        CommandSender commandSender = sender.getIssuer();
        if (t instanceof NoSuchElementException ex) {
            commandSender.sendMessage(miniMessage().deserialize("<red>Kein Element gefunden"));
            return true;
        }
        return false;
    }
}
