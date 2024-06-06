package it.einjojo.akani.crates.gui;

import com.github.Anon8281.universalScheduler.scheduling.tasks.MyScheduledTask;
import it.einjojo.akani.crates.crate.Crate;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CrateAnimatingOpenGui extends Gui {
    private static final int shiftingRows = 3;
    private final Queue<Icon> shiftingIcons = new ConcurrentLinkedQueue<>();
    private MyScheduledTask task;

    public CrateAnimatingOpenGui(@NotNull Player player, Crate crate) {
        super(player, "crate_open", crate.title(), 6);
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        updateTask(0, 6, (task) -> {
            setTask(task);
            moveItems();
            animateBackgroundRandomly();
        });
    }

    public void setTask(MyScheduledTask task) {
        if (this.task != null) {
            this.task = task;
        }
    }

    public void moveItems() {
        int i = 9;
        Icon outOfView = shiftingIcons.poll();
    }

    public void animateBackgroundRandomly() {

    }
}
