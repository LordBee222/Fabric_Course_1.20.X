package net.mac.mccourse.entity.ai.Task;



import com.google.common.collect.ImmutableMap;
import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.entity.custom.PorcupineEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;


public class FollowOwnerTask extends MultiTickTask<PorcupineEntity> {

    private final float followSpeed;
    private final float followDistance;
    private final float maxDistance;

    public FollowOwnerTask(float followSpeed, float followDistance, float maxDistance) {
        super(ImmutableMap.of(
                MemoryModuleType.WALK_TARGET, MemoryModuleState.REGISTERED
        ));
        this.followSpeed = followSpeed;
        this.followDistance = followDistance;
        this.maxDistance = maxDistance;
    }

    @Override
    protected boolean shouldRun(ServerWorld world, PorcupineEntity entity) {
        LivingEntity owner = entity.getOwner();
        return owner != null && entity.squaredDistanceTo(owner) > followDistance * followDistance;
    }

    @Override
    protected void run(ServerWorld world, PorcupineEntity entity, long time) {
        LivingEntity owner = entity.getOwner();
        double distance = entity.squaredDistanceTo(owner);

        if (distance < maxDistance * maxDistance) {
            entity.getBrain().remember(MemoryModuleType.WALK_TARGET, new WalkTarget(owner.getPos(), followSpeed, 1));
        } else {
            entity.getBrain().forget(MemoryModuleType.WALK_TARGET);
        }
    }
}

