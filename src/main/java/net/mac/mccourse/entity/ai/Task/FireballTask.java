package net.mac.mccourse.entity.ai.Task;

import com.google.common.collect.ImmutableMap;
import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.entity.ai.ModMemoryModuleTypes;
import net.mac.mccourse.entity.ai.PorcupineBrain;
import net.mac.mccourse.entity.custom.HomingRocketEntity;
import net.mac.mccourse.entity.custom.PorcupineEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;


public class FireballTask extends MultiTickTask<PorcupineEntity> {

    public FireballTask() {
        super(
                ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_PRESENT,
                        ModMemoryModuleTypes.FIREBALL_COOLDOWN, MemoryModuleState.VALUE_ABSENT,
                        ModMemoryModuleTypes.ATTACK_ON_COOLDOWN, MemoryModuleState.VALUE_ABSENT));
        MCCourseMod.LOGGER.info("Init Fireball Task");
    }


    // checkExtraStartConditions
    @Override
    protected boolean shouldRun(ServerWorld world, PorcupineEntity entity) {
        float distanceTo = entity.distanceTo(getTarget(entity));
        return distanceTo >= 6 && distanceTo < 50;
    }

    // start
    @Override
    protected void run(ServerWorld world, PorcupineEntity entity, long time) {
        super.run(world, entity, time);
        LivingEntity target = this.getTarget(entity);
        double radius = 50;

        world.spawnEntity(
                new HomingRocketEntity(
                        world,
                        target,
                        entity));




        /*Vec3d vec3d = entity.getRotationVec(1.0f);
            double x = target.getX() - (entity.getX() + vec3d.x * 4.0);
            double y = target.getBodyY(0.5) - (0.5 + entity.getBodyY(0.5));
            double z = target.getZ() - (entity.getZ() + vec3d.z * 4.0);

            FireballEntity fireballEntity = new FireballEntity(world, (LivingEntity)entity, x, y, z, 4);
            fireballEntity.setPosition(entity.getX(), entity.getBodyY(0.5) + 0.5, fireballEntity.getZ());
            world.spawnEntity(fireballEntity); */

            world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_ELDER_GUARDIAN_HURT, SoundCategory.NEUTRAL, 1, 2);
            //PorcupineBrain.postAttack(entity, ModMemoryModuleTypes.FIREBALL_COOLDOWN, entity.FireballCooldown);
        PorcupineBrain.postAttack(entity, ModMemoryModuleTypes.FIREBALL_COOLDOWN, 60);

        }

        public LivingEntity getTarget (LivingEntity entity){
            return entity.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).get();
        }
    }
