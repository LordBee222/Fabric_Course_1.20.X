package net.mac.mccourse.entity.ai.Task;



import com.google.common.collect.ImmutableMap;
import net.mac.mccourse.entity.custom.PorcupineEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;


public class AttackWithOwnerTask extends MultiTickTask<PorcupineEntity> {

    private LivingEntity lastAttackingEntity = null;
    private int lastAttackTime = -1;

    public AttackWithOwnerTask() {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_ABSENT));
    }

    @Override
    protected boolean shouldRun(ServerWorld world, PorcupineEntity entity) {
        Optional<LivingEntity> optionalOwner = Optional.ofNullable(entity.getOwner());
        if (optionalOwner.isEmpty() || !entity.isTamed() || entity.isSitting()) {
            return false;
        }

        LivingEntity owner = optionalOwner.get();
        LivingEntity ownerAttacking = owner.getAttacking();
        int ownerLastAttackTime = owner.getLastAttackTime();

        // Ensure the owner is attacking a new entity since the last check and validate attack target
        if (ownerLastAttackTime != this.lastAttackTime
                && this.canTrack(ownerAttacking, TargetPredicate.DEFAULT)
                && entity.canAttackWithOwner(ownerAttacking, owner)) {

            this.lastAttackingEntity = ownerAttacking;
            this.lastAttackTime = ownerLastAttackTime;
            return true;
        }

        return false;
    }

    @Override
    protected void run(ServerWorld world, PorcupineEntity entity, long time) {
        entity.getBrain().remember(MemoryModuleType.ATTACK_TARGET, this.lastAttackingEntity, 200L);
    }

    private boolean canTrack(LivingEntity target, TargetPredicate targetPredicate) {
        return target != null && targetPredicate.test(this.lastAttackingEntity, target);
    }
}

