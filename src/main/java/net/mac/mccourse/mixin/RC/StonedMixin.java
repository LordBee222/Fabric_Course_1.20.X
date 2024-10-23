package net.mac.mccourse.mixin.RC;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.effect.ModEffects;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class StonedMixin {
    @Mixin(Mouse.class)
    public abstract static class MouseMixin {
        @Inject(method = "onMouseButton", at = @At("HEAD"), cancellable = true)
        private void onMouseButton(long window, int button, int action, int mods, CallbackInfo callbackInfo) {
            if (MCCourseMod.isStoned(MinecraftClient.getInstance().player) && !MinecraftClient.getInstance().isPaused()) {
                //KeyBinding.unpressAll();
                //callbackInfo.cancel();
            }
        }

        @Inject(method = "updateMouse", at = @At("HEAD"), cancellable = true)
        private void onUpdateMouse(CallbackInfo ci) {
            if (MCCourseMod.isStoned(MinecraftClient.getInstance().player) && !MinecraftClient.getInstance().isPaused()) {
                //KeyBinding.unpressAll();
                //ci.cancel();
            }
        }

        @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
        private void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo callbackInfo) {
            if (MCCourseMod.isStoned(MinecraftClient.getInstance().player) && !MinecraftClient.getInstance().isPaused()) {
                //KeyBinding.unpressAll();
                //callbackInfo.cancel();
            }
        }
    }



    /********************************************************************************************************
     /// Sonic Enchantment Mixin
     /// Affected Methods: getSpeed
     /*********************************************************************************************************/
    @Mixin(Camera.class)
    public abstract static class CameraMixin {
        @Inject(method = "update", at = @At("HEAD"), cancellable = true)
        public void update(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo callbackInfo) {
            if (MCCourseMod.isStoned(MinecraftClient.getInstance().player)) {
               // callbackInfo.cancel();
            }
        }
    }

    /********************************************************************************************************
     /// Sonic Enchantment Mixin
     /// Affected Methods: getSpeed
     /*********************************************************************************************************/
    @Mixin(Keyboard.class)
    public abstract static class KeyboardMixin {
        @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
        public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo callbackInfo) {
            if (MCCourseMod.isStoned(MinecraftClient.getInstance().player) && !MinecraftClient.getInstance().isPaused()) {
                //KeyBinding.unpressAll();
                //callbackInfo.cancel();
            }
        }
    }


    /********************************************************************************************************
     /// Sonic Enchantment Mixin
     /// Affected Methods: getSpeed
     /*********************************************************************************************************/
    @Mixin(GameRenderer.class)
    public static abstract class HandRenderMixin {

        @Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
        public void onRenderHand(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo ci) {
            if (MCCourseMod.isStoned(MinecraftClient.getInstance().player)) {
                //ci.cancel();
            }
        }

        @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
        public void onBobView(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
            if (MCCourseMod.isStoned(MinecraftClient.getInstance().player)) {
               // ci.cancel();
            }
        }
    }


    /********************************************************************************************************
     /// Sonic Enchantment Mixin
     /// Affected Methods: getSpeed
     /*********************************************************************************************************/
    @Mixin(HeldItemRenderer.class)
    public static abstract class HeldItemRendererMixin {

        @Inject(method = "updateHeldItems", at = @At("HEAD"), cancellable = true)
        public void onUpdateHeldItems(CallbackInfo ci) {
            if (MCCourseMod.isStoned(MinecraftClient.getInstance().player)) {
                //ci.cancel();
            }
        }

        @Inject(method = "renderFirstPersonItem", at = @At("HEAD"), cancellable = true)
        public void onRenderFPItem(CallbackInfo ci) {
            if (MCCourseMod.isStoned(MinecraftClient.getInstance().player)) {
                //ci.cancel();
            }
        }
    }



}
