package net.mac.mccourse.entity.brain;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.mac.mccourse.entity.brain.task.*;
import net.mac.mccourse.entity.custom.SoulmellowEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class SoulmellowBrain {

    protected static Brain<?> create(Brain<SoulmellowEntity> brain) {
        SoulmellowBrain.addCoreTasks(brain);
        SoulmellowBrain.addIdleTasks(brain);
        SoulmellowBrain.addAvoidTasks(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.resetPossibleActivities();
        return brain;
    }

    private static void addCoreTasks(Brain<SoulmellowEntity> brain) {
        brain.setTaskList(Activity.CORE, 0, ImmutableList.of(
                new LookAroundTask(45, 90),
                new WanderAroundTask()
        ));
    }

    private static void addIdleTasks(Brain<SoulmellowEntity> brain) {
        brain.setTaskList(Activity.IDLE, 10, ImmutableList.of(
                new FollowOwnerTask(45.0D, 90F, 1.0F)  // Task to follow owner at speed 1.0f
        ));
    }

    private static void addAvoidTasks(Brain<SoulmellowEntity> brain) {
        brain.setTaskList(Activity.AVOID, 10, ImmutableList.of(
                GoToRememberedPositionTask.createEntityBased(MemoryModuleType.AVOID_TARGET, 1.3f, 15, false),  // Move away from hostile entity at speed 1.3f
                LookAtMobWithIntervalTask.follow(8.0f, UniformIntProvider.create(30, 60)),
                ForgetTask.create(SoulmellowBrain::isSafeFromHostiles, MemoryModuleType.AVOID_TARGET)  // Forget the avoid target if it's no longer valid
        ), MemoryModuleType.AVOID_TARGET);
    }

    private static boolean isSafeFromHostiles(SoulmellowEntity soulmellow) {
        // Check if there are no hostile entities nearby
        return !soulmellow.getBrain().hasMemoryModule(MemoryModuleType.AVOID_TARGET);
    }

}