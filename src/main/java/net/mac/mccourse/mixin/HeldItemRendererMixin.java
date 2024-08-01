package net.mac.mccourse.mixin;

import net.mac.mccourse.item.ModItems;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Field;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {

    @Inject(method = "getHandRenderType", at = @At("RETURN"), cancellable = true)
    private static void isCharged(ClientPlayerEntity player, CallbackInfoReturnable<Object> cir) {
        ItemStack stack = player.getMainHandStack();
        if (stack.isOf(ModItems.SCOPED_CROSSBOW) && CrossbowItem.isCharged(stack)) {
            try {
                Field handRenderTypeField = HeldItemRenderer.class.getDeclaredField("HandRenderType");
                handRenderTypeField.setAccessible(true);
                Object handRenderType = handRenderTypeField.get(null);

                Field renderMainHandOnlyField = handRenderType.getClass().getDeclaredField("RENDER_MAIN_HAND_ONLY");
                renderMainHandOnlyField.setAccessible(true);
                Object renderMainHandOnly = renderMainHandOnlyField.get(handRenderType);

                cir.setReturnValue(renderMainHandOnly);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
