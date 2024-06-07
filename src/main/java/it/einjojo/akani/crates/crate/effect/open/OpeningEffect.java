package it.einjojo.akani.crates.crate.effect.open;

import mc.obliviate.inventory.Gui;

public interface OpeningEffect {

    Gui gui();

    /**
     * Called once the crate opening has started.
     */
    void onStart();

    /**
     * Called every tick during the opening process
     */
    void tick();

    /**
     * Called once the opening process has ended.
     */
    void onEnd();

}
