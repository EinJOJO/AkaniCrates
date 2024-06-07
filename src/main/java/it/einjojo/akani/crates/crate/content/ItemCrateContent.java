package it.einjojo.akani.crates.crate.content;

import org.bukkit.inventory.ItemStack;

/**
 * Crate content that gives the player a certain item
 */
public interface ItemCrateContent extends CrateContent {
    ItemStack itemStack();
}
