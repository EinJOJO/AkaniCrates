package it.einjojo.akani.crates.gui;

import it.einjojo.akani.crates.crate.Crate;
import mc.obliviate.inventory.Gui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

public class CrateAdminGui extends Gui {
    private final CrateContentPreview crateContentPreview;

    public CrateAdminGui(@NotNull Player player, Crate crate) {
        super(player, "crate_admin", Component.text("Administrate Crate", NamedTextColor.GOLD), 6);
        crateContentPreview = new CrateContentPreview(crate, this);

    }



    @Override
    public void onOpen(InventoryOpenEvent event) {
         crateContentPreview.placeContents();
         addItemButton();
    }
    public void addItemButton() {

    }
}
