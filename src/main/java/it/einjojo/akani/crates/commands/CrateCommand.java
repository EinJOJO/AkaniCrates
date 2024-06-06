package it.einjojo.akani.crates.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import it.einjojo.akani.crates.crate.content.CrateContent;
import it.einjojo.akani.crates.crate.content.CrateContentFactory;
import it.einjojo.akani.crates.crate.Crate;
import it.einjojo.akani.crates.crate.CrateManager;
import it.einjojo.akani.crates.crate.impl.InventoryCrateImpl;
import it.einjojo.akani.crates.gui.CrateAdminGui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.List;

@CommandAlias("crate|crates")
public class CrateCommand  extends BaseCommand {
    Crate demoCrate;
    public CrateCommand(PaperCommandManager paperCommandManager, CrateManager crateManager) {
        paperCommandManager.registerCommand(this);


    }

    @Subcommand("edit")
    public void editCrate(Player player ) {
        if (demoCrate == null) {
            List<CrateContent> contents = new LinkedList<>();
            var factory = new CrateContentFactory();
            for (Material m : Material.values() ) {
                if (m == Material.AIR) continue;
                contents.add(factory.itemContent(new ItemStack(m), (float)Math.random()));
            }
            demoCrate = new InventoryCrateImpl("test",Component.text("TEST", NamedTextColor.RED),  contents, null);
        }

        new CrateAdminGui(player, demoCrate).open();

    }




}
