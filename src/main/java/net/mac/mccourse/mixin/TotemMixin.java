package net.mac.mccourse.mixin;

import net.mac.mccourse.effect.ModEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class TotemMixin {
    @Inject(method = "tryUseTotem", at = @At("HEAD"), cancellable = true)
    private void tryUseTotem(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity.hasStatusEffect(ModEffects.TOTEM_SICKNESS)) {
            cir.setReturnValue(false);
        }
    }


    @Redirect(method = "tryUseTotem", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;clearStatusEffects()Z"))
    private boolean redirectClearStatusEffects(LivingEntity entity, DamageSource source) {
        // Clear status effects and then apply the Totem Sickness effect
        entity.clearStatusEffects();
        entity.addStatusEffect(new StatusEffectInstance(ModEffects.TOTEM_SICKNESS, 6000)); // 6000 ticks = 5 minutes
        if (source.isOf(DamageTypes.OUT_OF_WORLD)){
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 6000)); // 6000 ticks = 5 minutes
        } else if (source.isOf(DamageTypes.DROWN)){
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 6000)); // 6000 ticks = 5 minutes
        }
        return true; // Return true to indicate the method should continue as usual
    }

    @Redirect(method = "tryUseTotem", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageSource;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
    private boolean redirectIsIn(DamageSource source, TagKey<DamageType> tag) {
        // Check if the source is 'out_of_world'
        if (source.isOf(DamageTypes.GENERIC_KILL)) {
            return false;
        }

        // Continue with the normal behavior
        return source.isIn(tag);
    }
}
