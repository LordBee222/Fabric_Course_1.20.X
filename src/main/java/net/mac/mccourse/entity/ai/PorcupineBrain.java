package net.mac.mccourse.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.entity.ModEntities;
import net.mac.mccourse.entity.ai.Task.FireballTask;
import net.mac.mccourse.entity.ai.Task.UnleashedSoulBlastTask;
import net.mac.mccourse.entity.custom.PorcupineEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.util.math.intprovider.UniformIntProvider;

import java.util.Optional;

public class PorcupineBrain {
    private static final UniformIntProvider WALKING_SPEED = UniformIntProvider.create(5, 16);
    private static final UniformIntProvider LONG_JUMP_COOLDOWN_RANGE = UniformIntProvider.create(600, 1200);
    private static final UniformIntProvider RAM_COOLDOWN_RANGE = UniformIntProvider.create(600, 6000);
    private static final UniformIntProvider WALK_TOWARD_CLOSEST_ADULT_RANGE = UniformIntProvider.create(5, 16);

    public static Brain<?> create(Brain<PorcupineEntity> brain) {
        addCoreTasks(brain);
        addIdleTasks(brain);
        addFightTasks(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.resetPossibleActivities();
        //initMemories(brain);
        //brain.remember(ModMemoryModuleTypes.ATTACK_ON_COOLDOWN, false);
        return brain;
    }

    private static void initMemories(Brain<PorcupineEntity> brain){
        brain.remember(ModMemoryModuleTypes.FIREBALL_COOLDOWN, false);
        brain.remember(ModMemoryModuleTypes.UNLEASHED_SOULS_COOLDOWN, false);

    }

    private static void addCoreTasks(Brain<PorcupineEntity> brain) {
        brain.setTaskList(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new LookAroundTask(45, 90),
                        new WanderAroundTask()/*,
                        new TemptationCooldownTask(ModMemoryModuleTypes.FIREBALL_COOLDOWN),
                        new TemptationCooldownTask(ModMemoryModuleTypes.UNLEASHED_SOULS_COOLDOWN)*/));
    }

    private static void addIdleTasks(Brain<PorcupineEntity> brain) {
        brain.setTaskList(
                Activity.IDLE,
                10,
                ImmutableList.of(
                        PacifyTask.create(MemoryModuleType.NEAREST_REPELLENT, 200),
                        GoToRememberedPositionTask.createPosBased(MemoryModuleType.NEAREST_REPELLENT, 1.0f, 8, true),
                        UpdateAttackTargetTask.create(PorcupineBrain::getNearestVisibleTargetablePlayer),
                        TaskTriggerer.runIf(PorcupineEntity::isAdult, GoToRememberedPositionTask.createEntityBased(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLIN, 0.4f, 8, false)),
                        LookAtMobWithIntervalTask.follow(8.0f, UniformIntProvider.create(30, 60)),
                        WalkTowardClosestAdultTask.create(WALK_TOWARD_CLOSEST_ADULT_RANGE, 0.6f),
                        makeRandomWalkTask()));
    }

    private static void addFightTasks(Brain<PorcupineEntity> brain) {
        brain.setTaskList(
                Activity.FIGHT,
                10,
                ImmutableList.of(
                        new UnleashedSoulBlastTask(3, 1.5, 3.75),
                        new FireballTask(),
                        RangedApproachTask.create(1.0f),
                        TaskTriggerer.runIf(PorcupineEntity::isAdult, MeleeAttackTask.create(40)),
                        TaskTriggerer.runIf(PassiveEntity::isBaby, MeleeAttackTask.create(15)),
                        ForgetAttackTargetTask.create()),
                MemoryModuleType.ATTACK_TARGET);
    }

    private static RandomTask<PorcupineEntity> makeRandomWalkTask() {
        return new RandomTask<PorcupineEntity>(ImmutableList.of(Pair.of(StrollTask.create(0.4f), 2), Pair.of(GoTowardsLookTargetTask.create(0.4f, 3), 2), Pair.of(new WaitTask(30, 60), 1)));
    }

    public static void refreshActivities(PorcupineEntity porcupine) {
        Brain<PorcupineEntity> brain = (Brain<PorcupineEntity>) porcupine.getBrain();
        Activity activity = brain.getFirstPossibleNonCoreActivity().orElse(null);
        brain.resetPossibleActivities(ImmutableList.of(Activity.FIGHT, Activity.AVOID, Activity.IDLE));
        Activity activity2 = brain.getFirstPossibleNonCoreActivity().orElse(null);
        porcupine.setAttacking(brain.hasMemoryModule(MemoryModuleType.ATTACK_TARGET));
        //if (brain.hasMemoryModule(ModMemoryModuleTypes.FIREBALL_COOLDOWN)) brain.remember(ModMemoryModuleTypes.FIREBALL_COOLDOWN, ((brain.getOptionalMemory(ModMemoryModuleTypes.FIREBALL_COOLDOWN).get()) -1));
        //if (brain.hasMemoryModule(ModMemoryModuleTypes.UNLEASHED_SOULS_COOLDOWN)) brain.remember(ModMemoryModuleTypes.UNLEASHED_SOULS_COOLDOWN, ((brain.getOptionalMemory(ModMemoryModuleTypes.UNLEASHED_SOULS_COOLDOWN).get()) -1));
    }

    public static void postAttack(PorcupineEntity porcupine, MemoryModuleType<Boolean> attackToCooldown, int cooldown){
        Brain<PorcupineEntity> brain = porcupine.getBrain();
        brain.remember(ModMemoryModuleTypes.ATTACK_ON_COOLDOWN, true, 30);
        brain.remember(attackToCooldown, true, cooldown);
        MCCourseMod.LOGGER.info("Post Attack");
        MCCourseMod.LOGGER.info("Fireball Cooling Down? " + brain.hasMemoryModule(ModMemoryModuleTypes.FIREBALL_COOLDOWN));
        MCCourseMod.LOGGER.info("Unleashed Cooling Down? " + brain.hasMemoryModule(ModMemoryModuleTypes.UNLEASHED_SOULS_COOLDOWN));
        MCCourseMod.LOGGER.info("Cann't Attack? " + brain.getOptionalMemory(ModMemoryModuleTypes.ATTACK_ON_COOLDOWN).get());
    }


    public static boolean isNearPlayer(PorcupineEntity porcupine) {
        return porcupine.getBrain().hasMemoryModule(MemoryModuleType.PACIFIED);
    }

    private static Optional<? extends LivingEntity> getNearestVisibleTargetablePlayer(PorcupineEntity porcupine) {
        if (isNearPlayer(porcupine)) {
            return Optional.empty();
        }
        return porcupine.getBrain().getOptionalRegisteredMemory(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER);
    }

    public static void onAttacked(PorcupineEntity porcupine, LivingEntity attacker) {
        Brain<PorcupineEntity> brain = porcupine.getBrain();
        brain.forget(MemoryModuleType.PACIFIED);
        brain.forget(MemoryModuleType.BREED_TARGET);
        if (porcupine.isBaby()) {
            return;
        }
        targetEnemy(porcupine, attacker);
    }

    public static void onAttacking(PorcupineEntity porcupine, LivingEntity target) {
        if (porcupine.isBaby()) {
            return;
        }
    }


    private static void setAttackTarget(PorcupineEntity porcupine, LivingEntity target) {
        Brain<PorcupineEntity> brain = porcupine.getBrain();
        brain.forget(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        brain.forget(MemoryModuleType.BREED_TARGET);
        brain.remember(MemoryModuleType.ATTACK_TARGET, target, 200L);
    }

    private static void targetEnemy(PorcupineEntity porcupine, LivingEntity target) {
        if (porcupine.getBrain().hasActivity(Activity.AVOID) && target.getType() == EntityType.PIGLIN) {
            return;
        }
        if (target.getType() == ModEntities.PORCUPINE) {
            return;
        }
        if (LookTargetUtil.isNewTargetTooFar(porcupine, target, 4.0)) {
            return;
        }
        if (!Sensor.testAttackableTargetPredicate(porcupine, target)) {
            return;
        }
        setAttackTarget(porcupine, target);
    }
}