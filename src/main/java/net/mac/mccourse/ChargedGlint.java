package net.mac.mccourse;

import net.mac.mccourse.enchantment.ModEnchantments;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ChargedGlint {
    private static final Identifier CHARGED_GLINT_TEXTURE  = new Identifier(MCCourseMod.MOD_ID, "textures/misc/charged_glint.png");

    public static void renderChargedGlint(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ItemStack stack) {
        if (EnchantmentHelper.getLevel(ModEnchantments.LIGHTNING_STRIKER, stack) > 0) {
            MinecraftClient client = MinecraftClient.getInstance();
            BakedModel model = client.getItemRenderer().getModel(stack, client.world, null, 0);
            matrices.push();
            client.getTextureManager().bindTexture(CHARGED_GLINT_TEXTURE);
            // Render the glint
            VertexConsumerProvider.Immediate immediate = client.getBufferBuilders().getEntityVertexConsumers();
            client.getItemRenderer().renderItem(stack, ModelTransformationMode.NONE, false, matrices, immediate, light, OverlayTexture.DEFAULT_UV, model);
            immediate.draw();
            matrices.pop();        }
    }
}
