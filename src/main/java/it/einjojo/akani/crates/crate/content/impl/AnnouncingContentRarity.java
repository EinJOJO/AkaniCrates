package it.einjojo.akani.crates.crate.content.impl;

import it.einjojo.akani.crates.CratesPlugin;
import it.einjojo.akani.crates.crate.content.ContentRarity;
import it.einjojo.akani.crates.crate.content.CrateContent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public record AnnouncingContentRarity(Component displayName, ItemStack openingIndicator) implements ContentRarity {
    @Override
    public Component displayName() {
        return displayName;
    }

    @Override
    public ItemStack openingTypeIndicator() {
        return openingIndicator;
    }

    @Override
    public void postGive(Player player, CrateContent crateContent) {
        Bukkit.broadcast(CratesPlugin.miniMessage().deserialize("<prefix><gray>Der Spieler <player> hat <rarity> <content> erhalten!",
                Placeholder.parsed("player", player.getName()),
                Placeholder.component("rarity", displayName),
                Placeholder.component("content", crateContent.displayName()))
        );
    }
}
