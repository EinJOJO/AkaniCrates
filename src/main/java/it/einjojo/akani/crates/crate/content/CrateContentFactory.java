package it.einjojo.akani.crates.crate.content;

import it.einjojo.akani.core.api.AkaniCoreProvider;
import it.einjojo.akani.crates.crate.content.impl.*;
import it.einjojo.akani.crates.util.AkaniUtil;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.time.Duration;

public class CrateContentFactory {
    private PreviewItemFactoryImpl previewItemFactory = new PreviewItemFactoryImpl();

    public PreviewItemFactoryImpl previewItemFactory() {
        return previewItemFactory;
    }

    public void setPreviewItemFactory(PreviewItemFactoryImpl previewItemFactory) {
        this.previewItemFactory = previewItemFactory;
    }

    public EconomyCrateContent thalerContent(@Range(from = 0, to = Integer.MAX_VALUE) int amount, @Range(from = 0, to = 1) float chance) {
        if (AkaniUtil.isAvailable()) {
            return new AkaniEconomyCrateContentImpl(amount, chance, AkaniCoreProvider.get().thalerManager(),
                    previewItemFactory, "Taler", rarity(chance));
        }
        return new NullEconomyCrateContentImpl(amount, chance, previewItemFactory, "NullTaler",
                rarity(chance));
    }

    public EconomyCrateContent coinsContent(@Range(from = 0, to = Integer.MAX_VALUE) int amount,
                                            @Range(from = 0, to = 1) float chance) {
        if (AkaniUtil.isAvailable()) {
            return new AkaniEconomyCrateContentImpl(amount, chance, AkaniCoreProvider.get().coinsManager(),
                    previewItemFactory, "Coins", rarity(chance));
        }
        return new NullEconomyCrateContentImpl(amount, chance, previewItemFactory, "NullCoins",
                rarity(chance));
    }

    public ItemCrateContent itemContent(@NotNull ItemStack itemStack, @Range(from = 0, to = 1) float chance) {
        return new ItemCrateContentImpl(itemStack, Float.max(0, Float.min(1, chance)), previewItemFactory,
                rarity(chance));
    }

    public PermissionCrateContent expiringPermissionContent(String permission, String permissionDescription,
                                                            @Range(from = 0, to = 1) float chance, Duration duration) {
        return new LuckPermsPermissionCrateContentImpl(LuckPermsProvider.get(), permission, permissionDescription,
                previewItemFactory, duration, chance, rarity(chance));
    }

    public PermissionCrateContent permissionContent(String permission, String permissionDescription,
                                                    @Range(from = 0, to = 1) float chance) {
        return new LuckPermsPermissionCrateContentImpl(LuckPermsProvider.get(), permission, permissionDescription,
                previewItemFactory, null, chance, rarity(chance));
    }

    public ContentRarity rarity(@Range(from = 0, to = 1) float chance) {
        return ContentRarity.NORMAL;
    }


}
