package net.mac.mccourse.mixin;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.effect.ModEffects;
import net.mac.mccourse.render.LodgedTridentFeatureRenderer;
import net.mac.mccourse.util.IEntityLodgedTridentOwnerSaver;
import net.mac.mccourse.util.IEntityLodgedTridentSaver;
import net.mac.mccourse.util.LodgedTridentUtil;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.TridentItem;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class LodgedTridentMixin {

    @Mixin(Entity.class)
    public static class LodgedTridentSaverMixin implements IEntityLodgedTridentSaver     {
        private NbtCompound lodgedTridents;
        //private NbtCompound lodgedTridentOwner;


        @Override
        public NbtCompound getLodgedTridents() {
            if (this.lodgedTridents == null) {
                this.lodgedTridents = new NbtCompound();
            }
            return lodgedTridents;
        }

        @Inject(method = "writeNbt", at = @At("HEAD"))
        protected void injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
            if (this.lodgedTridents != null) {
                nbt.put(LodgedTridentUtil.LODGED_TRIDENTS_KEY, lodgedTridents);
            }
        }

        @Inject(method = "readNbt", at = @At("HEAD"))
        protected void injectReadMethod(NbtCompound nbt, CallbackInfo info) {
            if (nbt.contains(LodgedTridentUtil.LODGED_TRIDENTS_KEY)) {
                this.lodgedTridents = nbt.getCompound(LodgedTridentUtil.LODGED_TRIDENTS_KEY);
            }
        }


    }

    /********************************************************************************************************
     /// LodgeTrident
     /// Affected Methods: onEntityHit
     // Increments Hit Entity's Lodged Trident Count
     /*********************************************************************************************************/
    @Mixin(PlayerEntityRenderer.class)
    public abstract static class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

        private PlayerRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
            super(ctx, model, shadowRadius);
        }

        @Inject(method = "<init>(Lnet/minecraft/client/render/entity/EntityRendererFactory$Context;Z)V", at = @At("TAIL"))
        private void addRenderFeature(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {

            addFeature(new LodgedTridentFeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>(ctx, this));
        }
    }

    /********************************************************************************************************
     /// LodgeTrident
     /// Affected Methods: onEntityHit
     // Increments Hit Entity's Lodged Trident Count
     /*********************************************************************************************************/
    @Mixin(TridentEntity.class)
    private abstract static class LodgeTridentToTargetMixin {

        @Redirect(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;onUserDamaged(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/Entity;)V"))
        private void lodgeTrident(LivingEntity user, Entity attacker) {
            TridentEntity trident = (TridentEntity) (Object) this;
            EnchantmentHelper.onUserDamaged(user, attacker);

            if (!LodgedTridentUtil.hasLodgedTrident(user)) {
                ItemStack tridentStack = LodgedTridentUtil.getTridentEntityStack(trident);
                LodgedTridentUtil.setLodgedTrident(user, attacker,tridentStack);
                user.addStatusEffect(new StatusEffectInstance(ModEffects.SHATTERED, 100, 0));
            }
        }
    }

    /********************************************************************************************************
     /// LodgeTrident
     /// Affected Methods: onEntityHit
     // Increments Hit Entity's Lodged Trident Count
     /*********************************************************************************************************/
    @Mixin(LivingEntity.class)
    private abstract static class InitDataTrackerMixin {
        private static final TrackedData<Boolean> HAS_LODGED_TRIDENT = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.BOOLEAN);


        @Inject(method = "initDataTracker", at = @At(value = "TAIL"))
        private void lodgeTrident(CallbackInfo ci) {
            LivingEntity entity = (LivingEntity) (Object) this;
            entity.getDataTracker().startTracking(HAS_LODGED_TRIDENT, false);
        }


        static {
           LodgedTridentUtil.getDataTrackerFromInit(HAS_LODGED_TRIDENT);
        }
    }

    /********************************************************************************************************
     /// LodgeTrident
     /// Affected Methods: onEntityHit
     // Increments Hit Entity's Lodged Trident Count
     /*********************************************************************************************************/
    @Mixin(ClientPlayerEntity.class)
    private abstract static class MovementOfTridents {

        @Redirect(method = "canStartSprinting", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"))
        private boolean allowTridentSprinting(ClientPlayerEntity instance){
            //boolean bl = instance.getMainHandStack().isOf(Items.TRIDENT);
            boolean bl2 = instance.getStackInHand(instance.getActiveHand()).isOf(Items.TRIDENT);
            if (!instance.isUsingItem()){
                return false;
            }
            if (bl2){
                return false;
            }
            return true;
        }

        @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"))
        private boolean allowIncreasedTridentAimingMovement(ClientPlayerEntity instance){
            boolean bl2 = instance.getStackInHand(instance.getActiveHand()).isOf(Items.TRIDENT);
            if (!instance.isUsingItem()){
                return false;
            }
            if (bl2){
                return false;
            }
            return true;
        }
    }
}


