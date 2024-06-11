package it.einjojo.akani.crates.crate.content.impl;

import it.einjojo.akani.crates.crate.content.ContentRarity;
import it.einjojo.akani.crates.crate.content.PermissionCrateContent;
import it.einjojo.akani.crates.crate.content.PreviewItemFactory;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.metadata.NodeMetadataKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;

public class LuckPermsPermissionCrateContentImpl implements PermissionCrateContent {
    private static final Logger log = LoggerFactory.getLogger(LuckPermsPermissionCrateContentImpl.class);
    private final LuckPerms luckPerms;
    private final String permission;
    private final String permissionDescription;
    private final ItemStack previewItem;
    private final Duration expiry;
    private float chance;
    private ContentRarity rarity;

    public LuckPermsPermissionCrateContentImpl(LuckPerms luckPerms, String permission, String permissionDescription, PreviewItemFactory previewItemFactory, Duration expiry, float chance, ContentRarity rarity) {
        this.luckPerms = luckPerms;
        this.permission = permission;
        this.permissionDescription = permissionDescription;
        this.expiry = expiry;
        this.chance = chance;
        this.rarity = rarity;
        this.previewItem = previewItemFactory.createPreviewItem(this);
    }

    @Override
    public String permission() {
        return permission;
    }

    @Override
    public String permissionDescription() {
        return permissionDescription;
    }

    @Override
    public void give(Player player) {
        User user = luckPerms.getPlayerAdapter(Player.class).getUser(player);
        //TODO overwriting permissions or throw exceptions if the permission is already present
        var nodeBuilder = Node.builder(permission()).withMetadata(NodeMetadataKey.of("origin", String.class), "crate-content");
        if (expiry != null)
            nodeBuilder.expiry(Instant.now().plus(expiry).toEpochMilli() / 1000); // epoch seconds required.
        user.data().add(nodeBuilder.build());
        log.info("Gave permission {} to player {}", permission(), player.getName());
        luckPerms.getUserManager().saveUser(user);
    }

    @Override
    public @Range(from = 0, to = 1) float chance() {
        return chance;
    }

    @Override
    public void setChance(@Range(from = 0, to = 1) float chance) {
        this.chance = chance;
    }

    @Override
    public ContentRarity rarity() {
        return rarity;
    }

    @Override
    public Component displayName() {
        return Component.text(permission());

    }

    @Override
    public void setRarity(ContentRarity rarity) {
        this.rarity = rarity;
    }

    @Override
    public ItemStack previewItem() {
        return previewItem;
    }
}
