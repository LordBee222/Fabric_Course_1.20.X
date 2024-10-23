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
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;


public class UnleashedSoulBlastTask extends MultiTickTask<PorcupineEntity> {
    private double radius;
    private double verticalVelocity;
    private double horizontalVelocity;

    public UnleashedSoulBlastTask(double radius, double verticalKnockback, double horizontalKnockback) {
        super(
                ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_PRESENT,
                        ModMemoryModuleTypes.UNLEASHED_SOULS_COOLDOWN, MemoryModuleState.VALUE_ABSENT,
                        ModMemoryModuleTypes.ATTACK_ON_COOLDOWN, MemoryModuleState.VALUE_ABSENT));
        MCCourseMod.LOGGER.info("Init Unleashed Blast Task");
        this.radius = radius;
        this.verticalVelocity = verticalKnockback;
        this.horizontalVelocity = horizontalKnockback;

    }

    // checkExtraStartConditions
    @Override
    protected boolean shouldRun(ServerWorld world, PorcupineEntity entity) {
        boolean bl = entity.distanceTo(this.getTarget(entity)) <= 6;

        return bl ;

    }

    // start
    @Override
    protected void run(ServerWorld world, PorcupineEntity entity, long time) {
        super.run(world, entity, time);

        List<Entity> entities = world.getOtherEntities(entity, new Box(
                entity.getX() - this.radius, entity.getY() - this.radius, entity.getZ() - this.radius,
                entity.getX() + this.radius, entity.getY() + this.radius, entity.getZ() + this.radius
        ));
        world.spawnParticles(ParticleTypes.EXPLOSION_EMITTER, entity.getX(), entity.getY(), entity.getZ(), 1, 0, 0, 0, 0);

        for (Entity target : entities) {
            Vec3d knockbackDirection = target.getPos().subtract(target.getPos()).normalize();
            Vec3d addedVelocity = new Vec3d(knockbackDirection.x * this.horizontalVelocity, this.verticalVelocity, knockbackDirection.z * this.horizontalVelocity);
            target.addVelocity(addedVelocity.x, addedVelocity.y, addedVelocity.z);
            target.velocityModified = true;
        }


        PorcupineBrain.postAttack(entity, ModMemoryModuleTypes.UNLEASHED_SOULS_COOLDOWN, 150);
    }

    public LivingEntity getTarget(LivingEntity entity){
        return entity.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).get();
    }
}


