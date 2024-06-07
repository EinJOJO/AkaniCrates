package it.einjojo.akani.crates.crate;

import it.einjojo.akani.crates.crate.content.CrateContent;
import it.einjojo.akani.crates.crate.effect.EffectFactory;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

public interface Crate {

    String id();

    Component title();

    void open(Player player);

    void preview(Player player);

    List<CrateContent> contents();

    EffectFactory effectFactory();

}
