package net.mac.mccourse.mixin.Trident;

import net.mac.mccourse.effect.ModEffects;
import net.mac.mccourse.enchantment.ModEnchantments;
import net.mac.mccourse.mixin.Trident.Accessor.TridentEntityAccessor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

public class OceansWrathMixin {
    @Mixin(TridentEntity.class)
    private static class IncreaseDamageWhenWet {
        @Inject(method = "onEntityHit", at = @At("HEAD"))
        private void IncreaseDamage(EntityHitResult entityHitResult, CallbackInfo ci) {
            TridentEntity trident = (TridentEntity) (Object) this;
            if (!trident.getWorld().isClient){
                ItemStack tridentStack = ((TridentEntityAccessor) trident).getTridentStack();
                int oceansWrathLevel = EnchantmentHelper.getLevel(ModEnchantments.SPLASHING, tridentStack);
                if (oceansWrathLevel > 0){
                    Entity owner = trident.getOwner();
                    Entity target = entityHitResult.getEntity();
                    if (target instanceof LivingEntity livingTarget){
                        if (owner instanceof PlayerEntity playerOwner && playerOwner.hasStatusEffect(ModEffects.SOAKED)){
                            DamageSource damageSource = trident.getDamageSources().trident(trident, trident.getOwner() == null ? trident : trident.getOwner());
                            if (oceansWrathLevel == 1) {
                                livingTarget.damage(damageSource, 2F);
                            } else {
                                livingTarget.damage(damageSource, 4F);
                            }
                        }
                    }
                }
            }
        }
    }
}
