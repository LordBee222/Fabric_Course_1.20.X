package net.mac.mccourse.mixin.Trident;

import net.mac.mccourse.effect.ModEffects;
import net.mac.mccourse.enchantment.ModEnchantments;
import net.mac.mccourse.mixin.Trident.Accessor.TridentEntityAccessor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class SplashingMixin {
    @Mixin(TridentEntity.class)
    private static class ApplySoakedEffect {

        @Inject(method = "onEntityHit", at = @At("HEAD"))
        private void ApplyWetEffect(EntityHitResult entityHitResult, CallbackInfo ci) {
            TridentEntity trident = (TridentEntity) (Object) this;
            if (!trident.getWorld().isClient){
                ItemStack tridentStack = ((TridentEntityAccessor) trident).getTridentStack();
                int splashingLevel = EnchantmentHelper.getLevel(ModEnchantments.SPLASHING, tridentStack);
                if (splashingLevel > 0){
                    Entity owner = trident.getOwner();
                    Entity target = entityHitResult.getEntity();
                    if (target instanceof LivingEntity livingTarget){
                        if (owner instanceof PlayerEntity playerOwner && playerOwner.hasStatusEffect(ModEffects.SOAKED)){
                            if (splashingLevel == 1) {
                                livingTarget.addStatusEffect(new StatusEffectInstance(ModEffects.SOAKED, 200, 0));
                            } else {
                                livingTarget.addStatusEffect(new StatusEffectInstance(ModEffects.SOAKED, 200, 1));
                            }
                        }
                    }
                }
            }
        }
    }
}
