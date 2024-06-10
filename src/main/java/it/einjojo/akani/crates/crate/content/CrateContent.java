package it.einjojo.akani.crates.crate.content;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Range;

/**
 * Generic crate content that can be given to a player
 */
public interface CrateContent {

    /**
     * enforce exception handling
     *
     * @param player the player to give the content to
     * @throws CrateGiveRewardException if the content could not be given and the player should be able to reroll
     */
    void give(Player player) throws CrateGiveRewardException;

    @Range(from = 0, to = 1)
    float chance();

    void setChance(@Range(from = 0, to = 1) float chance);

    ContentRarity rarity();

    /**
     * Name of the item
     *
     * @return the name of the item
     */
    Component displayName();

    void setRarity(ContentRarity rarity);

    ItemStack previewItem();

}
