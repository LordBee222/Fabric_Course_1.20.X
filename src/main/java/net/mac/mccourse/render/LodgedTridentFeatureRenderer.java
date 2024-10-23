package net.mac.mccourse.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.util.ILodgedTrident;
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

@Environment(value= EnvType.CLIENT)
public class LodgedTridentFeatureRenderer<T extends LivingEntity, M extends PlayerEntityModel<T>>
extends StuckObjectsFeatureRenderer<T, M> {
    private final EntityRenderDispatcher dispatcher;

    public LodgedTridentFeatureRenderer(EntityRendererFactory.Context context, LivingEntityRenderer<T, M> entityRenderer) {
        super(entityRenderer);
        this.dispatcher = context.getRenderDispatcher();
    }

    @Override
    protected int getObjectCount(T entity) {
        return ((ILodgedTrident)entity).hasLodgedTrident() ? 1 : 0;
    }

    @Override
    protected void renderObject(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Entity entity, float directionX, float directionY, float directionZ, float tickDelta) {
        float f = MathHelper.sqrt(directionX * directionX + directionZ * directionZ);
            TridentEntity trident = new TridentEntity(entity.getWorld(), (LivingEntity) entity, new ItemStack(Items.TRIDENT));
            trident.setYaw((float)(Math.atan2(directionX, directionZ) * 57.2957763671875));
            trident.setPitch((float)(Math.atan2(directionY, f) * 57.2957763671875));
            trident.prevYaw = trident.getYaw();
            trident.prevPitch = trident.getPitch();

            this.dispatcher.render(trident, 0.0, 0.0, 0.0, 0.0f, tickDelta, matrices, vertexConsumers, light);
    }
}
