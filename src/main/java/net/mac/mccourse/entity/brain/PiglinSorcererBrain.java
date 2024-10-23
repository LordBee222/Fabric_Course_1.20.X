package net.mac.mccourse.entity.brain;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.mac.mccourse.entity.custom.PiglinSorcererEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PiglinSorcererBrain {
    /*
    public static final int field_30565 = 8;
    public static final int field_30566 = 4;
    public static final Item BARTERING_ITEM = Items.GOLD_INGOT;
    private static final int field_30567 = 16;
    private static final int field_30568 = 600;
    private static final int field_30569 = 120;
    private static final int field_30570 = 9;
    private static final int field_30571 = 200;
    private static final int field_30572 = 200;
    private static final int field_30573 = 300;
    public static final UniformIntProvider HUNT_MEMORY_DURATION = TimeHelper.betweenSeconds(30, 120);
    private static final int AVOID_TARGET_EXPIRY = 100;
    private static final int ADMIRING_DISABLED_EXPIRY = 400;
    private static final int field_30576 = 8;
    private static final UniformIntProvider MEMORY_TRANSFER_TASK_DURATION = TimeHelper.betweenSeconds(10, 40);
    private static final UniformIntProvider RIDE_TARGET_MEMORY_DURATION = TimeHelper.betweenSeconds(10, 30);
    private static final UniformIntProvider AVOID_MEMORY_DURATION = TimeHelper.betweenSeconds(5, 20);
    private static final int field_30577 = 20;
    private static final int field_30578 = 200;
    private static final int field_30579 = 12;
    private static final int field_30580 = 8;
    private static final int field_30581 = 14;
    private static final int field_30582 = 8;
    private static final int field_30583 = 5;
    private static final float CROSSBOW_ATTACK_FORWARD_MOVEMENT = 0.75f;
    private static final int field_30585 = 6;
    private static final UniformIntProvider GO_TO_ZOMBIFIED_MEMORY_DURATION = TimeHelper.betweenSeconds(5, 7);
    private static final UniformIntProvider GO_TO_NEMESIS_MEMORY_DURATION = TimeHelper.betweenSeconds(5, 7);
    private static final float field_30557 = 0.1f;
    private static final float field_30558 = 1.0f;
    private static final float field_30559 = 1.0f;
    private static final float START_RIDING_SPEED = 0.8f;
    private static final float field_30561 = 1.0f;
    private static final float field_30562 = 1.0f;
    private static final float field_30563 = 0.6f;
    private static final float field_30564 = 0.6f;

    public static Brain<?> create(PiglinSorcererEntity piglin, Brain<PiglinSorcererEntity> brain) {
        PiglinSorcererBrain.addCoreActivities(brain);
        PiglinSorcererBrain.addIdleActivities(brain);
        PiglinSorcererBrain.addAdmireItemActivities(brain);
        PiglinSorcererBrain.addFightActivities(piglin, brain);
        PiglinSorcererBrain.addCelebrateActivities(brain);
        PiglinSorcererBrain.addAvoidActivities(brain);
        PiglinSorcererBrain.addRideActivities(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.resetPossibleActivities();
        return brain;
    }

    public static void setHuntedRecently(PiglinSorcererEntity piglin, Random random) {
        int i = HUNT_MEMORY_DURATION.get(random);
        piglin.getBrain().remember(MemoryModuleType.HUNTED_RECENTLY, true, i);
    }

    private static void addCoreActivities(Brain<PiglinSorcererEntity> piglin) {

        /*piglin.setTaskList(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new LookAroundTask(45, 90),
                        new WanderAroundTask(),
                        OpenDoorsTask.create(),
                        PiglinSorcererBrain.goToNemesisTask(),
                        PiglinSorcererBrain.makeGoToZombifiedPiglinTask(),
                        RemoveOffHandItemTask.create(),
                        AdmireItemTask.create(120),
                        DefeatTargetTask.create(300, PiglinSorcererBrain::isHuntingTarget),
                        ForgetAngryAtTargetTask.create()));


    }

    private static void addIdleActivities(Brain<PiglinSorcererEntity> piglin) {
        piglin.setTaskList(Activity.IDLE, 10, ImmutableList.of(LookAtMobTask.create(PiglinSorcererBrain::isGoldHoldingPlayer, 14.0f), UpdateAttackTargetTask.create(AbstractPiglinEntity::isAdult, PiglinSorcererBrain::getPreferredTarget), TaskTriggerer.runIf(PiglinSorcererEntity::canHunt, HuntHoglinTask.create()), PiglinSorcererBrain.makeGoToSoulFireTask(), PiglinSorcererBrain.makeRememberRideableHoglinTask(), PiglinSorcererBrain.makeRandomFollowTask(), PiglinSorcererBrain.makeRandomWanderTask(), FindInteractionTargetTask.create(EntityType.PLAYER, 4)));
    }

    private static void addFightActivities(PiglinSorcererEntity piglin, Brain<PiglinSorcererEntity> brain) {
        brain.setTaskList(
                Activity.FIGHT,
                10,
                (ImmutableList<? extends Task<? super PiglinSorcererEntity>>) ImmutableList.of(
                        ForgetAttackTargetTask.create(target -> !PiglinSorcererBrain.isPreferredAttackTarget(piglin, target)),
                        TaskTriggerer.runIf(PiglinSorcererBrain::isHoldingCrossbow, AttackTask.create(5, 0.75f)),
                        RangedApproachTask.create(1.0f), MeleeAttackTask.create(20),
                        new CrossbowAttackTask(), HuntFinishTask.create(),
                        ForgetTask.create(PiglinSorcererBrain::getNearestZombifiedPiglin, MemoryModuleType.ATTACK_TARGET)),
                MemoryModuleType.ATTACK_TARGET);
    }

    private static void addCelebrateActivities(Brain<PiglinSorcererEntity> brain) {
        brain.setTaskList(Activity.CELEBRATE, 10, (ImmutableList<? extends Task<? super PiglinSorcererEntity>>) ImmutableList.of(PiglinSorcererBrain.makeGoToSoulFireTask(), LookAtMobTask.create(PiglinSorcererBrain::isGoldHoldingPlayer, 14.0f), UpdateAttackTargetTask.create(AbstractPiglinEntity::isAdult, PiglinSorcererBrain::getPreferredTarget), TaskTriggerer.runIf(piglin -> !piglin.isDancing(), WalkTowardsPosTask.create(MemoryModuleType.CELEBRATE_LOCATION, 2, 1.0f)), TaskTriggerer.runIf(PiglinSorcererEntity::isDancing, WalkTowardsPosTask.create(MemoryModuleType.CELEBRATE_LOCATION, 4, 0.6f)), new RandomTask(ImmutableList.of(Pair.of(LookAtMobTask.create(EntityType.PIGLIN, 8.0f), 1), Pair.of(StrollTask.create(0.6f, 2, 1), 1), Pair.of(new WaitTask(10, 20), 1)))), MemoryModuleType.CELEBRATE_LOCATION);
    }

    private static void addAdmireItemActivities(Brain<PiglinSorcererEntity> brain) {
        brain.setTaskList(Activity.ADMIRE_ITEM, 10, (ImmutableList<? extends Task<? super PiglinSorcererEntity>>) ImmutableList.of(WalkToNearestVisibleWantedItemTask.create(PiglinSorcererBrain::doesNotHaveGoldInOffHand, 1.0f, true, 9), WantNewItemTask.create(9), AdmireItemTimeLimitTask.create(200, 200)), MemoryModuleType.ADMIRING_ITEM);
    }

    private static void addAvoidActivities(Brain<PiglinSorcererEntity> brain) {
        brain.setTaskList(Activity.AVOID, 10, ImmutableList.of(GoToRememberedPositionTask.createEntityBased(MemoryModuleType.AVOID_TARGET, 1.0f, 12, true), PiglinSorcererBrain.makeRandomFollowTask(), PiglinSorcererBrain.makeRandomWanderTask(), ForgetTask.create(PiglinSorcererBrain::shouldRunAwayFromHoglins, MemoryModuleType.AVOID_TARGET)), MemoryModuleType.AVOID_TARGET);
    }

    private static void addRideActivities(Brain<PiglinSorcererEntity> brain) {
        brain.setTaskList(Activity.RIDE, 10, (ImmutableList<? extends Task<? super PiglinSorcererEntity>>) ImmutableList.of(StartRidingTask.create(0.8f), LookAtMobTask.create(PiglinSorcererBrain::isGoldHoldingPlayer, 8.0f), TaskTriggerer.runIf(TaskTriggerer.predicate(Entity::hasVehicle), Tasks.pickRandomly(((ImmutableList.Builder) ((ImmutableList.Builder) ImmutableList.builder().addAll(PiglinSorcererBrain.makeFollowTasks())).add(Pair.of(TaskTriggerer.predicate(PiglinSorcererEntity -> true), 1))).build())), RidingTask.create(8, PiglinSorcererBrain::canRide)), MemoryModuleType.RIDE_TARGET);
    }

    private static ImmutableList<Pair<SingleTickTask<LivingEntity>, Integer>> makeFollowTasks() {
        return ImmutableList.of(Pair.of(LookAtMobTask.create(EntityType.PLAYER, 8.0f), 1), Pair.of(LookAtMobTask.create(EntityType.PIGLIN, 8.0f), 1), Pair.of(LookAtMobTask.create(8.0f), 1));
    }

    private static RandomTask<LivingEntity> makeRandomFollowTask() {
        return new RandomTask<LivingEntity>((List<Pair<Task<LivingEntity>, Integer>>) ((Object) ((ImmutableList.Builder) ((ImmutableList.Builder) ImmutableList.builder().addAll(PiglinSorcererBrain.makeFollowTasks())).add(Pair.of(new WaitTask(30, 60), 1))).build()));
    }

    private static RandomTask<PiglinSorcererEntity> makeRandomWanderTask() {
        return new RandomTask<PiglinSorcererEntity>(ImmutableList.of(Pair.of(StrollTask.create(0.6f), 2), Pair.of(FindEntityTask.create(EntityType.PIGLIN, 8, MemoryModuleType.INTERACTION_TARGET, 0.6f, 2), 2), Pair.of(TaskTriggerer.runIf(PiglinSorcererBrain::canWander, GoTowardsLookTargetTask.create(0.6f, 3)), 2), Pair.of(new WaitTask(30, 60), 1)));
    }

    private static Task<PathAwareEntity> makeGoToSoulFireTask() {
        return GoToRememberedPositionTask.createPosBased(MemoryModuleType.NEAREST_REPELLENT, 1.0f, 8, false);
    }

    private static Task<PiglinSorcererEntity> goToNemesisTask() {
        return MemoryTransferTask.create(PiglinSorcererEntity::isBaby, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.AVOID_TARGET, GO_TO_NEMESIS_MEMORY_DURATION);
    }

    private static Task<PiglinSorcererEntity> makeGoToZombifiedPiglinTask() {
        return MemoryTransferTask.create(PiglinSorcererBrain::getNearestZombifiedPiglin, MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, MemoryModuleType.AVOID_TARGET, GO_TO_ZOMBIFIED_MEMORY_DURATION);
    }

    public static void tickActivities(PiglinSorcererEntity piglin) {
        Brain<PiglinSorcererEntity> brain = piglin.getBrain();
        Activity activity = brain.getFirstPossibleNonCoreActivity().orElse(null);
        brain.resetPossibleActivities(ImmutableList.of(Activity.ADMIRE_ITEM, Activity.FIGHT, Activity.AVOID, Activity.CELEBRATE, Activity.RIDE, Activity.IDLE));
        Activity activity2 = brain.getFirstPossibleNonCoreActivity().orElse(null);
        if (activity != activity2) {
            PiglinSorcererBrain.getCurrentActivitySound(piglin).ifPresent(piglin::playSound);
        }
        piglin.setAttacking(brain.hasMemoryModule(MemoryModuleType.ATTACK_TARGET));
        if (!brain.hasMemoryModule(MemoryModuleType.RIDE_TARGET) && PiglinSorcererBrain.canRideHoglin(piglin)) {
            piglin.stopRiding();
        }
        if (!brain.hasMemoryModule(MemoryModuleType.CELEBRATE_LOCATION)) {
            brain.forget(MemoryModuleType.DANCING);
        }
        piglin.setDancing(brain.hasMemoryModule(MemoryModuleType.DANCING));
    }

    private static boolean canRideHoglin(PiglinSorcererEntity piglin) {
        if (!piglin.isBaby()) {
            return false;
        }
        Entity entity = piglin.getVehicle();
        return entity instanceof PiglinSorcererEntity && ((PiglinSorcererEntity) entity).isBaby() || entity instanceof HoglinEntity && ((HoglinEntity) entity).isBaby();
    }

    public static void loot(PiglinSorcererEntity piglin, ItemEntity drop) {
        boolean bl;
        ItemStack itemStack;
        PiglinSorcererBrain.stopWalking(piglin);
        if (drop.getStack().isOf(Items.GOLD_NUGGET)) {
            piglin.sendPickup(drop, drop.getStack().getCount());
            itemStack = drop.getStack();
            drop.discard();
        } else {
            piglin.sendPickup(drop, 1);
            itemStack = PiglinSorcererBrain.getItemFromStack(drop);
        }
        if (PiglinSorcererBrain.isGoldenItem(itemStack)) {
            piglin.getBrain().forget(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
            PiglinSorcererBrain.swapItemWithOffHand(piglin, itemStack);
            PiglinSorcererBrain.setAdmiringItem(piglin);
            return;
        }
        if (PiglinSorcererBrain.isFood(itemStack) && !PiglinSorcererBrain.hasAteRecently(piglin)) {
            PiglinSorcererBrain.setEatenRecently(piglin);
            return;
        }
        boolean bl2 = bl = !piglin.tryEquip(itemStack).equals(ItemStack.EMPTY);
        if (bl) {
            return;
        }
        PiglinSorcererBrain.barterItem(piglin, itemStack);
    }

    private static void swapItemWithOffHand(PiglinSorcererEntity piglin, ItemStack stack) {
        if (PiglinSorcererBrain.hasItemInOffHand(piglin)) {
            piglin.dropStack(piglin.getStackInHand(Hand.OFF_HAND));
        }
        piglin.equipToOffHand(stack);
    }

    private static ItemStack getItemFromStack(ItemEntity stack) {
        ItemStack itemStack = stack.getStack();
        ItemStack itemStack2 = itemStack.split(1);
        if (itemStack.isEmpty()) {
            stack.discard();
        } else {
            stack.setStack(itemStack);
        }
        return itemStack2;
    }

    public static void consumeOffHandItem(PiglinSorcererEntity piglin, boolean barter) {
        ItemStack itemStack = piglin.getStackInHand(Hand.OFF_HAND);
        piglin.setStackInHand(Hand.OFF_HAND, ItemStack.EMPTY);
        if (piglin.isAdult()) {
            boolean bl = PiglinSorcererBrain.acceptsForBarter(itemStack);
            if (barter && bl) {
                PiglinSorcererBrain.doBarter(piglin, PiglinSorcererBrain.getBarteredItem(piglin));
            } else if (!bl) {
                boolean bl2;
                boolean bl3 = bl2 = !piglin.tryEquip(itemStack).isEmpty();
                if (!bl2) {
                    PiglinSorcererBrain.barterItem(piglin, itemStack);
                }
            }
        } else {
            boolean bl;
            boolean bl4 = bl = !piglin.tryEquip(itemStack).isEmpty();
            if (!bl) {
                ItemStack itemStack2 = piglin.getMainHandStack();
                if (PiglinSorcererBrain.isGoldenItem(itemStack2)) {
                    PiglinSorcererBrain.barterItem(piglin, itemStack2);
                } else {
                    PiglinSorcererBrain.doBarter(piglin, Collections.singletonList(itemStack2));
                }
                piglin.equipToMainHand(itemStack);
            }
        }
    }

    public static void pickupItemWithOffHand(PiglinSorcererEntity piglin) {
        if (PiglinSorcererBrain.isAdmiringItem(piglin) && !piglin.getOffHandStack().isEmpty()) {
            piglin.dropStack(piglin.getOffHandStack());
            piglin.setStackInHand(Hand.OFF_HAND, ItemStack.EMPTY);
        }
    }

    private static void barterItem(PiglinSorcererEntity piglin, ItemStack stack) {
        ItemStack itemStack = piglin.addItem(stack);
        PiglinSorcererBrain.dropBarteredItem(piglin, Collections.singletonList(itemStack));
    }

    private static void doBarter(PiglinSorcererEntity piglin, List<ItemStack> items) {
        Optional<PlayerEntity> optional = piglin.getBrain().getOptionalRegisteredMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER);
        if (optional.isPresent()) {
            PiglinSorcererBrain.dropBarteredItem(piglin, optional.get(), items);
        } else {
            PiglinSorcererBrain.dropBarteredItem(piglin, items);
        }
    }

    private static void dropBarteredItem(PiglinSorcererEntity piglin, List<ItemStack> items) {
        PiglinSorcererBrain.drop(piglin, items, PiglinSorcererBrain.findGround(piglin));
    }

    private static void dropBarteredItem(PiglinSorcererEntity piglin, PlayerEntity player, List<ItemStack> items) {
        PiglinSorcererBrain.drop(piglin, items, player.getPos());
    }

    private static void drop(PiglinSorcererEntity piglin, List<ItemStack> items, Vec3d pos) {
        if (!items.isEmpty()) {
            piglin.swingHand(Hand.OFF_HAND);
            for (ItemStack itemStack : items) {
                LookTargetUtil.give(piglin, itemStack, pos.add(0.0, 1.0, 0.0));
            }
        }
    }

    private static List<ItemStack> getBarteredItem(PiglinSorcererEntity piglin) {
        LootTable lootTable = piglin.getWorld().getServer().getLootManager().getLootTable(LootTables.PIGLIN_BARTERING_GAMEPLAY);
        ObjectArrayList<ItemStack> list = lootTable.generateLoot(new LootContextParameterSet.Builder((ServerWorld) piglin.getWorld()).add(LootContextParameters.THIS_ENTITY, piglin).build(LootContextTypes.BARTER));
        return list;
    }

    private static boolean isHuntingTarget(LivingEntity piglin, LivingEntity target) {
        if (target.getType() != EntityType.HOGLIN) {
            return false;
        }
        return Random.create(piglin.getWorld().getTime()).nextFloat() < 0.1f;
    }

    public static boolean canGather(PiglinSorcererEntity piglin, ItemStack stack) {
        if (piglin.isBaby() && stack.isIn(ItemTags.IGNORED_BY_PIGLIN_BABIES)) {
            return false;
        }
        if (stack.isIn(ItemTags.PIGLIN_REPELLENTS)) {
            return false;
        }
        if (PiglinSorcererBrain.hasBeenHitByPlayer(piglin) && piglin.getBrain().hasMemoryModule(MemoryModuleType.ATTACK_TARGET)) {
            return false;
        }
        if (PiglinSorcererBrain.acceptsForBarter(stack)) {
            return PiglinSorcererBrain.doesNotHaveGoldInOffHand(piglin);
        }
        boolean bl = piglin.canInsertIntoInventory(stack);
        if (stack.isOf(Items.GOLD_NUGGET)) {
            return bl;
        }
        if (PiglinSorcererBrain.isFood(stack)) {
            return !PiglinSorcererBrain.hasAteRecently(piglin) && bl;
        }
        if (PiglinSorcererBrain.isGoldenItem(stack)) {
            return PiglinSorcererBrain.doesNotHaveGoldInOffHand(piglin) && bl;
        }
        return piglin.canEquipStack(stack);
    }

    public static boolean isGoldenItem(ItemStack stack) {
        return stack.isIn(ItemTags.PIGLIN_LOVED);
    }

    private static boolean canRide(PiglinSorcererEntity piglin, Entity ridden) {
        if (ridden instanceof MobEntity) {
            MobEntity mobEntity = (MobEntity) ridden;
            return !mobEntity.isBaby() || !mobEntity.isAlive() || PiglinSorcererBrain.hasBeenHurt(piglin) || PiglinSorcererBrain.hasBeenHurt(mobEntity) || mobEntity instanceof PiglinSorcererEntity && mobEntity.getVehicle() == null;
        }
        return false;
    }

    private static boolean isPreferredAttackTarget(PiglinSorcererEntity piglin, LivingEntity target) {
        return PiglinSorcererBrain.getPreferredTarget(piglin).filter(preferredTarget -> preferredTarget == target).isPresent();
    }

    private static boolean getNearestZombifiedPiglin(PiglinSorcererEntity piglin) {
        Brain<PiglinSorcererEntity> brain = piglin.getBrain();
        if (brain.hasMemoryModule(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED)) {
            LivingEntity livingEntity = brain.getOptionalRegisteredMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED).get();
            return piglin.isInRange(livingEntity, 6.0);
        }
        return false;
    }

    private static Optional<? extends LivingEntity> getPreferredTarget(PiglinSorcererEntity piglin) {
        Optional<MobEntity> optional2;
        Brain<PiglinSorcererEntity> brain = piglin.getBrain();
        if (PiglinSorcererBrain.getNearestZombifiedPiglin(piglin)) {
            return Optional.empty();
        }
        Optional<LivingEntity> optional = LookTargetUtil.getEntity(piglin, MemoryModuleType.ANGRY_AT);
        if (optional.isPresent() && Sensor.testAttackableTargetPredicateIgnoreVisibility(piglin, optional.get())) {
            return optional;
        }
        if (brain.hasMemoryModule(MemoryModuleType.UNIVERSAL_ANGER) && (optional2 = brain.getOptionalRegisteredMemory(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER)).isPresent()) {
            return optional2;
        }
        optional2 = brain.getOptionalRegisteredMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
        if (optional2.isPresent()) {
            return optional2;
        }
        Optional<PlayerEntity> optional3 = brain.getOptionalRegisteredMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD);
        if (optional3.isPresent() && Sensor.testAttackableTargetPredicate(piglin, optional3.get())) {
            return optional3;
        }
        return Optional.empty();
    }

    public static void onGuardedBlockInteracted(PlayerEntity player, boolean blockOpen) {
        List<PiglinSorcererEntity> list = player.getWorld().getNonSpectatingEntities(PiglinSorcererEntity.class, player.getBoundingBox().expand(16.0));
        list.stream().filter(PiglinSorcererBrain::hasIdleActivity).filter(piglin -> !blockOpen || LookTargetUtil.isVisibleInMemory(piglin, player)).forEach(piglin -> {
            if (piglin.getWorld().getGameRules().getBoolean(GameRules.UNIVERSAL_ANGER)) {
                PiglinSorcererBrain.becomeAngryWithPlayer(piglin, player);
            } else {
                PiglinSorcererBrain.becomeAngryWith(piglin, player);
            }
        });
    }

    public static ActionResult playerInteract(PiglinSorcererEntity piglin, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (PiglinSorcererBrain.isWillingToTrade(piglin, itemStack)) {
            ItemStack itemStack2 = itemStack.split(1);
            PiglinSorcererBrain.swapItemWithOffHand(piglin, itemStack2);
            PiglinSorcererBrain.setAdmiringItem(piglin);
            PiglinSorcererBrain.stopWalking(piglin);
            return ActionResult.CONSUME;
        }
        return ActionResult.PASS;
    }

    public static boolean isWillingToTrade(PiglinSorcererEntity piglin, ItemStack nearbyItems) {
        return !PiglinSorcererBrain.hasBeenHitByPlayer(piglin) && !PiglinSorcererBrain.isAdmiringItem(piglin) && piglin.isAdult() && PiglinSorcererBrain.acceptsForBarter(nearbyItems);
    }

    public static void onAttacked(PiglinSorcererEntity piglin, LivingEntity attacker) {
        if (attacker instanceof PiglinSorcererEntity) {
            return;
        }
        if (PiglinSorcererBrain.hasItemInOffHand(piglin)) {
            PiglinSorcererBrain.consumeOffHandItem(piglin, false);
        }
        Brain<PiglinSorcererEntity> brain = piglin.getBrain();
        brain.forget(MemoryModuleType.CELEBRATE_LOCATION);
        brain.forget(MemoryModuleType.DANCING);
        brain.forget(MemoryModuleType.ADMIRING_ITEM);
        if (attacker instanceof PlayerEntity) {
            brain.remember(MemoryModuleType.ADMIRING_DISABLED, true, 400L);
        }
        PiglinSorcererBrain.getAvoiding(piglin).ifPresent(avoiding -> {
            if (avoiding.getType() != attacker.getType()) {
                brain.forget(MemoryModuleType.AVOID_TARGET);
            }
        });
        if (piglin.isBaby()) {
            brain.remember(MemoryModuleType.AVOID_TARGET, attacker, 100L);
            if (Sensor.testAttackableTargetPredicateIgnoreVisibility(piglin, attacker)) {
                PiglinSorcererBrain.angerAtCloserTargets(piglin, attacker);
            }
            return;
        }
        if (attacker.getType() == EntityType.HOGLIN && PiglinSorcererBrain.hasOutnumberedHoglins(piglin)) {
            PiglinSorcererBrain.runAwayFrom(piglin, attacker);
            PiglinSorcererBrain.groupRunAwayFrom(piglin, attacker);
            return;
        }
        PiglinSorcererBrain.tryRevenge(piglin, attacker);
    }

    public static void tryRevenge(AbstractPiglinEntity piglin, LivingEntity target) {
        if (piglin.getBrain().hasActivity(Activity.AVOID)) {
            return;
        }
        if (!Sensor.testAttackableTargetPredicateIgnoreVisibility(piglin, target)) {
            return;
        }
        if (LookTargetUtil.isNewTargetTooFar(piglin, target, 4.0)) {
            return;
        }
        if (target.getType() == EntityType.PLAYER && piglin.getWorld().getGameRules().getBoolean(GameRules.UNIVERSAL_ANGER)) {
            PiglinSorcererBrain.becomeAngryWithPlayer(piglin, target);
            PiglinSorcererBrain.angerNearbyPiglins(piglin);
        } else {
            PiglinSorcererBrain.becomeAngryWith(piglin, target);
            PiglinSorcererBrain.angerAtCloserTargets(piglin, target);
        }
    }

    public static Optional<SoundEvent> getCurrentActivitySound(PiglinSorcererEntity piglin) {
        return piglin.getBrain().getFirstPossibleNonCoreActivity().map(activity -> PiglinSorcererBrain.getSound(piglin, activity));
    }

    private static SoundEvent getSound(PiglinSorcererEntity piglin, Activity activity) {
        if (activity == Activity.FIGHT) {
            return SoundEvents.ENTITY_PIGLIN_ANGRY;
        }
        if (piglin.shouldZombify()) {
            return SoundEvents.ENTITY_PIGLIN_RETREAT;
        }
        if (activity == Activity.AVOID && PiglinSorcererBrain.hasTargetToAvoid(piglin)) {
            return SoundEvents.ENTITY_PIGLIN_RETREAT;
        }
        if (activity == Activity.ADMIRE_ITEM) {
            return SoundEvents.ENTITY_PIGLIN_ADMIRING_ITEM;
        }
        if (activity == Activity.CELEBRATE) {
            return SoundEvents.ENTITY_PIGLIN_CELEBRATE;
        }
        if (PiglinSorcererBrain.hasPlayerHoldingWantedItemNearby(piglin)) {
            return SoundEvents.ENTITY_PIGLIN_JEALOUS;
        }
        if (PiglinSorcererBrain.hasSoulFireNearby(piglin)) {
            return SoundEvents.ENTITY_PIGLIN_RETREAT;
        }
        return SoundEvents.ENTITY_PIGLIN_AMBIENT;
    }

    private static boolean hasTargetToAvoid(PiglinSorcererEntity piglin) {
        Brain<PiglinSorcererEntity> brain = piglin.getBrain();
        if (!brain.hasMemoryModule(MemoryModuleType.AVOID_TARGET)) {
            return false;
        }
        return brain.getOptionalRegisteredMemory(MemoryModuleType.AVOID_TARGET).get().isInRange(piglin, 12.0);
    }

    public static List<AbstractPiglinEntity> getNearbyVisiblePiglins(PiglinSorcererEntity piglin) {
        return piglin.getBrain().getOptionalRegisteredMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS).orElse(ImmutableList.of());
    }

    private static List<AbstractPiglinEntity> getNearbyPiglins(AbstractPiglinEntity piglin) {
        return piglin.getBrain().getOptionalRegisteredMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS).orElse(ImmutableList.of());
    }

    public static boolean wearsGoldArmor(LivingEntity entity) {
        Iterable<ItemStack> iterable = entity.getArmorItems();
        for (ItemStack itemStack : iterable) {
            Item item = itemStack.getItem();
            if (!(item instanceof ArmorItem) || ((ArmorItem) item).getMaterial() != ArmorMaterials.GOLD) continue;
            return true;
        }
        return false;
    }

    private static void stopWalking(PiglinSorcererEntity piglin) {
        piglin.getBrain().forget(MemoryModuleType.WALK_TARGET);
        piglin.getNavigation().stop();
    }

    private static Task<LivingEntity> makeRememberRideableHoglinTask() {
        LookAtMobWithIntervalTask.Interval interval = new LookAtMobWithIntervalTask.Interval(MEMORY_TRANSFER_TASK_DURATION);
        return MemoryTransferTask.create(entity -> entity.isBaby() && interval.shouldRun(entity.getWorld().random), MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, MemoryModuleType.RIDE_TARGET, RIDE_TARGET_MEMORY_DURATION);
    }

    public static void angerAtCloserTargets(AbstractPiglinEntity piglin, LivingEntity target) {
        PiglinSorcererBrain.getNearbyPiglins(piglin).forEach(nearbyPiglin -> {
            if (!(target.getType() != EntityType.HOGLIN || nearbyPiglin.canHunt() && ((HoglinEntity) target).canBeHunted())) {
                return;
            }
            PiglinSorcererBrain.angerAtIfCloser(nearbyPiglin, target);
        });
    }

    public static void angerNearbyPiglins(AbstractPiglinEntity piglin) {
        PiglinSorcererBrain.getNearbyPiglins(piglin).forEach(nearbyPiglin -> PiglinSorcererBrain.getNearestDetectedPlayer(nearbyPiglin).ifPresent(player -> PiglinSorcererBrain.becomeAngryWith(nearbyPiglin, player)));
    }

    public static void becomeAngryWith(AbstractPiglinEntity piglin, LivingEntity target) {
        if (!Sensor.testAttackableTargetPredicateIgnoreVisibility(piglin, target)) {
            return;
        }
        piglin.getBrain().forget(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        piglin.getBrain().remember(MemoryModuleType.ANGRY_AT, target.getUuid(), 600L);
        if (target.getType() == EntityType.HOGLIN && piglin.canHunt()) {
            PiglinSorcererBrain.rememberHunting(piglin);
        }
        if (target.getType() == EntityType.PLAYER && piglin.getWorld().getGameRules().getBoolean(GameRules.UNIVERSAL_ANGER)) {
            piglin.getBrain().remember(MemoryModuleType.UNIVERSAL_ANGER, true, 600L);
        }
    }

    private static void becomeAngryWithPlayer(AbstractPiglinEntity piglin, LivingEntity player) {
        Optional<PlayerEntity> optional = PiglinSorcererBrain.getNearestDetectedPlayer(piglin);
        if (optional.isPresent()) {
            PiglinSorcererBrain.becomeAngryWith(piglin, optional.get());
        } else {
            PiglinSorcererBrain.becomeAngryWith(piglin, player);
        }
    }

    private static void angerAtIfCloser(AbstractPiglinEntity piglin, LivingEntity target) {
        Optional<LivingEntity> optional = PiglinSorcererBrain.getAngryAt(piglin);
        LivingEntity livingEntity = LookTargetUtil.getCloserEntity((LivingEntity) piglin, optional, target);
        if (optional.isPresent() && optional.get() == livingEntity) {
            return;
        }
        PiglinSorcererBrain.becomeAngryWith(piglin, livingEntity);
    }

    private static Optional<LivingEntity> getAngryAt(AbstractPiglinEntity piglin) {
        return LookTargetUtil.getEntity(piglin, MemoryModuleType.ANGRY_AT);
    }

    public static Optional<LivingEntity> getAvoiding(PiglinSorcererEntity piglin) {
        if (piglin.getBrain().hasMemoryModule(MemoryModuleType.AVOID_TARGET)) {
            return piglin.getBrain().getOptionalRegisteredMemory(MemoryModuleType.AVOID_TARGET);
        }
        return Optional.empty();
    }

    public static Optional<PlayerEntity> getNearestDetectedPlayer(AbstractPiglinEntity piglin) {
        if (piglin.getBrain().hasMemoryModule(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER)) {
            return piglin.getBrain().getOptionalRegisteredMemory(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER);
        }
        return Optional.empty();
    }

    private static void groupRunAwayFrom(PiglinSorcererEntity piglin2, LivingEntity target) {
        PiglinSorcererBrain.getNearbyVisiblePiglins(piglin2).stream().filter(nearbyVisiblePiglin -> nearbyVisiblePiglin instanceof PiglinEntity).forEach(piglin -> PiglinSorcererBrain.runAwayFromClosestTarget((PiglinEntity) piglin, target));
    }

    private static void runAwayFromClosestTarget(PiglinSorcererEntity piglin, LivingEntity target) {
        Brain<PiglinSorcererEntity> brain = piglin.getBrain();
        LivingEntity livingEntity = target;
        livingEntity = LookTargetUtil.getCloserEntity((LivingEntity) piglin, brain.getOptionalRegisteredMemory(MemoryModuleType.AVOID_TARGET), livingEntity);
        livingEntity = LookTargetUtil.getCloserEntity((LivingEntity) piglin, brain.getOptionalRegisteredMemory(MemoryModuleType.ATTACK_TARGET), livingEntity);
        PiglinSorcererBrain.runAwayFrom(piglin, livingEntity);
    }

    private static boolean shouldRunAwayFromHoglins(PiglinSorcererEntity piglin) {
        Brain<PiglinSorcererEntity> brain = piglin.getBrain();
        if (!brain.hasMemoryModule(MemoryModuleType.AVOID_TARGET)) {
            return true;
        }
        LivingEntity livingEntity = brain.getOptionalRegisteredMemory(MemoryModuleType.AVOID_TARGET).get();
        EntityType<?> entityType = livingEntity.getType();
        if (entityType == EntityType.HOGLIN) {
            return PiglinSorcererBrain.hasNoAdvantageAgainstHoglins(piglin);
        }
        if (PiglinSorcererBrain.isZombified(entityType)) {
            return !brain.hasMemoryModuleWithValue(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, livingEntity);
        }
        return false;
    }

    private static boolean hasNoAdvantageAgainstHoglins(PiglinSorcererEntity piglin) {
        return !PiglinSorcererBrain.hasOutnumberedHoglins(piglin);
    }

    private static boolean hasOutnumberedHoglins(PiglinSorcererEntity piglins) {
        int i = piglins.getBrain().getOptionalRegisteredMemory(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT).orElse(0) + 1;
        int j = piglins.getBrain().getOptionalRegisteredMemory(MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT).orElse(0);
        return j > i;
    }

    private static void runAwayFrom(PiglinSorcererEntity piglin, LivingEntity target) {
        piglin.getBrain().forget(MemoryModuleType.ANGRY_AT);
        piglin.getBrain().forget(MemoryModuleType.ATTACK_TARGET);
        piglin.getBrain().forget(MemoryModuleType.WALK_TARGET);
        piglin.getBrain().remember(MemoryModuleType.AVOID_TARGET, target, AVOID_MEMORY_DURATION.get(piglin.getWorld().random));
        PiglinSorcererBrain.rememberHunting(piglin);
    }

    public static void rememberHunting(AbstractPiglinEntity piglin) {
        piglin.getBrain().remember(MemoryModuleType.HUNTED_RECENTLY, true, HUNT_MEMORY_DURATION.get(piglin.getWorld().random));
    }

    private static void setEatenRecently(PiglinSorcererEntity piglin) {
        piglin.getBrain().remember(MemoryModuleType.ATE_RECENTLY, true, 200L);
    }

    private static Vec3d findGround(PiglinSorcererEntity piglin) {
        Vec3d vec3d = FuzzyTargeting.find(piglin, 4, 2);
        return vec3d == null ? piglin.getPos() : vec3d;
    }

    private static boolean hasAteRecently(PiglinSorcererEntity piglin) {
        return piglin.getBrain().hasMemoryModule(MemoryModuleType.ATE_RECENTLY);
    }

    public static boolean hasIdleActivity(AbstractPiglinEntity piglin) {
        return piglin.getBrain().hasActivity(Activity.IDLE);
    }

    private static boolean isHoldingCrossbow(LivingEntity piglin) {
        return piglin.isHolding(Items.CROSSBOW);
    }

    private static void setAdmiringItem(LivingEntity entity) {
        entity.getBrain().remember(MemoryModuleType.ADMIRING_ITEM, true, 120L);
    }

    private static boolean isAdmiringItem(PiglinSorcererEntity entity) {
        return entity.getBrain().hasMemoryModule(MemoryModuleType.ADMIRING_ITEM);
    }

    private static boolean acceptsForBarter(ItemStack stack) {
        return stack.isOf(BARTERING_ITEM);
    }

    private static boolean isFood(ItemStack stack) {
        return stack.isIn(ItemTags.PIGLIN_FOOD);
    }

    private static boolean hasSoulFireNearby(PiglinSorcererEntity piglin) {
        return piglin.getBrain().hasMemoryModule(MemoryModuleType.NEAREST_REPELLENT);
    }

    private static boolean hasPlayerHoldingWantedItemNearby(LivingEntity entity) {
        return entity.getBrain().hasMemoryModule(MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM);
    }

    private static boolean canWander(LivingEntity piglin) {
        return !PiglinSorcererBrain.hasPlayerHoldingWantedItemNearby(piglin);
    }

    public static boolean isGoldHoldingPlayer(LivingEntity target) {
        return target.getType() == EntityType.PLAYER && target.isHolding(PiglinSorcererBrain::isGoldenItem);
    }

    private static boolean hasBeenHitByPlayer(PiglinSorcererEntity piglin) {
        return piglin.getBrain().hasMemoryModule(MemoryModuleType.ADMIRING_DISABLED);
    }

    private static boolean hasBeenHurt(LivingEntity piglin) {
        return piglin.getBrain().hasMemoryModule(MemoryModuleType.HURT_BY);
    }

    private static boolean hasItemInOffHand(PiglinSorcererEntity piglin) {
        return !piglin.getOffHandStack().isEmpty();
    }

    private static boolean doesNotHaveGoldInOffHand(PiglinSorcererEntity piglin) {
        return piglin.getOffHandStack().isEmpty() || !PiglinSorcererBrain.isGoldenItem(piglin.getOffHandStack());
    }

    public static boolean isZombified(EntityType<?> entityType) {
        return entityType == EntityType.ZOMBIFIED_PIGLIN || entityType == EntityType.ZOGLIN;
    }
    */
}

