package it.einjojo.akani.crates.crate.content.impl;

import com.google.common.base.Preconditions;
import it.einjojo.akani.crates.crate.content.ItemCrateContent;
import it.einjojo.akani.crates.crate.content.PreviewItemFactory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public class ItemCrateContentImpl implements ItemCrateContent {
    private final ItemStack itemStack;
    private final ItemStack previewItem;
    private float chance;

    public ItemCrateContentImpl(@NotNull ItemStack itemStack, float chance, PreviewItemFactory previewItemFactory) {
        Preconditions.checkNotNull(itemStack);
        //TODO AIR CHECK
        if (itemStack.getType().equals(Material.AIR)) {
            throw new IllegalArgumentException("itemStack cannot be of type air");
        }
        this.itemStack = itemStack;
        this.chance = chance;
        this.previewItem = previewItemFactory.createPreviewItem(this);

    }

    @Override
    public ItemStack itemStack() {
        return itemStack;
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
        return previewItem;
    }
}
