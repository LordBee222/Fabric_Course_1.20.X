package net.mac.mccourse.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.mac.mccourse.effect.ModEffects;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ChargedPlayerRenderFeature extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    private static final Identifier SKIN = new Identifier("textures/entity/creeper/creeper_armor.png");
    private final PlayerEntityModel<AbstractClientPlayerEntity> chargedModel;

    public ChargedPlayerRenderFeature(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context, EntityModelLoader loader) {
        super(context);
        this.chargedModel = new PlayerEntityModel<>(loader.getModelPart(ModEntityModelLayers.PLAYER_CHARGED), false);

    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (this.shouldRenderOverlay(entity)) {
            float partialAge = (float)entity.age + tickDelta;
            this.chargedModel.animateModel(entity, limbAngle, limbDistance, tickDelta);
            this.chargedModel.copyStateTo(this.chargedModel);
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEnergySwirl(SKIN, this.getEnergySwirlX(partialAge) % 1.0F, partialAge * 0.01F % 1.0F));
            this.chargedModel.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
            this.chargedModel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 0.5F, 0.5F, 0.5F, 1.0F);
        }
    }

    private boolean shouldRenderOverlay(AbstractClientPlayerEntity player) {
        return player.hasStatusEffect(ModEffects.CHARGED);
    }

    protected float getEnergySwirlX(float partialAge) {
        return partialAge * 0.01f;
    }
}
