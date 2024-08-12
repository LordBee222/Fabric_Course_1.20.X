package net.mac.mccourse.mixin.Trident;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.util.DashUtil;
import net.mac.mccourse.util.DoubleJumpUtil;
import net.mac.mccourse.util.IPlayerDashSaver;
import net.mac.mccourse.util.IPlayerDoubleJumpSaver;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements IPlayerDashSaver, IPlayerDoubleJumpSaver{

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        IPlayerDashSaver playerDashData = (IPlayerDashSaver) player;
        if (DashUtil.hasDash(player)) {
            if (DashUtil.shouldResetDashes(playerDashData, player)) {
                MCCourseMod.LOGGER.info("RESET DASHES");
                DashUtil.resetDashes(playerDashData, player);
            }
        }

        IPlayerDoubleJumpSaver playerJumpData = (IPlayerDoubleJumpSaver) player;
        if (DoubleJumpUtil.hasCloudwalker(player)){
            if (DoubleJumpUtil.shouldResetJumps(playerJumpData, player)){
                DoubleJumpUtil.resetJumps(playerJumpData, player);
            }
        }
    }
}
