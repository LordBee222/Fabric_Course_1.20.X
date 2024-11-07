package net.mac.mccourse.entity.ai.Sensors;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.entity.ModEntities;
import net.mac.mccourse.entity.custom.PorcupineEntity;
import net.mac.mccourse.util.ModTags;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.NearestVisibleLivingEntitySensor;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.EntityTypeTags;

import java.util.ArrayList;
import java.util.List;

public class SoulmellowAttackablesSensor extends NearestVisibleLivingEntitySensor {
    public static final float TARGET_RANGE = 100.0f;

    @Override
    protected boolean matches(LivingEntity entity, LivingEntity target) {
        return this.isInRange(entity, target) && (this.isAlwaysHostileTo(target, entity) && Sensor.testAttackableTargetPredicate(entity, target));
    }

    private boolean isAlwaysHostileTo(LivingEntity target, LivingEntity soulmellow) {

        EntityType targetType = target.getType();
        boolean isOwnable = soulmellow instanceof Ownable;
        boolean isTargetOwner = isOwnable && ((Ownable) soulmellow).getOwner() != null && ((Ownable) soulmellow).getOwner().getId() == target.getId();

        return !(targetType == EntityType.PLAYER) &&
                !(targetType == ModEntities.PORCUPINE) &&
                !isTargetOwner;
    }

    private boolean isInRange(LivingEntity axolotl, LivingEntity target) {
        return target.squaredDistanceTo(axolotl) <= TARGET_RANGE;
    }

    @Override
    protected MemoryModuleType<LivingEntity> getOutputMemoryModule() {
        return MemoryModuleType.NEAREST_ATTACKABLE;
    }
}


