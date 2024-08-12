package net.mac.mccourse.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class ChargedEffect extends StatusEffect {
    protected ChargedEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int i = 100 >> amplifier;
        if (i > 0){
            return duration % i == 0;
        } else {
            return false;
        }
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        entity.damage(entity.getDamageSources().lightningBolt(), 1.0F);
    }
}
