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
import it.einjojo.akani.crates.crate.effect.NullEffectFactory;
import it.einjojo.akani.crates.crate.impl.InventoryCrateImpl;
import it.einjojo.akani.crates.gui.CrateAdminGui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.Optional;

@CommandAlias("crate|crates")
public class CrateCommand extends BaseCommand {
    private final CrateManager crateManager;

    public CrateCommand(PaperCommandManager paperCommandManager, CrateManager crateManager) {
        paperCommandManager.registerCommand(this);
        this.crateManager = crateManager;
        paperCommandManager.getCommandCompletions().registerAsyncCompletion("crates", c -> crateManager.crates().stream().map(Crate::id).toList());
        paperCommandManager.getCommandContexts().registerContext(Crate.class, c -> crateManager.crate(Optional.ofNullable(c.popFirstArg()).orElseThrow()));
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
        Component titleComponent = CratesPlugin.miniMessage().deserialize(title);
        crateManager.register(new InventoryCrateImpl(
                id,
                titleComponent,
                new LinkedList<>(),
                new NullEffectFactory()
        ));
        player.sendMessage(CratesPlugin.miniMessage().deserialize("<prefix><green>Crate <crate> <gray>(<id>)</gray> erstellt",
                Placeholder.parsed("id", id),
                Placeholder.component("crate", titleComponent)));

    }

    @Subcommand("edit")
    @CommandCompletion("@crates")
    public void editCrate(Player player, Crate crate) {
        player.sendMessage(CratesPlugin.miniMessage().deserialize("<prefix><gray>Editiere <crate> Crate",
                Placeholder.component("crate", crate.title())));
        new CrateAdminGui(player, crate, crateManager).open();
    }


}
