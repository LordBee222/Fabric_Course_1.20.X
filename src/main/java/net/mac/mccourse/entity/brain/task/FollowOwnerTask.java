package net.mac.mccourse.entity.brain.task;

import com.google.common.collect.ImmutableMap;
import net.mac.mccourse.entity.custom.SoulmellowEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.server.world.ServerWorld;

public class FollowOwnerTask extends MultiTickTask<SoulmellowEntity> {
    private final double     speed;
    private final float maxDistance;
    private final float minDistance;

    public FollowOwnerTask(double speed, float maxDistance, float minDistance) {
        super(ImmutableMap.of(
                MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_ABSENT // Ensure the entity doesn't have a walk target already
        ));
        this.speed = speed;
        this.maxDistance = maxDistance;
        this.minDistance = minDistance;
    }

    @Override
    protected boolean shouldRun(ServerWorld world, SoulmellowEntity entity) {
        // Get the owner from NBT (assuming the SoulmellowEntity has a getOwner() method)
        LivingEntity owner = (LivingEntity) entity.getOwner();
        return owner != null && entity.squaredDistanceTo(owner) > (double)(this.minDistance * this.minDistance);
    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld world, SoulmellowEntity entity, long time) {
        return false;
    }

    @Override
    protected void run(ServerWorld world, SoulmellowEntity entity, long time) {
        LivingEntity owner = (LivingEntity) entity.getOwner();
        if (owner != null) {
            entity.getBrain().remember(MemoryModuleType.WALK_TARGET, new WalkTarget((Entity) owner, (float) this.speed, (int) this.maxDistance));
        }
    }

    @Override
    protected void finishRunning(ServerWorld world, SoulmellowEntity entity, long time) {
        // Forget the WALK_TARGET when done
        entity.getBrain().forget(MemoryModuleType.WALK_TARGET);
    }
}

