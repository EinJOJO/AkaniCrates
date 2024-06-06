package it.einjojo.akani.crates.gui;

import it.einjojo.akani.crates.crate.Crate;
import mc.obliviate.inventory.Gui;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CratePreviewGui extends Gui {
    public CratePreviewGui(@NotNull Player player, @NotNull Crate crate) {
        super(player, "preview", crate.title(), 6);
    }
}
