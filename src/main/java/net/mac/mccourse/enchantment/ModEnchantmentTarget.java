package net.mac.mccourse.enchantment;

import net.mac.mccourse.item.ModItems;
import net.mac.mccourse.item.custom.CeremonialBladeItem;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.Item;

public enum ModEnchantmentTarget {
    CEREMONIAL_BLADE{
        @Override
        public boolean isAcceptableItem(Item item){
            return item instanceof CeremonialBladeItem;
        }
    };



    public abstract boolean isAcceptableItem(Item var1);

}
