package net.mac.mccourse.mixin;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.render.StuckShardFeatureRenderer;
import net.mac.mccourse.util.IEntityShardGetter;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class StuckAmethystMixin{

@Mixin(LivingEntity.class)
public static abstract class ShardBehavorMixin implements IEntityShardGetter {

    @Unique private static final TrackedData<Integer> SHARDS_COUNT = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    @Unique private static final TrackedData<Integer> SHARDS_TIMER = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);


    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void initData(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        entity.getDataTracker().startTracking(SHARDS_COUNT, 0);
        entity.getDataTracker().startTracking(SHARDS_TIMER, 0);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tickDownShards(CallbackInfo ci){
        LivingEntity entity = (LivingEntity) (Object) this;
        if (((IEntityShardGetter)entity).getStuckShards() > 0) {
            if (((IEntityShardGetter) entity).getShardTimer() <= 0) {
               ((IEntityShardGetter) entity).setShardTimer(5000);
                MCCourseMod.LOGGER.info("SET COUNTDOWN TO " + ((IEntityShardGetter) entity).getShardTimer());
            }
            /*
            if (i >= 8){
                ((IEntityShardGetter) entity).setStuckShards(entity, 0);
                this.explodeCluster(entity, entity.getWorld());
            }

             */
            ((IEntityShardGetter) entity).setShardTimer((((IEntityShardGetter) entity).getShardTimer() - 1));
            MCCourseMod.LOGGER.info("TICKED DOWN TIMER " + ((IEntityShardGetter) entity).getShardTimer());
            if (((IEntityShardGetter) entity).getShardTimer() <= 0) {
                MCCourseMod.LOGGER.info("about to remove Shard " + ((IEntityShardGetter) entity).getStuckShards());
                ((IEntityShardGetter) entity).setStuckShards(entity, (((IEntityShardGetter)entity).getStuckShards() - 1));
                 MCCourseMod.LOGGER.info("Removed Shard " + ((IEntityShardGetter) entity).getStuckShards());

            }
        }
    }


    public void explodeCluster(LivingEntity host, World world){
        if (!world.isClient){
            host.getWorld().createExplosion(host, host.getX(), host.getY(), host.getZ(), 4, World.ExplosionSourceType.MOB);
        }
    }

    @Override
    public int getStuckShards()  {
        LivingEntity entity = (LivingEntity) (Object) this;
        return entity.getDataTracker().get(SHARDS_COUNT);
        //return ((IEntityShardGetter)entity).getStuckShards();
    }

    @Override
    public void setStuckShards(LivingEntity entity, int value) {
        entity.getDataTracker().set(SHARDS_COUNT, value);
    }

    @Override
    public int getShardTimer() {
        LivingEntity entity = (LivingEntity) (Object) this;
        return entity.getDataTracker().get(SHARDS_TIMER);
    }

    @Override
    public void setShardTimer(int value) {
        LivingEntity entity = (LivingEntity) (Object) this;
        entity.getDataTracker().set(SHARDS_TIMER, value);

    }
}

@Mixin(PlayerEntityRenderer.class)
public abstract static class ShardPlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    private ShardPlayerRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }
    @Inject(method = "<init>(Lnet/minecraft/client/render/entity/EntityRendererFactory$Context;Z)V", at = @At("TAIL"))
    private void addRenderFeature(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
        addFeature(new StuckShardFeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>(ctx, this));
    }
}
}
