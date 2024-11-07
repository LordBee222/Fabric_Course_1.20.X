package net.mac.mccourse.entity.ai.Task;

import com.google.common.collect.ImmutableMap;
import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.entity.ai.ModMemoryModuleTypes;
import net.mac.mccourse.entity.ai.PorcupineBrain;
import net.mac.mccourse.entity.custom.PorcupineEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;

import java.util.List;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;


public class PossessionTask extends MultiTickTask<PorcupineEntity> {
    private LivingEntity target;

    public PossessionTask() {
        super(
                ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_PRESENT,
                        ModMemoryModuleTypes.POSSESSION_COOLDOWN, MemoryModuleState.VALUE_ABSENT,
                        ModMemoryModuleTypes.ATTACK_ON_COOLDOWN, MemoryModuleState.VALUE_ABSENT), 100);
        MCCourseMod.LOGGER.info("Init Possession Task");
    }


    // checkExtraStartConditions
    @Override
    protected boolean shouldRun(ServerWorld world, PorcupineEntity entity) {
        return entity.distanceTo(this.getTarget(entity)) <= 20;
    }

    // start
    @Override
    protected void run(ServerWorld world, PorcupineEntity entity, long time) {
        super.run(world, entity, time);
        target = this.getTarget(entity);
        entity.getBrain().remember(ModMemoryModuleTypes.LIFEFORCE_TIME, 100);

    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld world, PorcupineEntity entity, long time) {
        return entity.getBrain().getOptionalMemory(ModMemoryModuleTypes.LIFEFORCE_TIME).get() > 0 &&
                this.target.isAlive() && this.target != null;
    }

    @Override
    protected void keepRunning(ServerWorld world, PorcupineEntity entity, long time) {
        super.keepRunning(world, entity, time);
        LivingEntity target = this.target;
        LivingEntity owner = entity.getOwner();

        if (entity.getBrain().getOptionalMemory(ModMemoryModuleTypes.LIFEFORCE_TIME).get() % 5 == 0) entity.playSound(SoundEvents.ENTITY_WARDEN_SONIC_BOOM, 3.0f, 1.6f);
        //if (entity.getBrain().getOptionalMemory(ModMemoryModuleTypes.LIFEFORCE_TIME).get() %  == 0) {
            boolean bl = false;
            boolean bl2 = false;
            if (target != null) {   // Self -> Target
                Vec3d startPos = entity.getEyePos();
                Vec3d endPos = target.getEyePos();
                Vec3d SelfToTargetDirection = endPos.subtract(startPos).normalize();
                castRayFromSelfToTarget(world, entity, startPos, SelfToTargetDirection, endPos);
                bl = true;
            }
            if (owner != null && bl) {    // Self -> Owner
                Vec3d startPos = entity.getEyePos();
                Vec3d endPos = owner.getEyePos();
                Vec3d SelfToOwnerDirection = endPos.subtract(startPos).normalize();
                castRayFromSelfToOwner(world, entity, startPos, SelfToOwnerDirection, endPos);
                bl2 = true;
            }
            if (bl && bl2) {    // Target -> Owner
                Vec3d startPos = target.getEyePos();
                Vec3d endPos = owner.getEyePos();
                Vec3d TargetToOwnerDirection = endPos.subtract(startPos).normalize();
                castRayFromTargetToOwner(world, target, startPos, TargetToOwnerDirection, endPos, entity);
            }
        //}
        entity.getBrain().remember(ModMemoryModuleTypes.LIFEFORCE_TIME, (entity.getBrain().getOptionalMemory(ModMemoryModuleTypes.LIFEFORCE_TIME).get() - 1));
    }

    private void castRayFromSelfToTarget(World world, PorcupineEntity entity, Vec3d start, Vec3d direction, Vec3d targetPos) {
        if (world instanceof ServerWorld serverWorld) {
            double maxDistance = start.distanceTo(targetPos);
            Vec3d endPos = start.add(direction.multiply(maxDistance));

            // Do Raycast -> Save HitResult
            BlockHitResult hitResult = world.raycast(new RaycastContext(start, endPos, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity));
            Vec3d hitPos = hitResult.getPos();

            double step = 0.3;
            Vec3d rayDirection = hitPos.subtract(start).normalize();
            double distance = start.distanceTo(hitPos);

            for (double i = 0; i < distance; i += step) {
                Vec3d particlePos = start.add(rayDirection.multiply(i));
                serverWorld.spawnParticles(ParticleTypes.ELECTRIC_SPARK, particlePos.getX(), particlePos.getY(), particlePos.getZ(), 1, 0.01, 0.01, 0.01, 0.02);
            }

            BlockPos hitBlockPos = hitResult.getBlockPos();

            List<Entity> entities = world.getOtherEntities(entity, new Box(
                    hitPos.getX() - 1, hitPos.getY() - 1, hitPos.getZ() - 1,
                    hitPos.getX() + 1, hitPos.getY() + 1, hitPos.getZ() + 1
            ));

            for (Entity target : entities) {
                if (target instanceof LivingEntity livingTarget) {
                    livingTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 200, 0));
                    livingTarget.setOnFireFor(10);
                    livingTarget.damage(entity.getDamageSources().mobAttack(entity), 1f);
                    entity.heal(0.5f);
                }
            }
        }
    }

    private void castRayFromSelfToOwner(ServerWorld world, PorcupineEntity entity, Vec3d startPos, Vec3d selfToOwnerDirection, Vec3d endPos) {
        double maxDistance = startPos.distanceTo(endPos);
        Vec3d finalPos = startPos.add(selfToOwnerDirection.multiply(maxDistance));
        BlockHitResult hitResult = world.raycast(new RaycastContext(startPos, finalPos, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity));
        Vec3d hitPos = hitResult.getPos();
        double step = 0.3;
        Vec3d rayDirection = hitPos.subtract(startPos).normalize();
        double distance = startPos.distanceTo(hitPos);
        for (double i = 0; i < distance; i += step) {
            Vec3d particlePos = startPos.add(rayDirection.multiply(i));
            world.spawnParticles(ParticleTypes.ELECTRIC_SPARK, particlePos.getX(), particlePos.getY(), particlePos.getZ(), 1, 0.01, 0.01, 0.01, 0.02);
        }
    }

    private void castRayFromTargetToOwner(ServerWorld world, LivingEntity target, Vec3d startPos, Vec3d targetToOwnerDirection, Vec3d endPos, PorcupineEntity porcupine) {
        double maxDistance = startPos.distanceTo(endPos);
        Vec3d finalPos = startPos.add(targetToOwnerDirection.multiply(maxDistance));
        BlockHitResult hitResult = world.raycast(new RaycastContext(startPos, finalPos, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, target));
        Vec3d hitPos = hitResult.getPos();
        double step = 0.3;
        Vec3d rayDirection = hitPos.subtract(startPos).normalize();
        double distance = startPos.distanceTo(hitPos);
        for (double i = 0; i < distance; i += step) {
            Vec3d particlePos = startPos.add(rayDirection.multiply(i));
            world.spawnParticles(ParticleTypes.ELECTRIC_SPARK, particlePos.getX(), particlePos.getY(), particlePos.getZ(), 1, 0.01, 0.01, 0.01, 0.02);
        }
        List<Entity> entities = world.getOtherEntities(null, new Box(
                hitPos.getX() - 1, hitPos.getY() - 1, hitPos.getZ() - 1,
                hitPos.getX() + 1, hitPos.getY() + 1, hitPos.getZ() + 1
        ));
        for (Entity hitEntitie : entities) {
            if (hitEntitie instanceof LivingEntity) {
                if (porcupine.getOwner() != null && porcupine.getOwner() instanceof PlayerEntity owner)
                    owner.heal(0.5f);
            }
        }
    }

    @Override
    protected void finishRunning(ServerWorld world, PorcupineEntity entity, long time) {
        super.finishRunning(world, entity, time);

        PorcupineBrain.postAttack(entity, ModMemoryModuleTypes.POSSESSION_COOLDOWN, entity.PossessionCooldown);
    }


    public LivingEntity getTarget(LivingEntity entity) {
        return entity.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).get();
    }
}

