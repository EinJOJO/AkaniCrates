package it.einjojo.akani.crates.gui;

import mc.obliviate.inventory.Gui;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public enum GuiSound {
    GOOD_CLICK(Sound.UI_BUTTON_CLICK),
    BAD_CLICK(Sound.ENTITY_ITEM_BREAK);


    public final Sound sound;

    GuiSound(Sound sound) {
        this.sound = sound;
    }

    public void play(Player player) {
        player.playSound(player.getLocation(), sound, 1, 1.8f);
    }

    public void play(Gui gui) {
        play(gui.player);
    }
}
