package net.mac.mccourse.mixin;

import net.mac.mccourse.util.IEntityDataSaver;
import net.mac.mccourse.util.IEntityRiverStyxDashSaver;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class RiverStyxDashDataSaverMixin implements IEntityRiverStyxDashSaver {
    private NbtCompound river_styx_dash_data;
    private static final String RIVER_STYX_DASHES_KEY = "river_styx_dashes";

    @Override
    public NbtCompound getDashData() {
        if(this.river_styx_dash_data == null){
            this.river_styx_dash_data = new NbtCompound();
        }
        return river_styx_dash_data;
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> info){
        if(this.river_styx_dash_data != null){
            nbt.put(RIVER_STYX_DASHES_KEY, river_styx_dash_data);
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void injectReadMethod(NbtCompound nbt, CallbackInfo info){
        if(nbt.contains(RIVER_STYX_DASHES_KEY, 10)){
            this.river_styx_dash_data = nbt.getCompound(RIVER_STYX_DASHES_KEY);
        }
    }
}