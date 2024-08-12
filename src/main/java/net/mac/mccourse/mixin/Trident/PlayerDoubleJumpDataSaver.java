package net.mac.mccourse.mixin.Trident;

import net.mac.mccourse.util.IPlayerDashSaver;
import net.mac.mccourse.util.IPlayerDoubleJumpSaver;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class PlayerDoubleJumpDataSaver implements IPlayerDoubleJumpSaver {
    private NbtCompound jumpData;
    private static String nbtJumpKey = "cloudwalkerJumps";


    @Override
    public NbtCompound getDoubleJumpData() {
        if(this.jumpData == null){
            this.jumpData = new NbtCompound();
        }
        return jumpData;
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> info){
        if(this.jumpData != null){
            nbt.put(nbtJumpKey, jumpData);
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void injectReadMethod(NbtCompound nbt, CallbackInfo info){
        if(nbt.contains(nbtJumpKey, 10)){
            this.jumpData = nbt.getCompound(nbtJumpKey);
        }
    }
}
