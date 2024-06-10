package it.einjojo.akani.crates.gui;

import it.einjojo.akani.crates.crate.Crate;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

public class CratePreviewGui extends Gui {
    private final CrateContentPreview contentPreview;
    private final Crate crate;

    public CratePreviewGui(@NotNull Player player, @NotNull Crate crate) {
        super(player, "preview", crate.title(), 6);
        this.contentPreview = new CrateContentPreview(crate, this);
        this.crate = crate;
    }


    @Override
    public void onOpen(InventoryOpenEvent openEvent) {
        contentPreview.placeContents();
        Icon icon = new Icon(Material.GREEN_WOOL)
                .setName("§aCrate öffnen")
                .setLore("§7Klicke um die Kiste zu öffnen")
                .onClick((clickEvent) -> {
                    crate.open(player);
                });
        addItem(3, icon);
        addItem(4, icon);
        addItem(5, icon);
    }


}
