package it.einjojo.akani.crates.crate.content.impl;

import it.einjojo.akani.crates.crate.content.PermissionCrateContent;
import it.einjojo.akani.crates.crate.content.PreviewItemFactory;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.metadata.NodeMetadataKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Range;

import java.time.Instant;

public class LuckPermsPermissionCrateContent implements PermissionCrateContent {
    private final LuckPerms luckPerms;
    private final String permission;
    private final String permissionDescription;
    private final ItemStack previewItem;
    private final Instant expiry;
    private float chance;


    public LuckPermsPermissionCrateContent(LuckPerms luckPerms, String permission, String permissionDescription, PreviewItemFactory previewItemFactory, Instant expiry, float chance) {
        this.luckPerms = luckPerms;
        this.permission = permission;
        this.permissionDescription = permissionDescription;
        this.previewItem = previewItemFactory.createPreviewItem(this);
        this.expiry = expiry;
        this.chance = chance;
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
        var nodeBuilder = Node.builder(permission()).withMetadata(NodeMetadataKey.of("origin", String.class), "crate-content");
        if (expiry != null) nodeBuilder.expiry(expiry.toEpochMilli() / 1000); // epoch seconds required.
        user.data().add(nodeBuilder.build());
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
    public ItemStack previewItem() {
        return previewItem;
    }
}
