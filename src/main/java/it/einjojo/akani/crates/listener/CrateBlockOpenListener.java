package it.einjojo.akani.crates.listener;

import it.einjojo.akani.crates.crate.Crate;
import it.einjojo.akani.crates.crate.CrateLocationRegistry;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CrateBlockOpenListener implements Listener {

    private final CrateLocationRegistry crateLocationRegistry;

    public CrateBlockOpenListener(CrateLocationRegistry crateLocationRegistry, JavaPlugin plugin) {
        this.crateLocationRegistry = crateLocationRegistry;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onCrateBlockTouch(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) {
            return;
        }
        Block block = event.getClickedBlock();
        if (block == null) return;
        Crate crate = crateLocationRegistry.crateByLocation(block.getLocation());
        if (crate == null) return;
        event.setUseInteractedBlock(PlayerInteractEvent.Result.DENY);
        event.setUseItemInHand(PlayerInteractEvent.Result.DENY);
        event.setCancelled(true);
        crate.preview(event.getPlayer());
    }


}
