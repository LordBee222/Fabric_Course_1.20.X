package net.mac.mccourse.enchantment.trident;

import net.mac.mccourse.enchantment.ModEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class LightweightEnchantment  extends Enchantment {
    public LightweightEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot[] slotTypes) {
        super(weight, target, slotTypes);
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public boolean isAvailableForRandomSelection() {
        return true;
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return true;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    public static boolean hasLightweight(ItemStack stack){
        return EnchantmentHelper.getLevel(ModEnchantments.LIGHTWEIGHT, stack) >= 1;
    }

    public static int getLevel(ItemStack stack){
        return EnchantmentHelper.getLevel(ModEnchantments.LIGHTWEIGHT, stack);
    }

    public static double getLightweightChargeTicks(int level){
        return -1.5 * level + 10;
    }

    public static double getLightweightTridentSpeed(int level){
        return 0.5 * level + 2.5;
    }
}
