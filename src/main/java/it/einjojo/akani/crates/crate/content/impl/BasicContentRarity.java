package it.einjojo.akani.crates.crate.content.impl;

import it.einjojo.akani.crates.crate.content.ContentRarity;
import it.einjojo.akani.crates.crate.content.CrateContent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public record BasicContentRarity(Component displayName, ItemStack openingTypeIndicator) implements ContentRarity {

    @Override
    public void postGive(Player player, CrateContent crateContent) {

    }
}
