package net.mac.mccourse.mixin;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.effect.ModEffects;
import net.mac.mccourse.render.LodgedTridentFeatureRenderer;
import net.mac.mccourse.util.IEntityLodgedTridentSaver;
import net.mac.mccourse.util.ILodgedTrident;
import net.mac.mccourse.util.LodgedTridentUtil;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class TridentMixin {
    @Mixin(LivingEntity.class)
    public static abstract class TridentBehaviorMixin implements ILodgedTrident {

        @Unique private static final TrackedData<Integer> LODGED_TRIDENT_OWNER_ID = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);
        @Unique private static final TrackedData<Boolean> HAS_LODGED_TRIDENT = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

        @Inject(method = "initDataTracker", at = @At("TAIL"))
        private void initData(CallbackInfo ci) {
            LivingEntity entity = (LivingEntity) (Object) this;
            entity.getDataTracker().startTracking(HAS_LODGED_TRIDENT, false);
            entity.getDataTracker().startTracking(LODGED_TRIDENT_OWNER_ID, -1);

        }

        @Override @Unique
        public boolean hasLodgedTrident() {
            LivingEntity entity = (LivingEntity) (Object) this;
            return entity.getDataTracker().get(HAS_LODGED_TRIDENT);
        }

        @Override @Unique @Nullable
        public LivingEntity getLodgedTridentOwner() {
            LivingEntity entity = (LivingEntity) (Object) this;
            if (((ILodgedTrident)entity).hasLodgedTrident()){
                World world = entity.getWorld();
                int ownerId = entity.getDataTracker().get(LODGED_TRIDENT_OWNER_ID);
                MCCourseMod.LOGGER.info("GOT ID: " +  ownerId);
                LivingEntity owner = (LivingEntity) world.getEntityById(ownerId);
                MCCourseMod.LOGGER.info("GOT Owner: " +  owner.getType().toString());
                return owner;
            }
            return null;
        }

        @Override @Unique
        public void removeLodgedTrident() {
            LivingEntity entity = (LivingEntity) (Object) this;
            entity.getDataTracker().set(HAS_LODGED_TRIDENT, false);
            entity.getDataTracker().set(LODGED_TRIDENT_OWNER_ID, -1);
            ((ILodgedTrident)entity).getLodgedTridentData().remove(LodgedTridentUtil.LODGED_TRIDENTS_KEY);
        }

        @Override @Unique
        public void addLodgedTrident(LivingEntity tridentOwner, ItemStack tridentStack) {
            LivingEntity entity = (LivingEntity) (Object) this;
            entity.getDataTracker().set(HAS_LODGED_TRIDENT, true);
            entity.getDataTracker().set(LODGED_TRIDENT_OWNER_ID, tridentOwner.getId());
            ((ILodgedTrident)entity).getLodgedTridentData().put(LodgedTridentUtil.LODGED_TRIDENTS_KEY, tridentStack.writeNbt(new NbtCompound()));
            MCCourseMod.LOGGER.info("ID: " +  entity.getDataTracker().get(LODGED_TRIDENT_OWNER_ID));
        }
    }
    /********************************************************************************************************
     /// LodgeTrident
     /// Affected Methods: onEntityHit
     // Increments Hit Entity's Lodged Trident Count
     /*********************************************************************************************************/
    @Mixin(Entity.class)
    public static abstract class TridentEntityNBTMixin implements ILodgedTrident {
        private NbtCompound lodgedTridentItemData;

        @Override @Unique
        public NbtCompound getLodgedTridentData() {
            if (this.lodgedTridentItemData == null) {
                this.lodgedTridentItemData = new NbtCompound();
            }
            return lodgedTridentItemData;
        }

        @Inject(method = "writeNbt", at = @At("HEAD"))
        protected void injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
            if (this.lodgedTridentItemData != null) {
                nbt.put(LodgedTridentUtil.LODGED_TRIDENTS_KEY, lodgedTridentItemData);
            }
        }

        @Inject(method = "readNbt", at = @At("HEAD"))
        protected void injectReadMethod(NbtCompound nbt, CallbackInfo info) {
            if (nbt.contains(LodgedTridentUtil.LODGED_TRIDENTS_KEY)) {
                this.lodgedTridentItemData = nbt.getCompound(LodgedTridentUtil.LODGED_TRIDENTS_KEY);
            }
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

            if (!((ILodgedTrident)user).hasLodgedTrident()) {
                ((ILodgedTrident)user).addLodgedTrident((LivingEntity) attacker, ((TridentEntityAccessor)trident).getTridentStack());
                user.addStatusEffect(new StatusEffectInstance(ModEffects.SHATTERED, 100, 0));
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

}
