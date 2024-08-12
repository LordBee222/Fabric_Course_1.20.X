package net.mac.mccourse.render;

import net.mac.mccourse.effect.ModEffects;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class LightningOverlayPlayerRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    private static final Identifier LIGHTNING_OVERLAY = new Identifier("textures/entity/creeper/creeper_armor.png");

    public LightningOverlayPlayerRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player,
                       float limbAngle, float limbDistance, float tickDelta, float customAngle, float headYaw, float headPitch) {
        if (shouldRenderLightning(player)) {
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(LIGHTNING_OVERLAY));

            float red = 1.0F;
            float green = 1.0F;
            float blue = 1.0F;
            float alpha = 1.0F;

            this.getContextModel().head.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, red, green, blue, alpha);
            this.getContextModel().body.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, red, green, blue, alpha);
        }
    }

    private boolean shouldRenderLightning(AbstractClientPlayerEntity player) {
        return player.hasStatusEffect(ModEffects.SOAKED);
    }
}