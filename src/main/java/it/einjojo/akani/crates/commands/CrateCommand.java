package it.einjojo.akani.crates.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Single;
import co.aikar.commands.annotation.Subcommand;
import it.einjojo.akani.crates.CratesPlugin;
import it.einjojo.akani.crates.crate.Crate;
import it.einjojo.akani.crates.crate.CrateManager;
import it.einjojo.akani.crates.crate.content.CrateContent;
import it.einjojo.akani.crates.crate.content.CrateContentFactory;
import it.einjojo.akani.crates.crate.effect.NullEffectFactory;
import it.einjojo.akani.crates.crate.impl.InventoryCrateImpl;
import it.einjojo.akani.crates.gui.CrateAdminGui;
import it.einjojo.akani.crates.gui.CrateAnimatingOpenGui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@CommandAlias("crate|crates")
public class CrateCommand extends BaseCommand {
    private final CrateManager crateManager;

    public CrateCommand(PaperCommandManager paperCommandManager, CrateManager crateManager) {
        this.crateManager = crateManager;
        paperCommandManager.getCommandCompletions().registerAsyncCompletion("crates", c -> crateManager.crates().stream().map(Crate::id).toList());
        paperCommandManager.getCommandCompletions().registerAsyncCompletion("sound", c -> Arrays.stream(Sound.values()).map(Enum::name).toList());
        paperCommandManager.getCommandContexts().registerContext(Crate.class, c -> Optional.ofNullable(crateManager.crate(c.popFirstArg())).orElseThrow());
        paperCommandManager.getCommandContexts().registerContext(Sound.class, c -> {
            try {
                return Sound.valueOf(c.popFirstArg());
            } catch (IllegalArgumentException e) {
                throw new NoSuchElementException();
            }
        });
        paperCommandManager.registerCommand(this);
    }

    @Subcommand("create")
    @CommandCompletion("<id:string> <title:minimessage>")
    @Nullable
    public Crate createCrate(Player player, @Single String id, String title) {
        if (crateManager.crate(id) != null) {
            player.sendMessage(CratesPlugin.miniMessage().deserialize("<prefix><red>Die Kiste existiert bereits"));
            return null;
        }
        if (id.length() > 16) {
            player.sendMessage(CratesPlugin.miniMessage().deserialize("<prefix><red>Die ID darf maximal 16 Zeichen lang sein"));
            return null;
        }

        Component titleComponent = CratesPlugin.miniMessage().deserialize(title);
        Crate crate = new InventoryCrateImpl(
                id,
                titleComponent,
                new LinkedList<>(),
                new NullEffectFactory()
        );
        crateManager.register(crate);
        player.sendMessage(CratesPlugin.miniMessage().deserialize("<prefix><green>Crate <crate> <gray>(<id>)</gray> erstellt",
                Placeholder.parsed("id", id),
                Placeholder.component("crate", titleComponent)));

        return crate;
    }

    @Subcommand("create test")
    public void createTestCrate(Player player) {
        Crate crated = createCrate(player, "test", "<red>Test Crate</red>");
        if (crated == null) return;
        var factory = new CrateContentFactory();
        List<CrateContent> contents = new LinkedList<>();
        for (int i = 30; i < 60; i++) {
            contents.add(factory.itemContent(new ItemStack(Material.values()[i]), (float) 100 / i));
        }
        for (int i = 1; i < 6; i++) {
            contents.add(factory.coinsContent(1000 * i, (float) 1 / i));
        }
        for (int i = 10; i < 20; i++) {
            contents.add(factory.thalerContent(1000 * i, (float) 1 / i));
        }
        contents.add(factory.permissionContent("crate.test", "Test", 0.01f));
        crated.contents().addAll(contents);
    }

    @Subcommand("edit")
    @CommandCompletion("@crates --prefilled")
    public void editCrate(Player player, Crate crate) {
        player.sendMessage(CratesPlugin.miniMessage().deserialize("<prefix><gray>Editiere <crate> Crate",
                Placeholder.component("crate", crate.title())));
        new CrateAdminGui(player, crate, crateManager).open();
    }

    @Subcommand("preview")
    @CommandCompletion("@crates")
    public void previewCrate(Player player, Crate crate) {
        player.sendMessage(CratesPlugin.miniMessage().deserialize("<prefix><gray>Preview <crate> Crate",
                Placeholder.component("crate", crate.title())));
        crate.preview(player);
    }

    @Subcommand("open")
    @CommandCompletion("@crates")
    public void openCrate(Player player, Crate crate) {
        player.sendMessage(CratesPlugin.miniMessage().deserialize("<prefix><gray>Öffne <crate> Crate",
                Placeholder.component("crate", crate.title())));
        crate.open(player);
    }

    @Subcommand("setSound")
    @CommandCompletion("@sound")
    public void setSound(Player player, Sound sound) {
        player.sendMessage(CratesPlugin.miniMessage().deserialize("<prefix><gray>Setze Crate Sound auf <sound>",
                Placeholder.parsed("sound", sound.name())));
        CrateAnimatingOpenGui.setSound(sound);
    }


}
