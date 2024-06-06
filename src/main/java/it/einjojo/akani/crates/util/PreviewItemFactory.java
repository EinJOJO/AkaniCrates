package it.einjojo.akani.crates.util;

import it.einjojo.akani.crates.crate.content.CrateContent;
import it.einjojo.akani.crates.crate.content.ItemCrateContent;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.LinkedList;

public class PreviewItemFactory {

    public ItemStack createPreviewItem(@NotNull CrateContent crateContent) {
        if (crateContent instanceof ItemCrateContent itemCrateContent) {
            return createPreviewItem(itemCrateContent.itemStack(), itemCrateContent.chance());
        }
        return createPreviewItem(new ItemStack(Material.STONE), crateContent.chance());
    }

    public ItemStack createPreviewItem(ItemStack itemStack, float chance) {
        var previewItem = itemStack.clone();
        var im = previewItem.getItemMeta();
        var lore = new LinkedList<Component>();
        var oldLore = im.lore();
        if (oldLore != null) lore.addAll(oldLore);
        lore.add(Component.empty());
        lore.add(Component.text(MessageFormat.format("Chance {0}%", chance * 100)));
        im.lore(lore);
        previewItem.setItemMeta(im);
        return previewItem;
    }

}
