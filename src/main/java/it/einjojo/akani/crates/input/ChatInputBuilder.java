package it.einjojo.akani.crates.input;

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
        void onCancel(Runnable runnable);
        void prompt(Prompt prompt);
        ChatInput build();
    }

    class Impl implements ChatInputBuilder, A, B, C {
        private Player player;
        private JavaPlugin plugin;
        private Runnable onCancel;
        private Consumer<String> handler;
        private Prompt prompt;

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
        public void onCancel(Runnable runnable) {
            this.onCancel = runnable;
        }

        @Override
        public void prompt(Prompt prompt) {
            this.prompt = prompt;
        }

        @Override
        public ChatInput build() {
            return new ChatInput(player, plugin, handler, onCancel, prompt);
        }
    }
}