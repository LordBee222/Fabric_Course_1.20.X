package net.mac.mccourse.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.entity.ModEntities;
import net.mac.mccourse.entity.ai.Task.*;
import net.mac.mccourse.entity.custom.PorcupineEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HoglinBrain;
import net.minecraft.entity.mob.HoglinEntity;
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
        return brain;
    }

    private static void addCoreTasks(Brain<PorcupineEntity> brain) {
        brain.setTaskList(
                Activity.CORE,
                0,
                ImmutableList.of(
                       // new TrackOwnerAttackerTask(),
                        //new AttackWithOwnerTask(),
                        UpdateAttackTargetTask.create(PorcupineBrain::getOwnerAttackingEntity),
                        UpdateAttackTargetTask.create(PorcupineBrain::getEntityAttackingOwner),
                        // new BecomeEnragedTask(),
                        //new AvoidAttackTargetTask(1.0f, 5.0f, 3.0f),
                        new FollowOwnerTask(1.2f, 4.0f, 100.0f),
                        new LookAroundTask(45, 90),
                        new WanderAroundTask()));
    }

    private static void addIdleTasks(Brain<PorcupineEntity> brain) {
        brain.setTaskList(
                Activity.IDLE,
                10,
                ImmutableList.of(
                        GoToRememberedPositionTask.createPosBased(MemoryModuleType.NEAREST_REPELLENT, 1.0f, 8, true),
                        //UpdateAttackTargetTask.create(PorcupineBrain::getNearestVisibleTargetablePlayer),
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
                        new UnleashedSoulBlastTask(6, 1.5, 3.75, ModEntities.UNSTABLE_BLOCK),
                        new PossessionTask(),
                        new FireballTask(),
                        //RangedApproachTask.create(1.0f),
                        //TaskTriggerer.runIf(PorcupineEntity::isAdult, MeleeAttackTask.create(40)),
                        //TaskTriggerer.runIf(PassiveEntity::isBaby, MeleeAttackTask.create(15)),
                        ForgetAttackTargetTask.create()),
                MemoryModuleType.ATTACK_TARGET);
    }

    public static void onEnrage(PorcupineEntity porcupine){
            porcupine.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(10);
            porcupine.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_KNOCKBACK).setBaseValue(5);
            porcupine.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.5);
            porcupine.FireballCooldown = 50;
            porcupine.BlastCooldown = 75;
            porcupine.AttackDowntime = 5;
            porcupine.PossessionCooldown = 60;
            porcupine.CallOfTheDeadCooldown = 200;
    }

    private static RandomTask<PorcupineEntity> makeRandomWalkTask() {
        return new RandomTask<PorcupineEntity>(ImmutableList.of(Pair.of(StrollTask.create(0.4f), 2), Pair.of(GoTowardsLookTargetTask.create(0.4f, 3), 2), Pair.of(new WaitTask(30, 60), 1)));
    }

    public static void refreshActivities(PorcupineEntity porcupine) {
        Brain<PorcupineEntity> brain = (Brain<PorcupineEntity>) porcupine.getBrain();
        brain.resetPossibleActivities(ImmutableList.of(Activity.FIGHT, Activity.AVOID, Activity.IDLE));
        porcupine.setAttacking(brain.hasMemoryModule(MemoryModuleType.ATTACK_TARGET));
    }

    public static void postAttack(PorcupineEntity porcupine, MemoryModuleType<Boolean> attackToCooldown, int cooldown){
        Brain<PorcupineEntity> brain = porcupine.getBrain();
        brain.remember(ModMemoryModuleTypes.ATTACK_ON_COOLDOWN, true, porcupine.AttackDowntime);
        brain.remember(attackToCooldown, true, cooldown);
    }

    public static boolean isNearPlayer(PorcupineEntity porcupine) {
        return porcupine.getBrain().hasMemoryModule(MemoryModuleType.PACIFIED);
    }

    private static Optional<? extends LivingEntity> getNearestVisibleTargetablePlayer(PorcupineEntity porcupine) {
        if (isNearPlayer(porcupine)) {
            return Optional.empty();
        }
        return porcupine.getBrain().getOptionalRegisteredMemory(MemoryModuleType.NEAREST_ATTACKABLE);

        //return porcupine.getBrain().getOptionalRegisteredMemory(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER);
    }

    private static Optional<? extends LivingEntity> getOwnerAttackingEntity(PorcupineEntity porcupine) {
        if (porcupine.getOwner() == null) return Optional.empty();
        Optional<LivingEntity> ownerAttacking = Optional.ofNullable(porcupine.getOwner().getAttacking());
        if (ownerAttacking.isEmpty() || ownerAttacking.get() == null) return Optional.empty();
        if (!porcupine.canAttackWithOwner(ownerAttacking.get(), porcupine.getOwner())) return Optional.empty();
        if (ownerAttacking.get() == porcupine.getOwner() || ownerAttacking.get() == porcupine) return Optional.empty();
        return ownerAttacking;
    }

    private static Optional<? extends LivingEntity> getEntityAttackingOwner(PorcupineEntity porcupine) {
        if (porcupine.getOwner() == null || !porcupine.isTamed()) return Optional.empty();

        if (porcupine.getOwner().getAttacker() == null && !porcupine.canAttackWithOwner(porcupine.getOwner().getAttacker(), porcupine.getOwner())) return Optional.empty();
        if (porcupine.getOwner().getAttacker() == porcupine.getOwner() || porcupine.getOwner().getAttacker() == porcupine) return Optional.empty();
        return Optional.ofNullable(porcupine.getOwner().getAttacker());
    }

    public static void onAttacked(PorcupineEntity porcupine, LivingEntity attacker) {
        Brain<PorcupineEntity> brain = porcupine.getBrain();
        brain.forget(MemoryModuleType.PACIFIED);
        brain.forget(MemoryModuleType.BREED_TARGET);
        targetEnemy(porcupine, attacker);
    }


    private static void setAttackTarget(PorcupineEntity porcupine, LivingEntity target) {
        Brain<PorcupineEntity> brain = porcupine.getBrain();
        brain.forget(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        brain.forget(MemoryModuleType.BREED_TARGET);
        brain.remember(MemoryModuleType.ATTACK_TARGET, target, 200L);
//        brain.remember(MemoryModuleType.ATTACK_TARGET, target, 200L);

    }

    private static void targetEnemy(PorcupineEntity porcupine, LivingEntity target) {
        if (porcupine.getBrain().hasActivity(Activity.AVOID) && target.getType() == EntityType.PIGLIN) {
            return;
        }
        if (target.getType() == ModEntities.PORCUPINE) {
            return;
        }
        /*
        if (LookTargetUtil.isNewTargetTooFar(porcupine, target, 4.0)) {
            return;
        }*/
        if (!Sensor.testAttackableTargetPredicate(porcupine, target)) {
            return;
        }
        setAttackTarget(porcupine, target);
    }
}