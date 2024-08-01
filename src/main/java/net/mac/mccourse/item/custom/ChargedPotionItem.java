package net.mac.mccourse.item.custom;

import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;

public class ChargedPotionItem extends PotionItem {
    public ChargedPotionItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 15;
    }

}
