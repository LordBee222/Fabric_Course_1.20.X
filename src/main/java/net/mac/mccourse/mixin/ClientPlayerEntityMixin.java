package net.mac.mccourse.mixin;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.enchantment.ModEnchantments;
import net.mac.mccourse.item.ModItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {

    @Shadow @Final protected MinecraftClient client;

    @Inject(method = "isSneaking", at = @At("HEAD"))
    private void checkSneakZoom(CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (!player.getEntityWorld().isClient()) return;
        ItemStack stack = player.getMainHandStack();
        if (stack.isOf(Items.CROSSBOW)) {
            boolean hasScope = stack.getEnchantments().asString().contains(EnchantmentHelper.getEnchantmentId(ModEnchantments.SCOPE).toString());
            int scopeLevel = EnchantmentHelper.getLevel(ModEnchantments.SCOPE, stack);
            if (hasScope){
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
    private void checkHoldingScopedCrossbow(CallbackInfo ci){
        PlayerEntity player = (PlayerEntity) (Object) this;
        ItemStack stack = player.getMainHandStack();
        if (!stack.isOf(Items.CROSSBOW)) MCCourseMod.zoom = false;
    }

    private static double getZoomFromScopeLevel(int scopeLevel){
        switch (scopeLevel){
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

