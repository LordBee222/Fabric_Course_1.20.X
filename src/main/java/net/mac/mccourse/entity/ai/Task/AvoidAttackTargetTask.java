package net.mac.mccourse.entity.ai.Task;



import com.google.common.collect.ImmutableMap;
import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.entity.ai.PorcupineBrain;
import net.mac.mccourse.entity.custom.PorcupineEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;


public class AvoidAttackTargetTask extends MultiTickTask<PorcupineEntity> {

    private final float speed;
    private final float avoidDistance;
    private final float minDistanceToOwner;

    public AvoidAttackTargetTask(float speed, float avoidDistance, float minDistanceToOwner) {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_PRESENT,
                MemoryModuleType.WALK_TARGET, MemoryModuleState.REGISTERED
        ));
        this.speed = speed;
        this.avoidDistance = avoidDistance;
        this.minDistanceToOwner = minDistanceToOwner;
    }

    @Override
    protected boolean shouldRun(ServerWorld world, PorcupineEntity entity) {
        return entity.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).isPresent() && entity.getOwner() != null;
    }

    @Override
    protected void run(ServerWorld world, PorcupineEntity entity, long time) {
        LivingEntity target = entity.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).get();
        LivingEntity owner = entity.getOwner();

        if (target.squaredDistanceTo(entity) < avoidDistance * avoidDistance) {
            Vec3d directionToOwner = owner.getPos().subtract(entity.getPos()).normalize();
            Vec3d avoidTarget = entity.getPos().add(directionToOwner.multiply(speed));
            entity.getBrain().remember(MemoryModuleType.WALK_TARGET, new WalkTarget(avoidTarget, speed, 0));
        }
    }
}

