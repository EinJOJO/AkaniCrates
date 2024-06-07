package it.einjojo.akani.crates.crate.effect;

import it.einjojo.akani.crates.crate.effect.block.BlockEffect;
import it.einjojo.akani.crates.crate.effect.open.OpeningEffect;
import mc.obliviate.inventory.Gui;
import org.bukkit.Location;

public interface EffectFactory {

    BlockEffect createBlockEffect(Location location);

    OpeningEffect createOpeningEffect(Gui gui);

}
