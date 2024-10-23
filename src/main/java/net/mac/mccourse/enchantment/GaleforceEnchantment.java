package net.mac.mccourse.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;

import static net.minecraft.enchantment.EnchantmentHelper.getEquipmentLevel;

public class GaleforceEnchantment extends Enchantment {
    protected GaleforceEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot[] slotTypes) {
        super(weight, target, slotTypes);
    }

    @Override
    public int getMaxLevel() {
        return 3;
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

    public static int getGaleforce(LivingEntity entity) {
        return getEquipmentLevel(ModEnchantments.GALEFORCE, entity);
    }

    public static int getMaxDashes(LivingEntity entity) {
        return getGaleforce(entity) * 2;
    }

}
