package net.mac.mccourse.mixin.Trident;

import net.mac.mccourse.util.IEntityDataSaver;
import net.mac.mccourse.util.IPlayerDashSaver;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class ModPlayerDashSaver implements IPlayerDashSaver {
    private NbtCompound dashData;


    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> info){
        if(this.dashData != null){
            nbt.put("Dash", dashData);
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void injectReadMethod(NbtCompound nbt, CallbackInfo info){
        if(nbt.contains("Dash", 10)){
            this.dashData = nbt.getCompound("Dash");
        }
    }

    @Override
    public NbtCompound getDashData() {
        if(this.dashData == null){
            this.dashData = new NbtCompound();
        }
        return dashData;
    }
}
