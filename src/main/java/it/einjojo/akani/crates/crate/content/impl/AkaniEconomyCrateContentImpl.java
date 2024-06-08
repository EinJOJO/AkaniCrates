package it.einjojo.akani.crates.crate.content.impl;

import it.einjojo.akani.core.api.economy.EconomyManager;
import it.einjojo.akani.crates.crate.content.CrateGiveRewardException;
import it.einjojo.akani.crates.crate.content.EconomyCrateContent;
import it.einjojo.akani.crates.crate.content.PreviewItemFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Range;

public class AkaniEconomyCrateContentImpl implements EconomyCrateContent {
    private final int economyAmount;
    private final String currencyName;
    private final EconomyManager economyManager;
    private final ItemStack previewItem;
    private float chance;


    public AkaniEconomyCrateContentImpl(int economyAmount, float chance, EconomyManager economyManager, PreviewItemFactory previewItemFactory, String currencyName) {
        this.economyAmount = economyAmount;
        this.economyManager = economyManager;
        this.chance = chance;
        this.previewItem = previewItemFactory.createPreviewItem(this);
        this.currencyName = currencyName;
    }

    @Override
    public String currencyName() {
        return currencyName;
    }

    public EconomyManager economyManager() {
        return economyManager;
    }

    @Override
    public void give(Player player) throws CrateGiveRewardException {
        try {
            economyManager.playerEconomyAsync(player.getUniqueId()).thenAccept((economyHolder -> {
                economyHolder.orElseThrow().addBalance(economyAmount);
            })).exceptionally((ex) -> {
                throw new RuntimeException(ex);
            });
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
    }

    @Override
    public ItemStack previewItem() {
        return previewItem;
    }

    @Override
    public int economyAmount() {
        return economyAmount;
    }
}
