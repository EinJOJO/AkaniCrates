package it.einjojo.akani.crates.input;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public record ChatInput(Player player, JavaPlugin plugin, Consumer<String> onInput, @Nullable Runnable onCancel, Prompt prompt) implements Listener {
    private static final Map<UUID, ChatInput> inputSessions = new HashMap<>();
    private static final PlainTextComponentSerializer plainTextSerializer = PlainTextComponentSerializer.plainText();

    public static ChatInputBuilder builder() {
        return new ChatInputBuilder.Impl();
    }

    private void close() {
        HandlerList.unregisterAll(this);
        if (prompt != null) {
            prompt.stop();
        }
        inputSessions.remove(player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncChatEvent event) {
        event.setCancelled(true);
        String message = plainTextSerializer.serialize(event.originalMessage());
        if (message.equalsIgnoreCase("cancel")) {
            cancel();
        } else {
            accept(message);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        close();
    }

    public void cancel() {
        close();
        if (onCancel != null) {
            onCancel.run();
        }
    }

    public void accept(String input) {
        close();
        if (onInput != null) {
            onInput.accept(input);
        }
    }

    public void start() {
        ChatInput oldActive = inputSessions.put(player.getUniqueId(), this);
        if (oldActive != null) {
            oldActive.cancel();
        }
        if (prompt != null) {
            prompt.show();
        }
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }





}
