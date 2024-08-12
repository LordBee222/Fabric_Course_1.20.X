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

    public static final Potion SPLASHED$WATER = registerPotion("splashed-water",
            new Potion("water", new StatusEffectInstance(ModEffects.SOAKED, 100, 0, true, false)));

    public static final Potion SPLASHED$AWKWARD = registerPotion("splashed-awkward",
            new Potion("water", new StatusEffectInstance(ModEffects.SOAKED, 200, 0, true, false)));

    public static final Potion SPLASHED$THICK = registerPotion("splashed-thick",
            new Potion("water", new StatusEffectInstance(ModEffects.SOAKED, 150, 0, true, false)));

    public static final Potion SPLASHED$MUNDANE = registerPotion("splashed-mundane",
            new Potion("water", new StatusEffectInstance(ModEffects.SOAKED, 125, 0, true, false)));

    private static Potion registerPotion(String name, Potion potion){
        return Registry.register(Registries.POTION, new Identifier(MCCourseMod.MOD_ID, name), potion);
    }

    public static void registerPotions(){
        MCCourseMod.LOGGER.info("Registering Potions for " + MCCourseMod.MOD_ID);
    }
}
