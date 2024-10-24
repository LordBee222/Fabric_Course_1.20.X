package net.mac.mccourse.entity.ai.Task;



import com.google.common.collect.ImmutableMap;
import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.entity.ai.ModMemoryModuleTypes;
import net.mac.mccourse.entity.ai.PorcupineBrain;
import net.mac.mccourse.entity.custom.PorcupineEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;


public class RetreatTask extends MultiTickTask<PorcupineEntity> {
    private final double speed;
    private final int distance;
    private final int minDistance;

    public RetreatTask(double speed, int distance, int minDistance) {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_PRESENT,
                ModMemoryModuleTypes.FIREBALL_COOLDOWN, MemoryModuleState.VALUE_PRESENT,
                ModMemoryModuleTypes.UNLEASHED_SOULS_COOLDOWN, MemoryModuleState.VALUE_PRESENT));
        this.speed = speed;
        this.distance = distance;
        this.minDistance = minDistance;
    }

    @Override
    protected boolean shouldRun(ServerWorld world, PorcupineEntity entity) {
        MCCourseMod.LOGGER.info("Should Run? " + (entity.distanceTo(this.getTarget(entity)) <= this.minDistance));
        return entity.distanceTo(this.getTarget(entity)) <= minDistance;
    }

    @Override
    protected void run(ServerWorld world, PorcupineEntity entity, long time) {
        super.run(world, entity, time);
        MCCourseMod.LOGGER.info("Ran");
        LivingEntity player = entity.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).get();
        Vec3d entityPos = entity.getPos();
        Vec3d playerPos = player.getPos();
        Vec3d directionAway = entityPos.subtract(playerPos).normalize().multiply(distance);
        Vec3d targetPos = entityPos.add(directionAway);
        entity.getNavigation().startMovingTo(targetPos.x, targetPos.y, targetPos.z, this.speed);
        //entity.getBrain().remember(MemoryModuleType.WALK_TARGET, targetPos);
    }

    public LivingEntity getTarget(LivingEntity entity){
        return entity.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).get();
    }
}


