package it.einjojo.akani.crates.content;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Range;

public interface CrateContent {

    void give(Player player);

    @Range(from = 0, to = 1)
    float chance();

    void setChance(@Range(from = 0, to = 1) float chance);

    ItemStack previewItem();

}
