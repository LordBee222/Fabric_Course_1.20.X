package net.mac.mccourse.util;

import net.mac.mccourse.MCCourseMod;
import net.minecraft.item.ItemStack;

public class Ticker {
    private final ItemStack stack;
    private static final String TICKER_KEY = "ticker";

    public Ticker(ItemStack stack){
        this.stack = stack;
    }

    public void tick(){
        if (stack.hasNbt() && stack.getNbt().contains(TICKER_KEY)){
            int ticks = stack.getNbt().getInt(TICKER_KEY);
            if (ticks > 0){
                ticks--;
                stack.getOrCreateNbt().putInt(TICKER_KEY, ticks);
                MCCourseMod.LOGGER.info("Ran Ticker");
            }
        }
    }
}
