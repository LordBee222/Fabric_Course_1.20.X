package net.mac.mccourse.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public interface ILodgedTrident {
    // Does the player have a lodged trident
    boolean hasLodgedTrident();

    // Get the thrower of the lodged trident
    LivingEntity getLodgedTridentOwner();

    // get the Itemstack nbt as nbt
    NbtCompound getLodgedTridentData();

    // remove a trident
    void removeLodgedTrident();

    // add a trident
    void addLodgedTrident(LivingEntity tridentOwner, ItemStack tridentStack);

}
