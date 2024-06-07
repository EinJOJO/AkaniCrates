package it.einjojo.akani.crates.crate.impl;

import it.einjojo.akani.crates.crate.content.CrateContent;
import it.einjojo.akani.crates.crate.Crate;
import it.einjojo.akani.crates.crate.effect.EffectFactory;
import it.einjojo.akani.crates.gui.CrateAnimatingOpenGui;
import it.einjojo.akani.crates.gui.CratePreviewGui;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

public record InventoryCrateImpl(String id, Component title, List<CrateContent> contents, EffectFactory effectFactory) implements Crate {

    @Override
    public void open(Player player) {
        new CrateAnimatingOpenGui(player, this).open();
    }

    @Override
    public void preview(Player player) {
        new CratePreviewGui(player, this).open();
    }

}
