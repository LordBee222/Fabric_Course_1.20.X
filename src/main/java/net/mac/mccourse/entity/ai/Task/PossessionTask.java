package net.mac.mccourse.entity.ai.Task;

import com.google.common.collect.ImmutableMap;
import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.entity.ai.ModMemoryModuleTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Unit;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.List;


public class PossessionTask extends MultiTickTask<LivingEntity> {
    private static final int HORIZONTAL_RANGE = 15;
    private static final int VERTICAL_RANGE = 20;
    private static final double field_38852 = 0.5;
    private static final double field_38853 = 2.5;
    public static final int COOLDOWN = 40;
    private static final int SOUND_DELAY = MathHelper.ceil(34.0);
    private static final int RUN_TIME = MathHelper.ceil(60.0f);

    public PossessionTask(double radius, double verticalKnockback, double horizontalKnockback) {
        super(
                ImmutableMap.of(
                        MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_PRESENT,
                        MemoryModuleType.SONIC_BOOM_COOLDOWN, MemoryModuleState.VALUE_ABSENT,
                        MemoryModuleType.SONIC_BOOM_SOUND_COOLDOWN, MemoryModuleState.REGISTERED,
                        MemoryModuleType.SONIC_BOOM_SOUND_DELAY, MemoryModuleState.REGISTERED), RUN_TIME);


    }
/*
    // checkExtraStartConditions
    @Override
    protected boolean shouldRun(ServerWorld world, LivingEntity entity) {
        return entity.isInRange(entity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.ATTACK_TARGET).get(), 15.0, 20.0);

    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld world, LivingEntity entity, long time) {
        return true;
    }

    // start
    @Override
    protected void run(ServerWorld world, LivingEntity entity, long time) {
        MCCourseMod.LOGGER.info("Ran Unleashed Blast Task: " + this.getTarget(entity));

    }

    @Override
    protected void keepRunning(ServerWorld world, LivingEntity entity, long time) {
        entity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.ATTACK_TARGET).ifPresent(target -> entity.getLookControl().lookAt(target.getPos()));
        if (entity.getBrain().hasMemoryModule(MemoryModuleType.SONIC_BOOM_SOUND_DELAY) || entity.getBrain().hasMemoryModule(MemoryModuleType.SONIC_BOOM_SOUND_COOLDOWN)) {
            return;
        }
        entity.getBrain().remember(MemoryModuleType.SONIC_BOOM_SOUND_COOLDOWN, Unit.INSTANCE, RUN_TIME - SOUND_DELAY);
        entity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.ATTACK_TARGET).filter(entity::isValidTarget).filter(target -> entity.isInRange((Entity)target, 15.0, 20.0)).ifPresent(target -> {
            Vec3d vec3d = entity.getPos().add(0.0, 1.6f, 0.0);
            Vec3d vec3d2 = target.getEyePos().subtract(vec3d);
            Vec3d vec3d3 = vec3d2.normalize();
            for (int i = 1; i < MathHelper.floor(vec3d2.length()) + 7; ++i) {
                Vec3d vec3d4 = vec3d.add(vec3d3.multiply(i));
                world.spawnParticles(ParticleTypes.SONIC_BOOM, vec3d4.x, vec3d4.y, vec3d4.z, 1, 0.0, 0.0, 0.0, 0.0);
            }
            entity.playSound(SoundEvents.ENTITY_WARDEN_SONIC_BOOM, 3.0f, 1.0f);
            target.damage(world.getDamageSources().sonicBoom(entity), 10.0f);
            double d = 0.5 * (1.0 - target.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
            double e = 2.5 * (1.0 - target.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
            target.addVelocity(vec3d3.getX() * e, vec3d3.getY() * d, vec3d3.getZ() * e);
        });
    }

    public LivingEntity getTarget(LivingEntity entity){
        return entity.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).get();
    }

 */
}
