package net.mac.mccourse.util;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.enchantment.ModEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;

import static net.mac.mccourse.mixin.Trident.ReservoirMixin.Reservoir_Charges_Key;

public class InventoryUtil {
    public static boolean hasPlayerStackInInventory(PlayerEntity player, Item item){
        for (int i = 0; i < player.getInventory().size(); i++){
            ItemStack currentStack = player.getInventory().getStack(i);
            if (!currentStack.isEmpty() && currentStack.isOf(item)){
                return true;
            }
        }
        return false;
    }

    public static int getFirstInventoryIndex (PlayerEntity player, Item item){
        for (int i = 0; i < player.getInventory().size(); i++){
            ItemStack currentStack = player.getInventory().getStack(i);
            if (!currentStack.isEmpty() && currentStack.isOf(item)){
                return i;
            }
        }
        return -1;
    }

    public static boolean hasEnchantment(LivingEntity entity, Enchantment enchantment) {
        return EnchantmentHelper.getLevel(enchantment, entity.getMainHandStack()) >= 1 || EnchantmentHelper.getLevel(enchantment, entity.getOffHandStack()) >= 1;
    }

    public static void applyChargesToTridents(PlayerEntity player, Item item){
        for (int i = 0; i < player.getInventory().size(); i++){
            ItemStack currentStack = player.getInventory().getStack(i);
            if (!currentStack.isEmpty() && currentStack.isOf(item)){
                int reservoirLevel = EnchantmentHelper.getLevel(ModEnchantments.RESERVOIR, currentStack);
                if (reservoirLevel > 0) {
                    NbtCompound nbtCompound = currentStack.getOrCreateNbt();
                    MCCourseMod.LOGGER.info("Has Reservoir");
                    nbtCompound.putInt(Reservoir_Charges_Key, reservoirLevel);
                    currentStack.setNbt(nbtCompound);
                    MCCourseMod.LOGGER.info("Applied NBT");
                    }
            }
        }
    }
}
