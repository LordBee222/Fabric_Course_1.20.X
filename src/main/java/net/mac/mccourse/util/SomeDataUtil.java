package net.mac.mccourse.util;

import net.mac.mccourse.MCCourseMod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;

public class SomeDataUtil {
    public static TrackedData<Integer> SOME_DATA;

    public static int getSomeData(LivingEntity entity) {
            MCCourseMod.LOGGER.info("At Get: " + (entity.getDataTracker().get(SOME_DATA)) + ", For; " + entity.getType().toString());
            return entity.getDataTracker().get(SOME_DATA);
    }

    public static void setSomeData(LivingEntity entity, int someData) {
        MCCourseMod.LOGGER.info("At Set: " + (entity.getDataTracker().get(SOME_DATA)) + ", For; " + entity.getType().toString());
        entity.getDataTracker().set(SOME_DATA, someData);
    }
}

