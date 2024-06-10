package it.einjojo.akani.crates.crate.content.impl;

import it.einjojo.akani.core.economy.CoinsEconomyManager;
import it.einjojo.akani.core.economy.ThalerEconomyManager;
import it.einjojo.akani.crates.CratesPlugin;
import it.einjojo.akani.crates.crate.content.CrateContent;
import it.einjojo.akani.crates.crate.content.EconomyCrateContent;
import it.einjojo.akani.crates.crate.content.ItemCrateContent;
import it.einjojo.akani.crates.crate.content.PreviewItemFactory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

/**
 * Kein Bock für Economy und so weiter eigene Klasse zu erstellen. Alles hieer rein balllern und dann passt es.
 */
public class PreviewItemFactoryImpl implements PreviewItemFactory {


    @Override
    public ItemStack createPreviewItem(@NotNull CrateContent crateContent) {
        if (crateContent instanceof ItemCrateContent itemCrateContent) {
            return createChancedPreviewItem(itemCrateContent.itemStack(), itemCrateContent.chance());
        } else if (crateContent instanceof EconomyCrateContent economyCrateContent) {
            return createEconomyPreviewItem(economyCrateContent);
        }
        return createChancedPreviewItem(NO_PREVIEW, crateContent.chance());
    }

    /**
     * @param economyCrateContent economyCrateContent
     * @return ItemStack
     */
    public ItemStack createEconomyPreviewItem(EconomyCrateContent economyCrateContent) {
        ItemStack previewItem = NO_PREVIEW;
        if (economyCrateContent instanceof AkaniEconomyCrateContentImpl akaniEconomyCrateContent) {
            if (akaniEconomyCrateContent.economyManager() instanceof ThalerEconomyManager) {
                previewItem = new ItemStack(Material.DIAMOND);
            } else if (akaniEconomyCrateContent.economyManager() instanceof CoinsEconomyManager) {
                previewItem = new ItemStack(Material.GOLD_NUGGET);
            }
        }
        return appendLore(previewItem, List.of(
                Component.empty(),
                chanceComponent(economyCrateContent.chance()),
                economyComponent(economyCrateContent.economyAmount(), economyCrateContent.currencyName()),
                Component.empty()
        ));
    }


    /**
     * @param amount       amount
     * @param currencyName currencyName
     * @return Component with economy information
     */
    private final Component economyComponent(int amount, String currencyName) {
        if (currencyName == null) {
            currencyName = "null";
        }
        return CratesPlugin.miniMessage().deserialize("<yellow><amount> <white><currency>",
                Placeholder.parsed("amount", String.valueOf(amount)),
                Placeholder.parsed("currency", currencyName));
    }

    /**
     * @param chance chance in percent
     * @return Component with chance information
     */
    private Component chanceComponent(float chance) {
        return CratesPlugin.miniMessage().deserialize("<yellow>Chance: <white><chance>%",
                Placeholder.parsed("chance", String.valueOf(chance * 100)));
    }

    /**
     * Appends chance to the item lore
     *
     * @param itemStack item to be cloned
     * @param chance    chance to be appended
     * @return new item with appended chance
     */
    public ItemStack createChancedPreviewItem(ItemStack itemStack, float chance) {
        return appendLore(itemStack, List.of(
                Component.empty(),
                chanceComponent(chance),
                Component.empty()
        ));
    }

    public ItemStack appendLore(@NotNull ItemStack itemStack, @NotNull List<Component> appendingLore) {
        var previewItem = itemStack.clone();
        var itemMeta = previewItem.getItemMeta();
        var newLore = new LinkedList<Component>();
        var oldLore = itemMeta.lore();
        if (oldLore != null) newLore.addAll(oldLore);
        newLore.addAll(appendingLore);
        itemMeta.lore(newLore);
        previewItem.setItemMeta(itemMeta);
        return previewItem;
    }


}
