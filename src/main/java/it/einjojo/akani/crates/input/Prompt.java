package it.einjojo.akani.crates.input;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;

public class Prompt {
    private final Title title;
    private final JavaPlugin plugin;
    private final Player player;
    private BukkitTask task;

    public Prompt(Player player, Component header, Component footer, JavaPlugin plugin) {
        title = Title.title(header, footer, Title.Times.times(Duration.ofMillis(100), Duration.ofMillis(500), Duration.ofMillis(100)));
        this.plugin = plugin;
        this.player = player;
    }

    public void show() {
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            player.showTitle(title);
        }, 0, 10);
    }

    public void stop() {
        if (task != null) {
            task.cancel();
        }
    }
}
