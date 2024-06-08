package it.einjojo.akani.crates.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import it.einjojo.akani.crates.crate.Crate;
import it.einjojo.akani.crates.crate.CrateManager;
import it.einjojo.akani.crates.crate.content.CrateContent;
import it.einjojo.akani.crates.crate.impl.InventoryCrateImpl;
import it.einjojo.akani.crates.gui.CrateAdminGui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

@CommandAlias("crate|crates")
public class CrateCommand extends BaseCommand {
    Crate demoCrate;

    public CrateCommand(PaperCommandManager paperCommandManager, CrateManager crateManager) {
        paperCommandManager.registerCommand(this);


    }

    @Subcommand("edit")
    public void editCrate(Player player) {
        if (demoCrate == null) {
            List<CrateContent> contents = new LinkedList<>();
            demoCrate = new InventoryCrateImpl("test", Component.text("TEST", NamedTextColor.RED), contents, null);
        }

        new CrateAdminGui(player, demoCrate).open();

    }


}
