package it.einjojo.akani.crates.crate;

import it.einjojo.akani.crates.content.CrateContent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface Crate {

    String id();

    Component title();

    void open(Player player);

    void preview(Player player);

    List<CrateContent> contents();

}
