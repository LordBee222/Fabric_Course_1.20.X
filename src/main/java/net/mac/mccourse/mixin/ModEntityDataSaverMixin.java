package net.mac.mccourse.mixin;

import net.mac.mccourse.util.IEntityDataSaver;
import net.mac.mccourse.util.IEntityLodgedTridentSaver;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class ModEntityDataSaverMixin implements IEntityDataSaver, IEntityLodgedTridentSaver {
    private NbtCompound persistentData;
    private NbtCompound lodgedTridents;


    @Override
    public NbtCompound getPersistentData() {
        if(this.persistentData == null){
            this.persistentData = new NbtCompound();
        }
        return persistentData;
    }

    @Override
    public NbtCompound getLodgedTridents() {
        if(this.lodgedTridents == null){
            this.lodgedTridents = new NbtCompound();
        }
        return lodgedTridents;
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> info){
        if(this.persistentData != null){
            nbt.put("mccourse.custom_data", persistentData);
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void injectReadMethod(NbtCompound nbt, CallbackInfo info){
        if(nbt.contains("mccourse.custom_data", 10)){
            this.persistentData = nbt.getCompound("mccourse.custom_data");
        }
    }
}