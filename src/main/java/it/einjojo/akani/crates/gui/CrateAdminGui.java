package it.einjojo.akani.crates.gui;

import it.einjojo.akani.crates.CratesPlugin;
import it.einjojo.akani.crates.crate.Crate;
import it.einjojo.akani.crates.crate.content.CrateContent;
import it.einjojo.akani.crates.crate.content.CrateContentFactory;
import it.einjojo.akani.crates.input.ChatInput;
import it.einjojo.akani.crates.input.CrateContentInput;
import mc.obliviate.inventory.ComponentIcon;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
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
                        CratesPlugin.miniMessage().deserialize("<gray>[<yellow>Linksklick</yellow>] Chance ändern</gray>"),
                        CratesPlugin.miniMessage().deserialize("<gray>[<red>Rechtsklick</red>] Entfernen</gray>"),
                        Component.empty()
                )
        );
        componentIcon.onClick((event) -> {
            if (event.isRightClick()) {
                crateContentPreview.paginationManager().getItems().remove(componentIcon.toIcon());
                crate.contents().remove(content);
                player.sendMessage("Du hast das Item entfernt");
                open();
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
                        player.sendMessage(CratesPlugin.miniMessage().deserialize("<gray>Chance auf <yellow><chance>",
                                Placeholder.parsed("chance", String.format("%.2f", f))));
                        runTaskLater(1, myScheduledTask -> {
                            open();
                        });
                    } catch (NumberFormatException ex) {
                        player.sendMessage(CratesPlugin.miniMessage().deserialize("<gray>Bitte eine Zahl zwischen <yellow>0</yellow> und <yellow>100</yellow> eingeben</gray>"));
                    }

                })
                .promptHeader(Component.text("Eine Chance eingeben", NamedTextColor.GRAY))
                .promptFooter(Component.text("0 - 100 oder cancel zum abbrechen", NamedTextColor.GRAY))
                .build().start();
    }


    @Override
    public void onOpen(InventoryOpenEvent event) {
        crateContentPreview.placeContents();
        addItemButton();
    }

    public void addItemButton() {
        addItem(48, new Icon(Heads.ADD.skull()).toComp().appendLore(Component.text("Klicke um neues Item hinzufügen", NamedTextColor.GREEN))
                .onClick((event) -> {
                    player.closeInventory();
                    new CrateContentInput((JavaPlugin) getPlugin(), player, (contents) -> {
                        runTaskLater(1, (task) -> {
                            crate.contents().addAll(contents);
                            open();
                        });
                    }, new CrateContentFactory()).start();
                }));
    }


}