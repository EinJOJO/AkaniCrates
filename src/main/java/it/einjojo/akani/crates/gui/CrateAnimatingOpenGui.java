package it.einjojo.akani.crates.gui;

import com.github.Anon8281.universalScheduler.scheduling.tasks.MyScheduledTask;
import it.einjojo.akani.crates.CratesPlugin;
import it.einjojo.akani.crates.crate.Crate;
import it.einjojo.akani.crates.crate.content.CrateContent;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.ArrayList;

public class CrateAnimatingOpenGui extends Gui {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Logger log = LoggerFactory.getLogger(CrateAnimatingOpenGui.class);
    private final ArrayList<Icon> spinningIconList = new ArrayList<>();
    private final int[] scrambledIndexes;
    private final Crate crate;
    private final int stopAtDelay = 5 + RANDOM.nextInt(10);
    private int offset = -9;
    private int tickCounter = 0;
    private int delay = 0;
    private int stopTickOffset = 0;
    private MyScheduledTask task;
    private boolean givenReward;
    private @Nullable CrateContent givenRewardObject = null;
    private boolean stopping;


    public CrateAnimatingOpenGui(@NotNull Player player, Crate crate) {
        super(player, "crate_open", crate.title(), 6);
        crate.contents().forEach(content -> {
            if (content.chance() >= RANDOM.nextFloat()) {
                spinningIconList.add(new Icon(content.previewItem()));
            }
        });
        scrambledIndexes = new int[spinningIconList.size()];
        scrambleIconList();
        this.crate = crate;
    }

    private void scrambleIconList() {

        for (int i = 0; i < spinningIconList.size(); i++) {
            int randomIndex = RANDOM.nextInt(spinningIconList.size());
            Icon temp = spinningIconList.get(i);
            spinningIconList.set(i, spinningIconList.get(randomIndex));
            spinningIconList.set(randomIndex, temp);
            int tempIndex = scrambledIndexes[i];
            scrambledIndexes[i] = randomIndex;
            scrambledIndexes[randomIndex] = tempIndex;
        }
    }

    private void hopperIcon() {
        addItem(9 * 2 + 4, new Icon(Material.HOPPER).setName("§6§lDein Gewinn"));
    }

    private float soundPitch() {
        return 0.5f + 2 / ((float) stopAtDelay / delay);
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        hopperIcon();
        if (!givenReward) {
            setActionSlot(new Icon(Material.BARRIER).setName("§cStoppen").onClick((click) -> stopSpinning()));
            updateTask(0, 2, (task) -> {
                this.task = task;
                if (givenReward) {
                    task.cancel();
                    return;
                }
                tickCounter++;
                if (stopTickOffset > 0) {
                    stopTickOffset--;
                }
                if (stopping && stopTickOffset == 0) {
                    if (!(tickCounter % delay == 0)) {
                        return;
                    }
                    tickCounter = 1;
                    delay++;
                    if (delay >= stopAtDelay) {
                        giveReward();
                        getItems().clear();
                        open();
                        return;
                    }
                }
                moveItems();
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_CHIME, 1, soundPitch());
            });
        } else {
            if (givenRewardObject == null) return;
            addItem(9 * 3 + 4, new Icon(givenRewardObject.previewItem()));
            hopperIcon();
            setActionSlot(new Icon(Material.GREEN_WOOL).setName("§aEine weitere Crate öffnen").onClick((click) -> {
                new CrateAnimatingOpenGui(player, crate).open();
            }));
        }
    }

    public void stopSpinning() {
        if (stopping) return;
        stopping = true;
        setActionSlot(new Icon(Material.ORANGE_WOOL).setName("§eVerlangsame..."));
        delay = 1;
        stopTickOffset = RANDOM.nextInt(25);
    }

    public void setActionSlot(Icon icon) {
        addItem(4 * 9 + 4, icon);
    }

    public void moveItems() {
        offset = (offset + 1) % spinningIconList.size();
        for (int i = 0; i < 9; i++) {
            Icon icon = icon((i + offset) % spinningIconList.size());
            if (icon != null) {
                addItem(3 * 9 + i, icon);
            }
        }
    }

    public Icon icon(int slot) {
        return (slot < 0 || slot >= spinningIconList.size()) ? null : spinningIconList.get(slot);
    }

    private void cancelTask(@Nullable MyScheduledTask task) {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        super.onClose(event);
        if (!givenReward) {
            offset = RANDOM.nextInt(spinningIconList.size());
            giveReward();
        }
    }


    private void giveReward() {
        if (givenReward) return;
        givenReward = true;
        player.playSound(player, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, 1);
        cancelTask(task);

        try {

            givenRewardObject = crate.contents().get(scrambledIndexes[(offset + 4) % spinningIconList.size()]);
            givenRewardObject.give(player);
        } catch (Exception e) {
            player.sendMessage(CratesPlugin.miniMessage().deserialize("<prefix><red>Ein Fehler ist aufgetreten!"));
            log.error("An error occurred while giving the player the reward", e);
        }

    }
}