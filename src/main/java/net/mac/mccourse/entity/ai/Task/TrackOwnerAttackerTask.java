package net.mac.mccourse.entity.ai.Task;

import com.google.common.collect.ImmutableMap;
import net.mac.mccourse.entity.custom.PorcupineEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.server.world.ServerWorld;

public class TrackOwnerAttackerTask extends MultiTickTask<PorcupineEntity> {

    public TrackOwnerAttackerTask() {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_ABSENT
        ));
    }

    @Override
    protected boolean shouldRun(ServerWorld world, PorcupineEntity entity) {
        LivingEntity owner = entity.getOwner();
        if (owner == null || !entity.isTamed() || entity.isSitting()) {
            return false;
        }
        LivingEntity attacker = owner.getAttacker();
        return attacker != null && entity.canAttackWithOwner(attacker, owner);
    }

    @Override
    protected void run(ServerWorld world, PorcupineEntity entity, long time) {
        LivingEntity owner = entity.getOwner();
        if (owner != null) {
            LivingEntity attacker = owner.getAttacker();
            if (attacker != null) {
                entity.getBrain().remember(MemoryModuleType.ATTACK_TARGET, attacker, 200L);
            }
        }
    }
}
