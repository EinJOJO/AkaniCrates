package it.einjojo.akani.crates.gui;

import com.github.Anon8281.universalScheduler.scheduling.tasks.MyScheduledTask;
import it.einjojo.akani.crates.CratesPlugin;
import it.einjojo.akani.crates.crate.Crate;
import it.einjojo.akani.crates.crate.content.CrateContent;
import it.einjojo.akani.crates.util.IconTuple;
import it.einjojo.akani.crates.util.ScrambledArrayList;
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
import java.util.LinkedList;

public class CrateAnimatingOpenGui extends Gui {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Logger log = LoggerFactory.getLogger(CrateAnimatingOpenGui.class);
    private static Sound CRATE_OPEN_SOUND = Sound.BLOCK_NOTE_BLOCK_CHIME;
    private final ArrayList<CrateContent> availableRewards = new ArrayList<>();
    private final ScrambledArrayList<IconTuple> scrambledArrayList;
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
        LinkedList<IconTuple> icons = new LinkedList<>();
        crate.contents().forEach(content -> {
            if (content.chance() >= RANDOM.nextFloat()) {
                availableRewards.add(content);
                icons.add(new IconTuple(
                        new Icon(content.previewItem()),
                        new Icon(content.rarity().openingTypeIndicator()
                        )));
            }
        });
        scrambledArrayList = new ScrambledArrayList<>(icons);
        this.crate = crate;
    }

    public static void setSound(Sound s) {
        CRATE_OPEN_SOUND = s;
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
                player.playSound(player, CRATE_OPEN_SOUND, 1, soundPitch());
            });
        } else {
            if (givenRewardObject == null) return;
            addItem(9 * 3 + 4, new Icon(givenRewardObject.previewItem()));
            hopperIcon();
            setActionSlot(new Icon(Material.GREEN_WOOL).setName("§aEine weitere Crate öffnen").onClick((click) -> {
                GuiSound.GOOD_CLICK.play(this);
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
        GuiSound.GOOD_CLICK.play(this);
    }

    public void setActionSlot(Icon icon) {
        addItem(4 * 9 + 4, icon);
    }

    public void moveItems() {
        offset = (offset + 1) % scrambledArrayList.size();
        for (int i = 0; i < 9; i++) {
            IconTuple iconTuple = icon((i + offset) % scrambledArrayList.size());
            if (iconTuple != null) {
                addItem(3 * 9 + i, iconTuple.first());
                addItem(4 * 9 + i, iconTuple.second());
            }
        }
    }

    public IconTuple icon(int slot) {
        return (slot < 0 || slot >= scrambledArrayList.size()) ? null : scrambledArrayList.get(slot);
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
            offset = RANDOM.nextInt(scrambledArrayList.size());
            giveReward();
        }
    }


    private void giveReward() {
        if (givenReward) return;
        givenReward = true;
        player.playSound(player, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, 1);
        cancelTask(task);
        try {
            givenRewardObject = availableRewards.get(scrambledArrayList.getOriginalIndexByScrambledIndex((offset + 4) % scrambledArrayList.size()));
            givenRewardObject.give(player);
            givenRewardObject.rarity().postGive(player, givenRewardObject);
        } catch (Exception e) {
            player.sendMessage(CratesPlugin.miniMessage().deserialize("<prefix><red>Ein Fehler ist aufgetreten!"));
            log.error("An error occurred while giving the player the reward", e);
        }

    }
}