package net.mac.mccourse.entity.ai.Task;

import com.google.common.collect.ImmutableMap;
import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.entity.ai.ModMemoryModuleTypes;
import net.mac.mccourse.entity.ai.PorcupineBrain;
import net.mac.mccourse.entity.custom.HomingRocketEntity;
import net.mac.mccourse.entity.custom.PorcupineEntity;
import net.mac.mccourse.entity.custom.SoulShulkerBulletEntity;
import net.mac.mccourse.entity.custom.TarotCardEntity;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.mob.EvokerEntity;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;


public class CallOfTheDeadTask extends MultiTickTask<PorcupineEntity> {

    public CallOfTheDeadTask() {
        super(
                ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_PRESENT,
                        ModMemoryModuleTypes.CALL_OF_THE_DEAD_COOLDOWN, MemoryModuleState.VALUE_ABSENT,
                        ModMemoryModuleTypes.ATTACK_ON_COOLDOWN, MemoryModuleState.VALUE_ABSENT));
    }

    @Override
    protected boolean shouldRun(ServerWorld world, PorcupineEntity entity) {
        return true;
    }

    @Override
    protected void run(ServerWorld world, PorcupineEntity entity, long time) {
        super.run(world, entity, time);
        LivingEntity target = getTarget(entity);
/*
        for (int i = 0; i < 5; i++){
            world.spawnEntity(new SoulShulkerBulletEntity(world, entity, target, entity.getMovementDirection().getAxis()));
            MCCourseMod.LOGGER.info("Summoned Bullet");
            world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_SHULKER_SHOOT, SoundCategory.NEUTRAL, 1, 2);
        }

 */

        Vec3d startPos = entity.getEyePos();
        Vec3d targetPos = target.getEyePos();
        Vec3d mainDirection = targetPos.subtract(startPos).normalize();

        float spreadAngle = 15.0f;

        Vec3d leftDirection = mainDirection.rotateY((float) Math.toRadians(spreadAngle));
        Vec3d rightDirection = mainDirection.rotateY((float) Math.toRadians(-spreadAngle));

        world.spawnEntity(new TarotCardEntity(world, entity, leftDirection.getX(), leftDirection.getY(), leftDirection.getZ(), 0));
        world.spawnEntity(new TarotCardEntity(world, entity, rightDirection.getX(), rightDirection.getY(), rightDirection.getZ(), 0));
        world.spawnEntity(new TarotCardEntity(world, entity, mainDirection.getX(), mainDirection.getY(), mainDirection.getZ(), 0));


            PorcupineBrain.postAttack(entity, ModMemoryModuleTypes.CALL_OF_THE_DEAD_COOLDOWN, entity.CallOfTheDeadCooldown);
        //PorcupineBrain.postAttack(entity, ModMemoryModuleTypes.CALL_OF_THE_DEAD_COOLDOWN, 30);

    }

    public LivingEntity getTarget(LivingEntity entity){
        return entity.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).get();
    }
}
