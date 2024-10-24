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
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Unit;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.List;

import com.google.common.collect.ImmutableMap;
import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.entity.ai.ModMemoryModuleTypes;
import net.mac.mccourse.entity.ai.PorcupineBrain;
import net.mac.mccourse.entity.custom.PorcupineEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;


public class PossessionTask extends MultiTickTask<PorcupineEntity> {

    public PossessionTask() {
        super(
                ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_PRESENT,
                        ModMemoryModuleTypes.POSSESSION_COOLDOWN, MemoryModuleState.VALUE_ABSENT,
                        ModMemoryModuleTypes.ATTACK_ON_COOLDOWN, MemoryModuleState.VALUE_ABSENT));
        MCCourseMod.LOGGER.info("Init Possession Task");
    }


    // checkExtraStartConditions
    @Override
    protected boolean shouldRun(ServerWorld world, PorcupineEntity entity) {
        //return true;
        return entity.distanceTo(getTarget(entity)) >= 4;
    }

    // start
    @Override
    protected void run(ServerWorld world, PorcupineEntity entity, long time) {
        super.run(world, entity, time);
        LivingEntity target = this.getTarget(entity);
        Vec3d targetPos = target.getPos();

        Vec3d startPos = entity.getCameraPosVec(1.0F);


        // Perform the raycast
        HitResult hitResult = world.raycast(new RaycastContext(
                startPos, targetPos,
                RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE,
                entity));

        Vec3d hitPos = hitResult.getPos();

        if (hitResult.getType() == HitResult.Type.MISS) {
            // No blocks or entities are in the way
            System.out.println("Clear line of sight between the mobs!");
        } else if (hitResult.getType() == HitResult.Type.BLOCK) {
            // Block is in the way
            System.out.println("A block is obstructing the view between the mobs.");
        } else if (hitResult.getType() == HitResult.Type.ENTITY) {
            // Entity is hit by the raycast
            EntityHitResult entityHit = (EntityHitResult) hitResult;
            Entity hitEntity = entityHit.getEntity();
            System.out.println("An entity is hit: " + hitEntity.getName().getString());
        }

        // Step 2: Sample points along the ray's path
        double step = 0.2; // Smaller step = more particles
        Vec3d rayDirection = hitPos.subtract(startPos).normalize(); // Normalize direction
        double distance = startPos.distanceTo(hitPos);

        for (double i = 0; i < distance; i += step) {
            Vec3d particlePos = startPos.add(rayDirection.multiply(i));
            // Step 3: Spawn particles at the sampled position
            world.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME, particlePos.getX(), particlePos.getY(), particlePos.getZ(), 1, 0.2, 0.2, 0.2, 0.1);
        }

        world.createExplosion(null, hitPos.getX(), hitPos.getY(), hitPos.getZ(), 5, false, World.ExplosionSourceType.TNT);
        PorcupineBrain.postAttack(entity, ModMemoryModuleTypes.POSSESSION_COOLDOWN, entity.PossessionCooldown);
    }

    public LivingEntity getTarget(LivingEntity entity) {
        return entity.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).get();
    }

}

