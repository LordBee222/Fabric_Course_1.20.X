package net.mac.mccourse.entity.ai;

import com.google.common.collect.ImmutableMap;
import net.mac.mccourse.entity.custom.PorcupineEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;

import java.util.Optional;

public class PorcupineAttackTask extends MultiTickTask<PorcupineEntity> {
    private static final int ATTACK_DELAY = 20;
    private int ticksUntilNextAttack = ATTACK_DELAY;
    private boolean shouldCountTillNextAttack = false;

    public PorcupineAttackTask() {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_PRESENT
        ), ATTACK_DELAY); // Runs for at least ATTACK_DELAY ticks
    }

    @Override
    protected boolean shouldRun(ServerWorld world, PorcupineEntity entity) {
        return entity.getBrain().hasMemoryModule(MemoryModuleType.ATTACK_TARGET);
    }

    @Override
    protected void run(ServerWorld world, PorcupineEntity entity, long time) {
        Optional<LivingEntity> attackTarget = entity.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET);

        attackTarget.ifPresent(target -> {
            double distance = entity.squaredDistanceTo(target);
            if (isEnemyWithinAttackDistance(entity, target, distance)) {
                shouldCountTillNextAttack = true;

                if (isTimeToStartAttackAnimation()) {
                    entity.setAttacking(true);
                }

                if (isTimeToAttack()) {
                    LookTargetUtil.lookAt(entity, target);
                    performAttack(entity, target);
                }
            } else {
                resetAttackCooldown();
                shouldCountTillNextAttack = false;
                entity.setAttacking(false);
                entity.attackAnimationTimeout = 0;
            }
        });
    }

    @Override
    protected void keepRunning(ServerWorld world, PorcupineEntity entity, long time) {
        if (shouldCountTillNextAttack) {
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
        }
    }

    @Override
    protected void finishRunning(ServerWorld world, PorcupineEntity entity, long time) {
        entity.setAttacking(false);
        super.finishRunning(world, entity, time);
    }

    private boolean isEnemyWithinAttackDistance(PorcupineEntity entity, LivingEntity target, double distance) {
        return distance <= getSquaredMaxAttackDistance(entity, target);
    }

    private double getSquaredMaxAttackDistance(LivingEntity self,LivingEntity entity){
        return self.getWidth() * 2.0f * (self.getWidth() * 2.0f) + entity.getWidth();
    }

    private boolean isTimeToStartAttackAnimation() {
        return this.ticksUntilNextAttack <= ATTACK_DELAY;
    }

    private boolean isTimeToAttack() {
        return this.ticksUntilNextAttack <= 0;
    }

    private void resetAttackCooldown() {
        this.ticksUntilNextAttack = ATTACK_DELAY * 2;
    }

    private void performAttack(PorcupineEntity entity, LivingEntity target) {
        resetAttackCooldown();
        entity.swingHand(Hand.MAIN_HAND);
        entity.tryAttack(target);
    }
}