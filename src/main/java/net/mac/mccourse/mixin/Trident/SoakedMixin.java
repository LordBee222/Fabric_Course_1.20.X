package net.mac.mccourse.mixin.Trident;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.effect.ModEffects;
import net.mac.mccourse.potion.ModPotions;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public abstract class SoakedMixin {

    /********************************************************************************************************
     /// PotionItemDefaultStack
     /// Purpose: Set Soaked As PotionItem's Default Stack
     /// Affected Class: PotionItem
     /// Affected Methods: getDefaultStack
     /*********************************************************************************************************/
    @Mixin(PotionItem.class)
    public static abstract class PotionItemDefaultStack {
        @Inject(method = "getDefaultStack", at = @At("RETURN"), cancellable = true)
        private void modifyDefaultStack(CallbackInfoReturnable<ItemStack> cir) {
            ItemStack defaultStack = cir.getReturnValue();
            ItemStack modifiedStack = PotionUtil.setPotion(defaultStack, ModPotions.SPLASHED$WATER);
            MCCourseMod.LOGGER.info("CHANGED DEFAULT STACK");
            cir.setReturnValue(modifiedStack);
        }
    }

    /********************************************************************************************************
     /// SoakedDripParticlesMixin
     /// Purpose: Blocks Vanilla Effect Particles For Soaked
     /// Affected Class: LivingEntity
     /// Affected Methods: baseTick
     /*********************************************************************************************************/
    @Mixin(LivingEntity.class)
    public static abstract class SoakedDripParticlesMixin {
        @Inject(method = "baseTick", at = @At("HEAD"))
        public void soakedDripEffect(CallbackInfo ci) {
            LivingEntity entity = (LivingEntity) (Object) this;
            World world = entity.getEntityWorld();
            if (world.isClient()) {
                StatusEffectInstance effectInstance = entity.getStatusEffect(ModEffects.SOAKED);
                if (effectInstance != null) {
                    ((ClientWorld) world).addParticle(ParticleTypes.DRIPPING_WATER,
                            entity.getX() + (world.random.nextDouble() - 0.5D) * entity.getWidth(),
                            entity.getY() + world.random.nextDouble() * entity.getHeight() - 0.5F,
                            entity.getZ() + (world.random.nextDouble() - 0.5D) * entity.getWidth(),
                            0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    /********************************************************************************************************
     /// SoakedBlockVanillaEffectParticlesMixin
     /// Purpose: Blocks Vanilla Effect Particles For Soaked
     /// Affected Class: StatusEffectInstance
     /// Affected Methods: shouldShowParticles
     /*********************************************************************************************************/
    @Mixin(StatusEffectInstance.class)
    public static abstract class SoakedBlockVanillaEffectParticlesMixin {
        @Inject(method = "shouldShowParticles", at = @At("HEAD"), cancellable = true)
        public void blockVanillaParticles(CallbackInfoReturnable<Boolean> cir) {
            StatusEffectInstance instance = (StatusEffectInstance) (Object) this;
            if (instance.getEffectType() == ModEffects.SOAKED) {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }

    /********************************************************************************************************
     /// SoakedNoParticlesColorFixMixin
     /// Purpose: Fix No Color For Soaked Due To No Particles
     /// Affected Class: PotionUtil
     /// Affected Methods: getColor
     /*********************************************************************************************************/
    @Mixin(PotionUtil.class)
    public static abstract class SoakedNoParticlesColorFixMixin {
        @Redirect(method = "getColor(Ljava/util/Collection;)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffectInstance;shouldShowParticles()Z"))
        private static boolean getColor(StatusEffectInstance instance) {
            if (!instance.shouldShowParticles()) {
                return instance.getEffectType() == ModEffects.SOAKED;
            } else {
                return true;
            }
        }
    }

    /********************************************************************************************************
     /// ApplySoakedInWetSituation
     /// Purpose: Apply Soaked When Wet
     /// Affected Class: LivingEntity
     /// Affected Methods: baseTick
     /*********************************************************************************************************/
    @Mixin(LivingEntity.class)
    public static abstract class ApplySoakedInWetSituation {
        @Inject(method = "baseTick", at = @At(value = "HEAD"))
        private void applySoaked(CallbackInfo ci) {
            LivingEntity livingEntity = (LivingEntity) (Object) this;
            if (livingEntity.isWet()) {
                livingEntity.addStatusEffect(new StatusEffectInstance(ModEffects.SOAKED, 100, 0, false, false));
            }
        }
    }

    /********************************************************************************************************
     /// ApplySoakedInWetSituation
     /// Purpose: Apply Soaked When Wet
     /// Affected Class: LivingEntity
     /// Affected Methods: baseTick
     /*********************************************************************************************************/
    @Mixin(TridentEntity.class)
    public static abstract class TridentEntityMixin {
        private static final TrackedData<Boolean> IS_SOAKED = DataTracker.registerData(TridentEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
        @Inject(method = "initDataTracker", at = @At("TAIL"))
        private void initDataTracker(CallbackInfo ci) {
            TridentEntity trident = (TridentEntity) (Object) this;
            trident.getDataTracker().startTracking(IS_SOAKED, false);
        }
        @Inject(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)V", at = @At("TAIL"))
        private void onConstruct(World world, LivingEntity owner, ItemStack stack, CallbackInfo ci) {
            TridentEntity trident = (TridentEntity) (Object) this;
            if (owner != null) {
                trident.getDataTracker().set(IS_SOAKED, owner.hasStatusEffect(ModEffects.SOAKED));
            } else {
                trident.getDataTracker().set(IS_SOAKED, false);
            }
        }
        @Inject(method = "tick", at = @At("HEAD"))
        public void onTick(CallbackInfo ci) {
            TridentEntity self = (TridentEntity) (Object) this;
            World world = self.getWorld();
            boolean isSoaked = self.getDataTracker().get(IS_SOAKED);
            if (world.isClient()) {
                if (isSoaked) {
                    world.addParticle(ParticleTypes.DRIPPING_WATER,
                            self.getX() + (world.random.nextDouble() - 0.5D) * self.getWidth(),
                            self.getY() + world.random.nextDouble() * self.getHeight() - 0.5F,
                            self.getZ() + (world.random.nextDouble() - 0.5D) * self.getWidth(),
                            0.0D, 0.0D, 0.0D);
                }
            }
        }
    }
}

