package it.einjojo.akani.crates.gui;

import com.github.Anon8281.universalScheduler.scheduling.tasks.MyScheduledTask;
import it.einjojo.akani.crates.CratesPlugin;
import it.einjojo.akani.crates.crate.Crate;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.ArrayList;

public class CrateAnimatingOpenGui extends Gui {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Logger log = LoggerFactory.getLogger(CrateAnimatingOpenGui.class);
    private final ArrayList<Icon> spinning = new ArrayList<>();
    private final Crate crate;
    private final int MAX_UPDATES;
    private boolean givenReward = false;
    private int updateCounter = 0;
    private int offset = 0;
    private MyScheduledTask task;
    private MyScheduledTask stoppingTask;
    private int interval = 1;

    public CrateAnimatingOpenGui(@NotNull Player player, Crate crate) {
        super(player, "crate_open", crate.title(), 6);
        crate.contents().forEach(content -> spinning.add(new Icon(content.previewItem())));
        this.MAX_UPDATES = spinning.size() + RANDOM.nextInt(spinning.size());
        this.crate = crate;
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        cancelTask(task);
        addItem(0, new Icon(Material.BARRIER).onClick((click) -> stopSpinning()));
        if (interval < 32 && !givenReward) {
            updateTask(0, interval, (task) -> {
                if (givenReward) {
                    task.cancel();
                    return;
                }
                this.task = task;
                moveItems();
                updateCounter++;
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 1);
                if (updateCounter >= MAX_UPDATES && !givenReward) {
                    stopSpinning();
                }
            });
        } else {
            giveReward();
        }
    }

    public void stopSpinning() {
        if (givenReward || stoppingTask != null) {
            return;
        }
        updateTask(RANDOM.nextInt(20), RANDOM.nextInt(15) + 15, (stopperTask) -> {
            this.stoppingTask = stopperTask;
            if (givenReward) {
                stopperTask.cancel();
                return;
            }
            interval += RANDOM.nextInt(8) + 1;
            open();
        });
    }

    public void moveItems() {
        offset = (offset + 1) % spinning.size();
        for (int i = 0; i < 9; i++) {
            Icon icon = icon((i + offset) % spinning.size());
            if (icon != null) {
                addItem(3 * 9 + i, icon);
            }
        }
    }

    public Icon icon(int slot) {
        return (slot < 0 || slot >= spinning.size()) ? null : spinning.get(slot);
    }

    private void cancelTask(MyScheduledTask task) {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        if (!givenReward && stoppingTask == null) {
            offset = RANDOM.nextInt(spinning.size());
            giveReward();
        }
    }

    private void giveReward() {
        if (givenReward) return;
        givenReward = true;
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 1);
        cancelTask(stoppingTask);
        cancelTask(task);
        var gui = getItems().get(9 * 3 + 4);
        getItems().clear();
        addItem(9 * 3 + 4, gui);
        try {
            crate.contents().get(offset + 4).give(player);
        } catch (Exception e) {
            player.sendMessage(CratesPlugin.miniMessage().deserialize("<prefix><red>Ein Fehler ist aufgetreten!"));
            log.error("An error occurred while giving the player the reward", e);
        }
    }
}