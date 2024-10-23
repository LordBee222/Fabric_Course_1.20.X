package net.mac.mccourse.effect;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;

public class StonedEffect extends StatusEffect {
    public StonedEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }
}
