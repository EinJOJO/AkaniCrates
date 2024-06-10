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
    public void createCrate(Player player, @Single String id, String title) {
        if (crateManager.crate(id) != null) {
            player.sendMessage(CratesPlugin.miniMessage().deserialize("<prefix><red>Die Kiste existiert bereits"));
            return;
        }
        if (id.length() > 16) {
            player.sendMessage(CratesPlugin.miniMessage().deserialize("<prefix><red>Die ID darf maximal 16 Zeichen lang sein"));
            return;
        }

        List<CrateContent> contents = new LinkedList<>();
        var factory = new CrateContentFactory();
        //for (Material m : Material.values()) {
        //    if (m.isFuel() && !m.isAir()) {
        //        contents.add(factory.itemContent(new ItemStack(m), 1));
        //    }
        //}

        Component titleComponent = CratesPlugin.miniMessage().deserialize(title);
        crateManager.register(new InventoryCrateImpl(
                id,
                titleComponent,
                contents,
                new NullEffectFactory()
        ));
        player.sendMessage(CratesPlugin.miniMessage().deserialize("<prefix><green>Crate <crate> <gray>(<id>)</gray> erstellt",
                Placeholder.parsed("id", id),
                Placeholder.component("crate", titleComponent)));

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
