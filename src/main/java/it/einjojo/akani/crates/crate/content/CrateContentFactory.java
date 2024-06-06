package it.einjojo.akani.crates.crate.content;

import it.einjojo.akani.crates.crate.content.impl.ItemCrateContentImpl;
import it.einjojo.akani.crates.util.PreviewItemFactory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public class CrateContentFactory {
    private PreviewItemFactory previewItemFactory = new PreviewItemFactory();

    public PreviewItemFactory previewItemFactory() {
        return previewItemFactory;
    }

    public void setPreviewItemFactory(PreviewItemFactory previewItemFactory) {
        this.previewItemFactory = previewItemFactory;
    }

    public EconomyCrateContent thalerContent(@Range(from = 0, to = Integer.MAX_VALUE) int amount, @Range(from = 0, to = 1) float chance) {
        return null;
    }

    public ItemCrateContent itemContent(@NotNull ItemStack itemStack, @Range(from = 0, to = 1) float chance) {
        return new ItemCrateContentImpl(itemStack, Float.max(0, Float.min(1, chance)), previewItemFactory);
    }

}
