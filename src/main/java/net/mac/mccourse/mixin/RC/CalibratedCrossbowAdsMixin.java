package net.mac.mccourse.mixin.RC;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.enchantment.ModEnchantments;
import net.mac.mccourse.item.KeyBinds.AdsKeybind;
import net.mac.mccourse.item.ModItems;
import net.mac.mccourse.item.custom.CalibratedCrossbowItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class CalibratedCrossbowAdsMixin {

    @Mixin(GameRenderer.class)
    public abstract static class GameRendererMixin {
        @Shadow @Final MinecraftClient client;
        private double multiplier = 1.0F;
        private double lastMultiplier = 1.0F;

        @Inject(method = "tick", at = @At("HEAD"))
        private void tick(CallbackInfo ci) {
            double zoomMultiplier = CalibratedCrossbowItem.isAds ? 0.7F : 1.0F;

            if (!client.options.getPerspective().isFirstPerson()) zoomMultiplier = 1.0F;

            lastMultiplier = multiplier;
            multiplier += (zoomMultiplier - multiplier) * 0.66F;
        }

        @Inject(method = "getFov(Lnet/minecraft/client/render/Camera;FZ)D", at = @At("RETURN"), cancellable = true)
        private void getFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> callbackInfo) {
            double fov = callbackInfo.getReturnValue(), fovMultiplier = MathHelper.lerp(tickDelta, lastMultiplier, multiplier), zoomedFov = fov * fovMultiplier;
            if (Math.abs(fov - zoomedFov) > 0.5) callbackInfo.setReturnValue(zoomedFov);
        }
    }


    /********************************************************************************************************
     /// InGameHudMixin
     /// Purpose: Apply Spyglass Overlay for Crossbow Scope Enchantment
     /// Affected Class: InGameHud
     /// Affected Methods: render
     /*********************************************************************************************************/
    @Mixin(InGameHud.class)
    public abstract static class InGameHudMixin {
        Identifier defaultCrosshairTexture = new Identifier(MCCourseMod.MOD_ID, "textures/gui/default_crosshair.png");
        Identifier adsCrosshairTexture = new Identifier(MCCourseMod.MOD_ID, "textures/gui/ads_crosshair.png");
        Identifier HitscanCrosshairTexture = new Identifier(MCCourseMod.MOD_ID, "textures/gui/hitscan_crosshair.png");

        /*
        @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingSpyglass()Z"))
        private boolean spyglassOverlay(ClientPlayerEntity clientPlayerEntity) {
            //ItemStack stack = clientPlayerEntity.getMainHandStack();
           // KeyBinding adsBind = AdsKeybind.AdsKeyBinding;
            return clientPlayerEntity.isUsingSpyglass() ||CalibratedCrossbowItem.isAds;
            //return clientPlayerEntity.isUsingSpyglass() || adsBind.isPressed();
        }
         */

        @Shadow @Final private MinecraftClient client;
        @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
        private void renderCustomCrosshair(DrawContext context, CallbackInfo ci) {
            MinecraftClient client = MinecraftClient.getInstance();
            GameOptions gameOptions = this.client.options;
            if (client.player != null && isHoldingCalibratedCrossbow(client.player) && gameOptions.getPerspective().isFirstPerson()) {
                ci.cancel();
                drawAdsCrosshair(context, client);
            }
        }

        private boolean isHoldingCalibratedCrossbow(PlayerEntity player) {
            return player.getMainHandStack().getItem() == ModItems.SNIPER ||
                    player.getOffHandStack().getItem() == ModItems.SNIPER;
        }

        private void drawAdsCrosshair(DrawContext context, MinecraftClient client) {
            Identifier crosshairToUse = CalibratedCrossbowItem.isAds ? adsCrosshairTexture : defaultCrosshairTexture;
            context.drawTexture(crosshairToUse, (client.getWindow().getScaledWidth() / 2) - 8, (client.getWindow().getScaledHeight() / 2) - 8, 0, 0, 15, 15, 15, 15);
        }
    }


    /********************************************************************************************************
     /// MouseMixin
     /// Purpose: Slow Camera Movement for When Scoping with Level >= 3 Crossbow Scope Enchantment
     /// Affected Class: Mouse
     /// Affected Methods: updateMouse
     /*********************************************************************************************************/
    @Mixin(Mouse.class)
    public abstract static class MouseMixin {
        @Redirect(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingSpyglass()Z"))
        private boolean slowCamera(ClientPlayerEntity clientPlayerEntity) {
            return clientPlayerEntity.isUsingSpyglass() || CalibratedCrossbowItem.isAds;
        }
    }

}
