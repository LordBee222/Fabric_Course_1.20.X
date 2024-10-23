package net.mac.mccourse.mixin.RC;

import net.mac.mccourse.network.DoubleJumpPacket;
import net.mac.mccourse.util.DoubleJumpUtil;
import net.mac.mccourse.util.IPlayerDoubleJumpSaver;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "jump", at = @At(value = "HEAD"))
    private void doJump(CallbackInfo ci) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        DoubleJumpPacket.sendDoubleJumpPacket();
        IPlayerDoubleJumpSaver playerJumpData = (IPlayerDoubleJumpSaver) player;
        if (DoubleJumpUtil.canJump(playerJumpData, player)){
            DoubleJumpUtil.use(playerJumpData, player);
        }
    }
}
