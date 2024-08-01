package net.mac.mccourse.potion;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.effect.ModEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModPotions {
    public static final Potion TOTEM_SICKNESS_POTION = registerPotion("totem_sickness_potion",
            new Potion("totem_sickness", new StatusEffectInstance(ModEffects.TOTEM_SICKNESS, 200)));


    private static Potion registerPotion(String name, Potion potion){
        return Registry.register(Registries.POTION, new Identifier(MCCourseMod.MOD_ID, name), potion);
    }

    public static void registerPotions(){
        MCCourseMod.LOGGER.info("Registering Potions for " + MCCourseMod.MOD_ID);
    }
}
