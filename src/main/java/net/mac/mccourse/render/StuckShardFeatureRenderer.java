package net.mac.mccourse.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mac.mccourse.entity.custom.AmethystShardEntity;
import net.mac.mccourse.util.IEntityShardGetter;
import net.mac.mccourse.util.LodgedTridentUtil;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.StuckObjectsFeatureRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;

@Environment(value = EnvType.CLIENT)
public class StuckShardFeatureRenderer<T extends LivingEntity, M extends PlayerEntityModel<T>>
        extends StuckObjectsFeatureRenderer<T, M> {
    private final EntityRenderDispatcher dispatcher;

    public StuckShardFeatureRenderer(EntityRendererFactory.Context context, LivingEntityRenderer<T, M> entityRenderer) {
        super(entityRenderer);
        this.dispatcher = context.getRenderDispatcher();
    }

    @Override
    protected int getObjectCount(T entity) {
        return ((IEntityShardGetter) entity).getStuckShards();
    }

    @Override
    protected void renderObject(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Entity entity, float directionX, float directionY, float directionZ, float tickDelta) {
        float f = MathHelper.sqrt(directionX * directionX + directionZ * directionZ);
        AmethystShardEntity shard = new AmethystShardEntity(entity.getWorld(), entity.getX(), entity.getY(), entity.getZ());
        shard.setYaw((float) (Math.atan2(directionX, directionZ) * 57.2957763671875));
        shard.setPitch((float) (Math.atan2(directionY, f) * 57.2957763671875));
        shard.prevYaw = shard.getYaw();
        shard.prevPitch = shard.getPitch();
        this.dispatcher.render(shard, 0.0, 0.0, 0.0, 0.0f, tickDelta, matrices, vertexConsumers, light);
    }
}
