package it.einjojo.akani.crates.crate.effect;

import it.einjojo.akani.crates.crate.effect.block.BlockEffect;
import it.einjojo.akani.crates.crate.effect.open.OpeningEffect;
import mc.obliviate.inventory.Gui;
import org.bukkit.Location;

public class NullEffectFactory implements EffectFactory {
    @Override
    public BlockEffect createBlockEffect(Location location) {
        return new BlockEffect() {
            @Override
            public void tick() {

            }
        };
    }

    @Override
    public OpeningEffect createOpeningEffect(Gui gui) {
        return new OpeningEffect() {
            @Override
            public Gui gui() {
                return null;
            }

            @Override
            public void onStart() {

            }

            @Override
            public void tick() {

            }

            @Override
            public void onEnd() {

            }
        };
    }
}
