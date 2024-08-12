package net.mac.mccourse.mixin.Trident;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.effect.ModEffects;
import net.mac.mccourse.mixin.Trident.Accessor.TridentEntityAccessor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

public class ImpalingMixin {

    @Mixin(TridentEntity.class)
    private static class MakeImpalingGreatAgain {
        @Inject(method = "onEntityHit", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getAttackDamage(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EntityGroup;)F"), locals = LocalCapture.CAPTURE_FAILHARD)
        private void getGreatImpalingDamage(EntityHitResult entityHitResult, CallbackInfo ci, Entity entity, float f, LivingEntity livingEntity) {
            TridentEntity trident = (TridentEntity) (Object) this;
            if (!trident.getWorld().isClient){
                ItemStack tridentStack = ((TridentEntityAccessor) trident).getTridentStack();
                float additionalDamage = 0.0F;
                if (livingEntity.hasStatusEffect(ModEffects.SOAKED)) {
                    int impalingLevel = EnchantmentHelper.getLevel(Enchantments.IMPALING, tridentStack);
                    if (impalingLevel > 0) {
                        additionalDamage = 1.5F * impalingLevel;
                    }
                }
                DamageSource damageSource = trident.getDamageSources().trident(trident, trident.getOwner() == null ? trident : trident.getOwner());
                entity.damage(damageSource, additionalDamage);
            }
        }
    }
}
