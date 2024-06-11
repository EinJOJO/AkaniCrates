package it.einjojo.akani.crates.crate.content;

import it.einjojo.akani.crates.crate.content.impl.AnnouncingContentRarity;
import it.einjojo.akani.crates.crate.content.impl.BasicContentRarity;
import it.einjojo.akani.crates.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.NavigableMap;
import java.util.TreeMap;

import static net.kyori.adventure.text.format.NamedTextColor.*;

public interface ContentRarity {


    ContentRarity NORMAL = new BasicContentRarity(Component.text("Normal", GRAY),
            new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).build());
    ContentRarity UNCOMMON = new BasicContentRarity(Component.text("Ungewöhnlich", GREEN),
            new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).displayName(Component.empty()).build());
    ContentRarity RARE = new BasicContentRarity(Component.text("Selten", WHITE),
            new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).displayName(Component.empty()).build());
    ContentRarity VERY_RARE = new BasicContentRarity(Component.text("Sehr Selten", BLUE),
            new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE).displayName(Component.empty()).build());
    ContentRarity EPIC = new BasicContentRarity(Component.text("Episch", DARK_PURPLE).decorate(TextDecoration.BOLD),
            new ItemBuilder(Material.PURPLE_STAINED_GLASS_PANE).displayName(Component.empty()).build());
    ContentRarity MYTHIC = new BasicContentRarity(Component.text("Mystisch", RED).decorate(TextDecoration.BOLD),
            new ItemBuilder(Material.RED_STAINED_GLASS_PANE).displayName(Component.empty()).build());
    ContentRarity LEGENDARY = new AnnouncingContentRarity(Component.text("Legendär", YELLOW).decorate(TextDecoration.BOLD),
            new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE).displayName(Component.empty()).build());
    ContentRarity GODLY = new AnnouncingContentRarity(Component.text("Göttlich", LIGHT_PURPLE).decorate(TextDecoration.BOLD),
            new ItemBuilder(Material.PINK_STAINED_GLASS_PANE).displayName(Component.empty()).build());

    /**
     * How the ContentRarity should appear in lore.
     */
    Component displayName();

    /**
     * The Material that should be used to represent the ContentRarity
     */
    ItemStack openingTypeIndicator();

    /**
     * What should be done if the items gets pulled
     *
     * @param player the player that pulled the item
     */
    void postGive(Player player, CrateContent content);

}
