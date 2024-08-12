package net.mac.mccourse.util;

import net.mac.mccourse.MCCourseMod;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

public class CrossbowUtil {
    public static final String CHARGED_PROJECTILES_KEY = "ChargedProjectiles";
    public static final String SHOTS_KEY = "Shots";

    public static void clearProjectiles(ItemStack crossbow) {
        NbtCompound nbtCompound = crossbow.getNbt();
        if (nbtCompound != null) {
            NbtList nbtList = nbtCompound.getList(CHARGED_PROJECTILES_KEY, NbtElement.LIST_TYPE);
            nbtList.clear();
            nbtCompound.put(CHARGED_PROJECTILES_KEY, nbtList);
        }
    }

    public static float getSpeed(ItemStack stack) {
        if (CrossbowItem.hasProjectile(stack, Items.FIREWORK_ROCKET)) {
            return 1.6f;
        }
        return 3.15f;
    }
}
