package it.einjojo.akani.crates.crate.content;

import it.einjojo.akani.crates.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Factory for creating preview items for crate content
 */
public interface PreviewItemFactory {
    ItemStack NO_PREVIEW = new ItemBuilder(Material.STRUCTURE_VOID)
            .displayName(Component.text("Leere Vorschau", NamedTextColor.RED))
            .lore(List.of(
                    Component.text("Kein Preview-Item konnte", NamedTextColor.RED),
                    Component.text("mithilfe der Factory erstellt werden", NamedTextColor.RED))
            ).build();

    ItemStack createPreviewItem(@NotNull CrateContent crateContent);
}
