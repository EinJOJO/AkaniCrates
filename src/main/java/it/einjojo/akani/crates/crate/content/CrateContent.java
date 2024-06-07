package it.einjojo.akani.crates.crate.content;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Range;

/**
 * Generic crate content that can be given to a player
 */
public interface CrateContent {

    void give(Player player);

    @Range(from = 0, to = 1)
    float chance();

    void setChance(@Range(from = 0, to = 1) float chance);

    ItemStack previewItem();

}
