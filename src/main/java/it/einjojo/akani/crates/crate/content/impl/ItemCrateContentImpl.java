package it.einjojo.akani.crates.crate.content.impl;

import com.google.common.base.Preconditions;
import it.einjojo.akani.crates.crate.content.ContentRarity;
import it.einjojo.akani.crates.crate.content.CrateGiveRewardException;
import it.einjojo.akani.crates.crate.content.ItemCrateContent;
import it.einjojo.akani.crates.crate.content.PreviewItemFactory;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public class ItemCrateContentImpl implements ItemCrateContent {
    private final ItemStack itemStack;
    private final PreviewItemFactory previewItemFactory;
    private ItemStack previewItem;
    private float chance;
    private ContentRarity rarity;

    public ItemCrateContentImpl(@NotNull ItemStack itemStack, float chance, PreviewItemFactory previewItemFactory, ContentRarity rarity) {
        Preconditions.checkNotNull(itemStack);
        //TODO AIR CHECK
        if (itemStack.getType().equals(Material.AIR)) {
            throw new IllegalArgumentException("itemStack cannot be of type air");
        }
        this.itemStack = itemStack;
        this.chance = chance;
        this.rarity = rarity;
        this.previewItemFactory = previewItemFactory;
        this.previewItem = previewItemFactory.createPreviewItem(this);

    }

    @Override
    public Component displayName() {
        return Component.text(itemStack.getType().name());
    }

    @Override
    public ContentRarity rarity() {
        return rarity;
    }

    @Override
    public void setRarity(ContentRarity rarity) {
        this.rarity = rarity;
    }

    @Override
    public ItemStack itemStack() {
        return itemStack;
    }

    @Override
    public void give(Player player) throws CrateGiveRewardException {
        try {
            player.getInventory().addItem(itemStack());
        } catch (Exception ex) {
            throw new CrateGiveRewardException(ex);
        }
    }

    @Override
    public @Range(from = 0, to = 1) float chance() {
        return chance;
    }

    @Override
    public void setChance(@Range(from = 0, to = 1) float chance) {
        this.chance = chance;
        this.previewItem = previewItemFactory.createPreviewItem(this);
    }

    @Override
    public ItemStack previewItem() {
        return previewItem;
    }
}
