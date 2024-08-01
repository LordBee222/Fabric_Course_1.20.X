package net.mac.mccourse.mixin;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.enchantment.ModEnchantments;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class ScopeEnchantmentMixin {
    @Mixin(GameRenderer.class)
    public abstract static class GameRendererMixin {
        @Shadow @Final
        private MinecraftClient client;
        private double multiplier = 1.0F;
        private double lastMultiplier = 1.0F;
        @Inject(method = "tick", at = @At("HEAD"))
        private void tick (CallbackInfo ci){
            double zoomMultiplier = MCCourseMod.zoom ? MCCourseMod.zoomMultiplayer : 1.0F;
            if (!client.options.getPerspective().isFirstPerson()) zoomMultiplier = 1.0F;
            lastMultiplier = multiplier;
            multiplier += (zoomMultiplier - multiplier) * 0.66F;
        }
        @Inject(method = "getFov(Lnet/minecraft/client/render/Camera;FZ)D", at = @At("RETURN"), cancellable = true)
        private void getFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> callbackInfo){
            double fov = callbackInfo.getReturnValue();
            double fovMultiplier = MathHelper.lerp(tickDelta, lastMultiplier, multiplier);
            double zoomedFov = fov * fovMultiplier;
            if (Math.abs(fov-zoomedFov) > 0.5)
                callbackInfo.setReturnValue(zoomedFov);
        }
    }

    @Mixin(InGameHud.class)
    public abstract static class InGameHudMixin {
        @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingSpyglass()Z"))
        private boolean spyglassOverlay(ClientPlayerEntity clientPlayerEntity) {
            ItemStack stack = clientPlayerEntity.getMainHandStack();
            return (MCCourseMod.zoom && EnchantmentHelper.getLevel(ModEnchantments.SCOPE, stack) == 3) || clientPlayerEntity.isUsingSpyglass();
        }
    }


    @Mixin(Mouse.class)
    public abstract static class MouseMixin {
        @Redirect(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingSpyglass()Z"))
        private boolean slowCamera(ClientPlayerEntity clientPlayerEntity){
            return clientPlayerEntity.isUsingSpyglass() || MCCourseMod.zoom;
        }
    }


    @Mixin(ClientPlayerEntity.class)
    public abstract static class ClientPlayerEntityMixin {
        @Shadow @Final
        protected MinecraftClient client;
        @Inject(method = "isSneaking", at = @At("HEAD"))
        private void checkSneakZoom(CallbackInfoReturnable<Boolean> cir) {
            PlayerEntity player = (PlayerEntity) (Object) this;
            if (!player.getEntityWorld().isClient()) return;
            ItemStack stack = player.getMainHandStack();
            if (stack.isOf(Items.CROSSBOW)) {
                boolean hasScope = stack.getEnchantments().asString().contains(EnchantmentHelper.getEnchantmentId(ModEnchantments.SCOPE).toString());
                int scopeLevel = EnchantmentHelper.getLevel(ModEnchantments.SCOPE, stack);
                if (hasScope) {
                    boolean sneaking = player.isInSneakingPose();
                    if (sneaking && sneaking != MCCourseMod.zoom) {
                        player.playSound(SoundEvents.ITEM_SPYGLASS_USE, 1.0F, 1.0F);
                        MCCourseMod.zoomMultiplayer = getZoomFromScopeLevel(scopeLevel);
                        MCCourseMod.zoom = sneaking;
                    } else if (!sneaking && sneaking != MCCourseMod.zoom) {
                        player.playSound(SoundEvents.ITEM_SPYGLASS_STOP_USING, 1.0F, 1.0F);
                        MCCourseMod.zoomMultiplayer = 1.0F;
                        MCCourseMod.zoom = sneaking;
                    }
                } else {
                    MCCourseMod.zoomMultiplayer = 1.0F;
                    MCCourseMod.zoom = false;
                }
            }
        }
        @Inject(method = "tick", at = @At("HEAD"))
        private void checkHoldingScopedCrossbow(CallbackInfo ci) {
            PlayerEntity player = (PlayerEntity) (Object) this;
            ItemStack stack = player.getMainHandStack();
            if (!stack.isOf(Items.CROSSBOW)) MCCourseMod.zoom = false;
        }
        private static double getZoomFromScopeLevel(int scopeLevel) {
            switch (scopeLevel) {
                case 1:
                    return 0.5F;
                case 2:
                    return 0.3F;
                case 3:
                    return 0.1F;
                default:
                    return 1.0F;
            }
        }
    }



}
