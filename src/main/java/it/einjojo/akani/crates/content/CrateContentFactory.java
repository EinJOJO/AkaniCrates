package it.einjojo.akani.crates.content;

import com.google.common.base.Preconditions;
import it.einjojo.akani.crates.content.impl.ItemCrateContentImpl;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public class CrateContentFactory {


    public EconomyCrateContent thalerContent(@Range(from = 0, to=Integer.MAX_VALUE) int amount, @Range(from=0, to=1) float chance) {
        return null;
    }

    public ItemCrateContent itemContent(@NotNull ItemStack itemStack, @Range(from=0, to=1) float chance) {

        return new ItemCrateContentImpl(itemStack, Float.max(0, Float.min(1, chance)));
    }

}
