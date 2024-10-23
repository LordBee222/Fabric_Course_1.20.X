package net.mac.mccourse.mixin;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.util.SomeDataUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.mac.mccourse.util.SomeDataUtil.getSomeData;
import static net.mac.mccourse.util.SomeDataUtil.setSomeData;

@Mixin(LivingEntity.class)
public abstract class SomeDataMixin {
    private static final TrackedData<Integer> SOME_DATA = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);;

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void initData(CallbackInfo ci) {

        // get the entity
        LivingEntity entity = (LivingEntity) (Object) this;

        // Start entity tracking data
        entity.getDataTracker().startTracking(SOME_DATA, 50);
        MCCourseMod.LOGGER.info("Init Data, " + getSomeData(entity));

        setSomeData(entity, (getSomeData(entity) + 1));
        MCCourseMod.LOGGER.info("SET Data, " + getSomeData(entity));

    }

    static{
        MCCourseMod.LOGGER.info("Init Some Date For Util");
        SomeDataUtil.SOME_DATA = SOME_DATA;
    }
}
