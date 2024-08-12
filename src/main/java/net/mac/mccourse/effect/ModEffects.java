package net.mac.mccourse.effect;

import net.mac.mccourse.MCCourseMod;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEffects {
    public static final StatusEffect TOTEM_SICKNESS = registerStatusEffect("totem_sickness",
            new TotemSicknessEffect(StatusEffectCategory.HARMFUL, 0x36ebab));

    public static final StatusEffect SOAKED = registerStatusEffect("soaked",
            new SoakedEffect(StatusEffectCategory.NEUTRAL, 3694022));

    public static final StatusEffect CHARGED = registerStatusEffect("charged",
            new ChargedEffect(StatusEffectCategory.NEUTRAL, 3694022));


    private static StatusEffect registerStatusEffect(String name, StatusEffect statusEffect){
        return Registry.register(Registries.STATUS_EFFECT, new Identifier(MCCourseMod.MOD_ID, name), statusEffect);
    }

    public static void registerEffects(){
        MCCourseMod.LOGGER.info("Registering Mod Effects For " + MCCourseMod.MOD_ID);
    }
}
