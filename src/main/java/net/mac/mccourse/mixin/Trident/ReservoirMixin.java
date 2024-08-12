package net.mac.mccourse.mixin.Trident;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.effect.ModEffects;
import net.mac.mccourse.enchantment.ModEnchantments;
import net.mac.mccourse.item.ModItems;
import net.mac.mccourse.util.InventoryUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class ReservoirMixin {
    public static final String Reservoir_Charges_Key = "charges";

    @Mixin(LivingEntity.class)
    public static class ReservoirApplyChargesMixin {

        @Unique
        private boolean hadSoakedEffect = false;

        @Inject(method = "tick", at = @At("HEAD"))
        private void onTick(CallbackInfo info) {
            LivingEntity entity = (LivingEntity) (Object) this;
            boolean hasSoakedEffect = entity.hasStatusEffect(ModEffects.SOAKED);
            if (!hasSoakedEffect && hadSoakedEffect) {
                    if (entity instanceof  PlayerEntity player) {
                        InventoryUtil.applyChargesToTridents(player, Items.TRIDENT);
                }
            }
            hadSoakedEffect = hasSoakedEffect;
        }
    }
}
