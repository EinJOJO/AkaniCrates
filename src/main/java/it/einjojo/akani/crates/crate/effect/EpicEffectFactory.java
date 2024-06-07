package it.einjojo.akani.crates.crate.effect;

import it.einjojo.akani.crates.crate.effect.block.BlockEffect;
import it.einjojo.akani.crates.crate.effect.block.EpicBlockEffect;
import it.einjojo.akani.crates.crate.effect.open.EpicOpeningEffect;
import it.einjojo.akani.crates.crate.effect.open.OpeningEffect;
import mc.obliviate.inventory.Gui;
import org.bukkit.Location;

public class EpicEffectFactory implements EffectFactory {
    @Override
    public BlockEffect createBlockEffect(Location location) {
        return null;
    }

    @Override
    public OpeningEffect createOpeningEffect(Gui gui) {
        return null;
    }
}
