package it.einjojo.akani.crates.crate.content.impl;

import it.einjojo.akani.crates.crate.content.EconomyCrateContent;
import it.einjojo.akani.crates.crate.content.PreviewItemFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Range;

public class NullEconomyCrateContentImpl implements EconomyCrateContent {
    private final int amount;
    private final String economyName;
    private final ItemStack previewItem;
    private float chance;


    public NullEconomyCrateContentImpl(int amount, float chance, PreviewItemFactory previewItemFactory, String economyName) {
        this.amount = amount;
        this.chance = chance;
        previewItem = previewItemFactory.createPreviewItem(this);
        this.economyName = economyName;
    }

    @Override
    public String currencyName() {
        return economyName;
    }

    @Override
    public int economyAmount() {
        return amount;
    }

    @Override
    public void give(Player player) {
        player.sendMessage("Du hast theoretisch " + amount + " Geld erhalten, aber gerade ist kein Economy-System verfügbar!");
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
