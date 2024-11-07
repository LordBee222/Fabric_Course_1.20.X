package net.mac.mccourse;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.mac.mccourse.block.ModBlocks;
import net.mac.mccourse.entity.ModEntities;
import net.mac.mccourse.entity.client.PorcupineModel;
import net.mac.mccourse.entity.client.PorcupineRenderer;
import net.mac.mccourse.entity.layer.ModModelLayers;
import net.mac.mccourse.entity.renderer.AmethystShardEntityRenderer;
import net.mac.mccourse.entity.renderer.CustomEndCrystalEntityRenderer;
import net.mac.mccourse.render.ChargedPlayerRenderFeature;
import net.mac.mccourse.util.ModModelPredicateProvider;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.FallingBlockEntityRenderer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;

public class MCCourseModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PINK_GARNET_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PINK_GARNET_TRAPDOOR, RenderLayer.getCutout());
        ModModelPredicateProvider.registerModModels();
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CAULIFLOWER_CROP, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PETUNIA, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.POTTED_PETUNIA, RenderLayer.getCutout());
        EntityRendererRegistry.register(ModEntities.THROWN_DICE_PROJECTILE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.DYNAMITE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.BOOM_SLIME, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.BOOM_HONEY, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.AMETHYST_SHARD, AmethystShardEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.CUSTOM_CRYSTAL, CustomEndCrystalEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.CUSTOM_BOMB_ENTITY, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.SHARD, FlyingItemEntityRenderer::new);

        EntityRendererRegistry.register(ModEntities.BOMB, FlyingItemEntityRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.PORCUPINE, PorcupineModel::getTexturedModelData);

        EntityRendererRegistry.register(ModEntities.PORCUPINE, PorcupineRenderer::new);
        EntityRendererRegistry.register(ModEntities.UNSTABLE_BLOCK, FallingBlockEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.HOMING_ROCKET, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.SOUL_SHULKER_BULLET, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.TAROT_CARD, FlyingItemEntityRenderer::new);

    }
}
