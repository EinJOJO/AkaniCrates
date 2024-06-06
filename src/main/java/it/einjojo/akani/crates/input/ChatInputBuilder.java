package it.einjojo.akani.crates.input;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public interface ChatInputBuilder {

    A plugin(JavaPlugin plugin);

    interface A {
        B player(Player player);
    }

    interface B {
        C onInput(Consumer<String> inputHandler);
    }

    interface C {
        C onCancel(Runnable runnable);
        C promptHeader(Component component);
        C promptFooter(Component component);
        ChatInput build();
    }


    class Impl implements ChatInputBuilder, A, B, C {
        private Player player;
        private JavaPlugin plugin;
        private Runnable onCancel;
        private Consumer<String> handler;
        private Component promptFooter;
        private Component promptHeader;

        @Override
        public A plugin(JavaPlugin plugin) {
            this.plugin = plugin;
            return this;
        }

        @Override
        public B player(Player player) {
            this.player = player;
            return this;
        }

        @Override
        public C onInput(Consumer<String> inputHandler) {
            handler = inputHandler;
            return this;
        }

        @Override
        public C onCancel(Runnable runnable) {
            this.onCancel = runnable;
            return this;
        }


        @Override
        public C promptHeader(Component component) {
            promptHeader = component;
            return this;
        }

        @Override
        public C promptFooter(Component component) {
            promptFooter = component;
            return this;
        }

        @Override
        public ChatInput build() {
            Prompt prompt = null;
            if (promptFooter != null || promptHeader  != null) {
                if (promptFooter == null) promptFooter = Component.empty();
                if (promptHeader == null) promptHeader = Component.empty();
                prompt = new Prompt(player, promptHeader, promptFooter, plugin);
            }
            return new ChatInput(player, plugin, handler, onCancel, prompt);
        }
    }
}