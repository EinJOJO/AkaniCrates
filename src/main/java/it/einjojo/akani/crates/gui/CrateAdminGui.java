package it.einjojo.akani.crates.gui;

import it.einjojo.akani.crates.crate.Crate;
import it.einjojo.akani.crates.crate.content.CrateContent;
import it.einjojo.akani.crates.input.ChatInput;
import mc.obliviate.inventory.ComponentIcon;
import mc.obliviate.inventory.Gui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CrateAdminGui extends Gui {
    private final CrateContentPreview crateContentPreview;
    private final Crate crate;

    public CrateAdminGui(@NotNull Player player, Crate crate) {
        super(player, "crate_admin", Component.text("Administrate Crate", NamedTextColor.GOLD), 6);
        crateContentPreview = new CrateContentPreview(crate, this);
        crateContentPreview.setPreviewIconModifier(this::modifyContentIcon);
        this.crate = crate;

    }

    private void modifyContentIcon(CrateContent content, ComponentIcon componentIcon) {
        componentIcon.appendLore(List.of(
                        Component.empty(),
                        Component.text("Linksklick um Chance anzupassen", NamedTextColor.YELLOW),
                        Component.text("Rechtsklick zum Löschen", NamedTextColor.RED),
                        Component.empty()
                )
        );
        componentIcon.onClick((event) -> {
            if (event.isRightClick()) {
                crateContentPreview.paginationManager().getItems().remove(componentIcon.toIcon());
                crateContentPreview.paginationManager().update();
                crate.contents().remove(content);
                player.sendMessage("Du hast das Item entfernt");
            } else if (event.isLeftClick()) {
                player.closeInventory();
                chanceInput(content);
            }
        });
    }

    private void chanceInput(CrateContent content) {
        ChatInput.builder()
                .plugin((JavaPlugin) getPlugin())
                .player(player)
                .onInput((input) -> {
                    try {
                        float f = Float.min(100, Float.max(0, Float.parseFloat(input)));
                        content.setChance(f / 100);
                        player.sendMessage("Chance gesetzt auf " + f);

                    } catch (NumberFormatException ex) {
                        player.sendMessage("Bitte eine Zahl zwischen 0 und 100 eingeben");
                    }
                    runTaskLater(1, myScheduledTask -> {
                        new CrateAdminGui(player, crate).open();
                    });
                })
                .promptHeader(Component.text("Chance eingeben"))
                .promptFooter(Component.text("0 - 100 oder cancel zum abbrechen"))
                .build().start();
    }


    @Override
    public void onOpen(InventoryOpenEvent event) {
        crateContentPreview.placeContents();
        addItemButton();
    }

    public void addItemButton() {

    }
}