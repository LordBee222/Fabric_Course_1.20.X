package net.mac.mccourse.entity.brain.task;

import com.google.common.collect.ImmutableMap;
import net.mac.mccourse.entity.brain.memory_module_types.ModMemoryModulesTypes;
import net.mac.mccourse.entity.custom.SoulmellowEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class SummonSpiritsTask extends MultiTickTask<SoulmellowEntity> {
    private int cooldown;

    public SummonSpiritsTask() {
        super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_PRESENT));
        this.cooldown = 100;
    }

    @Override
    protected boolean shouldRun(ServerWorld world, SoulmellowEntity entity) {
        return entity.getBrain().hasMemoryModule(MemoryModuleType.ATTACK_TARGET) && entity.isRampaging();
    }

    @Override
    protected void run(ServerWorld world, SoulmellowEntity entity, long time) {
        BlockPos entityPos = entity.getBlockPos();
        VexEntity vex = EntityType.VEX.create(world);
        if (vex != null) {
            vex.refreshPositionAndAngles(entityPos, entity.getYaw(), entity.getPitch());
            vex.setOwner(entity);
            world.spawnEntity(vex);
        }
    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld world, SoulmellowEntity entity, long time) {
        return entity.getBrain().hasMemoryModule(MemoryModuleType.ATTACK_TARGET) && entity.isRampaging();
    }

    @Override
    protected void finishRunning(ServerWorld world, SoulmellowEntity entity, long time) {
    }

    @Override
    protected void keepRunning(ServerWorld world, SoulmellowEntity entity, long time) {
        super.keepRunning(world, entity, time);
        BlockPos entityPos = entity.getBlockPos();

        VexEntity vex = EntityType.VEX.create(world);
        if (vex != null) {
            vex.refreshPositionAndAngles(entityPos, entity.getYaw(), entity.getPitch());
            vex.setOwner(entity);
            world.spawnEntity(vex);
        }
    }

    public int getCooldown;
}

