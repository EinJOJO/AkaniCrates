package it.einjojo.akani.crates.content.impl;

import com.google.common.base.Preconditions;
import it.einjojo.akani.crates.content.ItemCrateContent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public class ItemCrateContentImpl implements ItemCrateContent {
    private final ItemStack itemStack;
    private float chance;

    public ItemCrateContentImpl(@NotNull ItemStack itemStack, float chance) {
        Preconditions.checkNotNull(itemStack);
        if (itemStack.getType().equals(Material.AIR)) {
            throw new IllegalArgumentException("itemStack cannot be of type air");
        }
        this.itemStack = itemStack;
        this.chance = chance;
    }

    @Override
    public ItemStack itemStack() {
        return itemStack.clone();
    }

    @Override
    public void give(Player player) {
        player.getInventory().addItem(itemStack());
    }

    @Override
    public @Range(from = 0, to = 1) float chance() {
        return chance;
    }

    @Override
    public void setChance(@Range(from = 0, to = 1) float chance) {
        this.chance = chance;
    }

    @Override
    public ItemStack previewItem() {
        return itemStack();
    }
}
