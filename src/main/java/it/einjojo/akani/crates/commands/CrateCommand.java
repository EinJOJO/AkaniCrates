package it.einjojo.akani.crates.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import it.einjojo.akani.crates.content.CrateContent;
import it.einjojo.akani.crates.crate.Crate;
import it.einjojo.akani.crates.crate.CrateManager;
import it.einjojo.akani.crates.crate.impl.CrateImpl;
import it.einjojo.akani.crates.gui.CrateAdminGui;
import it.einjojo.akani.crates.input.ChatInput;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

@CommandAlias("crate|crates")
public class CrateCommand  extends BaseCommand {

    public CrateCommand(PaperCommandManager paperCommandManager, CrateManager crateManager) {
        paperCommandManager.registerCommand(this);


    }

    @Subcommand("edit")
    public void editCrate(Player player ) {
        List<CrateContent> contents = new LinkedList<>();
        new CrateAdminGui(player, new CrateImpl("test",Component.text("TEST", NamedTextColor.RED),  contents)).open();
        new ChatInput(player, null, null, null, null);
    }




}
