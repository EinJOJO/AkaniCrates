package it.einjojo.akani.crates.input;

import it.einjojo.akani.crates.CratesPlugin;
import it.einjojo.akani.crates.crate.content.CrateContent;
import it.einjojo.akani.crates.crate.content.CrateContentFactory;
import it.einjojo.akani.crates.crate.content.PermissionCrateContent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class CrateContentInput {

    private final JavaPlugin plugin;
    private final Player player;
    private final Consumer<List<CrateContent>> crateContentConsumer;
    private final CrateContentFactory crateContentFactory;
    private final List<CrateContent> crateContentList = new LinkedList<>();

    public CrateContentInput(JavaPlugin plugin, Player player, Consumer<List<CrateContent>> crateContentConsumer, CrateContentFactory crateContentFactory) {
        this.plugin = plugin;
        this.player = player;
        this.crateContentConsumer = crateContentConsumer;
        this.crateContentFactory = crateContentFactory;
    }


    public void start() {
        typeSelector();
    }

    private void typeSelector() {
        player.sendMessage(Component.empty());
        sendMessage("<gray>Wähle den Typen des Inhalts aus:");
        sendMessage("<dark_gray> - <gray>1. <green>Item");
        sendMessage("<dark_gray> - <gray>2. <red>Permission");
        sendMessage("<dark_gray> - <gray>3. <yellow>Coins");
        sendMessage("<dark_gray> - <gray>4. <gold>Taler");
        sendMessage("<dark_gray> - <gray>5. <green>Setup beenden");
        player.sendMessage(Component.empty());
        createChatInputBuilder().onInput((input) -> {
            switch (input) {
                case "1" -> runTask(this::itemSetup);
                case "2" -> runTask(this::permissionSetup);
                case "3" -> runTask(this::coinsSetup);
                case "4" -> runTask(this::talerSetup);
                case "5" -> {
                    sendMessage(MessageFormat.format("<green>Setup beendet mit {0} neuen Items", crateContentList.size()));
                    crateContentConsumer.accept(crateContentList);
                }
                default -> runTask(this::typeSelector);
            }
        }).onCancel(() -> runTask(this::typeSelector)).build().start();
    }

    private void sendMessage(String miniMessage) {
        player.sendMessage(CratesPlugin.miniMessage().deserialize("<gray>[<red>Content Creator<gray>] <white>" + miniMessage));
    }

    private void chanceInput(Consumer<Float> chanceConsumer) {
        sendMessage("<gray>Bitte gib die Chance ein (0-100):");
        createChatInputBuilder().onInput((input) -> {
            try {
                float f = Float.min(100, Float.max(0, Float.parseFloat(input)));
                sendMessage("<gray>Chance auf <yellow>" + f + "%");
                chanceConsumer.accept(f / 100);
            } catch (NumberFormatException e) {
                sendMessage("<red>Bitte eine Zahl zwischen <yellow>0</yellow> und <yellow>100</yellow> eingeben");
                runTask(() -> chanceInput(chanceConsumer));
            }
        }).onCancel(() -> runTask(this::typeSelector)).build().start();
    }

    private void itemSetup() {
        sendMessage("<b>Halte das Item in der Hand und gib die Chance an</b>");
        chanceInput((chance) -> {
            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            if (itemInHand.getType().isAir()) {
                sendMessage("<red>Du musst ein Item in der Hand halten.");
                runTask(this::itemSetup);
                return;
            }
            crateContentList.add(crateContentFactory.itemContent(itemInHand.clone(), chance));
            sendMessage(MessageFormat.format("<green>Item {0} <gray>x</gray> {1} <gray>hinzugefügt", itemInHand.getAmount(), itemInHand.getType()));
            runTask(this::typeSelector);
        });

    }

    private void permissionSetup() {
        sendMessage("<gray> 1/4 Bitte gib die Permission ein:");
        createChatInputBuilder().onInput((permission) -> {
            if (permission.isEmpty() || permission.contains(" ")) {
                sendMessage("<red>Bitte gib eine gültige Permission an");
                runTask(this::permissionSetup);
                return;
            }
            sendMessage("<green>1/4 Permission <yellow>" + permission + "<green> ausgewählt");
            runTask(() -> permissionSetupTwo(permission));
        }).onCancel(() -> runTask(this::typeSelector)).build().start();
    }

    private void permissionSetupTwo(String permission) {
        sendMessage("<gray> 2/4 Was kann man mit der Permission machen? [VORSCHAU] (z.B 'Du erhälst den Rang VIP'");
        createChatInputBuilder().onInput((description) -> {
            sendMessage("<green>2/4 Beschreibung <yellow>" + description);
            runTask(() -> permissionSetupThree(permission, description));
        }).onCancel(() -> runTask(this::typeSelector)).build().start();
    }

    private void permissionSetupThree(String permission, String permissionDescription) {
        sendMessage("Gib die Auslaufzeit der Permission an (in Sekunden) oder 0 für unbegrenzt:");
        sendMessage("Benötigte Endungen: s, m, h, d, w");
        createChatInputBuilder().onInput((durationString) -> {
            if (!durationString.equals("0")) {
                try {
                    sendMessage("<gray>Die Permission ist für <yellow>" + durationString + "<gray> gültig");
                    runTask(() -> permissionSetupFour(permission, permissionDescription, Duration.parse("PT" + durationString.toUpperCase())));
                } catch (Exception e) {
                    sendMessage("<red>Bitte gib eine gültige Dauer an");
                    runTask(() -> permissionSetupThree(permission, permissionDescription));
                    return;
                }
            } else {
                sendMessage("<gray>Die Permission ist unbegrenzt gültig");
                runTask(() -> permissionSetupFour(permission, permissionDescription, null));
            }
        }).onCancel(() -> runTask(this::typeSelector)).build().start();
    }

    private void permissionSetupFour(String permission, String permissionDescription, @Nullable Duration duration) {
        sendMessage("<gray> 4/4 Bitte gib die Chance an:");
        chanceInput((chance) -> {
            PermissionCrateContent permissionCrateContent;
            if (duration == null) {
                sendMessage("<gray>Die Permission ist unbegrenzt gültig");
                permissionCrateContent = crateContentFactory.permissionContent(permission, permissionDescription, chance);
            } else {
                sendMessage(MessageFormat.format("<gray>Die Permission ist für <yellow>{0}</yellow> gültig", duration));
                permissionCrateContent = crateContentFactory.expiringPermissionContent(permission, permissionDescription, chance, duration);
            }
            sendMessage("<green>4/4 Permission <yellow>" + permission + "<green> hinzugefügt");
            crateContentList.add(permissionCrateContent);
            runTask(this::typeSelector);
        });
    }

    private void coinsSetup() {
        sendMessage("<gray> 1/2 Bitte gib die Anzahl an Coins ein:");
        createChatInputBuilder().onInput((input) -> {
            try {
                int amount = Integer.parseInt(input);
                sendMessage("<green>1/2 Coins <yellow>" + amount + "<green> ausgewählt");
                runTask(() -> coinsSetupTwo(amount));
            } catch (NumberFormatException e) {
                sendMessage("<red>Bitte gib eine gültige Zahl an");
                runTask(this::coinsSetup);
            }
        }).onCancel(() -> runTask(this::typeSelector)).build().start();
    }

    private void coinsSetupTwo(int amount) {
        sendMessage("<gray> 2/2 Bitte gib die Chance an:");
        chanceInput((chance) -> {
            crateContentList.add(crateContentFactory.coinsContent(amount, chance));
            sendMessage(MessageFormat.format("<green>2/2 Coins <yellow>{0} <green>hinzugefügt", amount));
            runTask(this::typeSelector);
        });
    }

    private void talerSetup() {
        sendMessage("<gray> 1/2 Bitte gib die Anzahl an Talern ein:");
        createChatInputBuilder().onInput((input) -> {
            try {
                int amount = Integer.parseInt(input);
                sendMessage("<green>1/2 Taler <yellow>" + amount + "<green> ausgewählt");
                runTask(() -> talerSetupTwo(amount));
            } catch (NumberFormatException e) {
                sendMessage("<red>Bitte gib eine gültige Zahl an");
                runTask(this::talerSetup);
            }
        }).onCancel(() -> runTask(this::typeSelector)).build().start();
    }

    private void talerSetupTwo(int amount) {
        sendMessage("<gray> 2/2 Bitte gib die Chance an:");
        chanceInput((chance) -> {
            crateContentList.add(crateContentFactory.thalerContent(amount, chance));
            sendMessage(MessageFormat.format("<green>2/2 Taler <yellow>{0} <green>hinzugefügt", amount));
            runTask(this::typeSelector);
        });
    }

    private ChatInputBuilder.B createChatInputBuilder() {
        return ChatInput.builder().plugin(plugin).player(player);
    }

    private void runTask(Runnable runnable) {
        plugin.getServer().getScheduler().runTask(plugin, runnable);
    }


}
