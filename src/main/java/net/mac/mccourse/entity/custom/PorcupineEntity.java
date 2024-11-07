package net.mac.mccourse.entity.custom;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.entity.ModEntities;
import net.mac.mccourse.entity.ai.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

public class PorcupineEntity extends TameableEntity
        implements Hoglin {


    private static final TrackedData<Boolean> BABY = DataTracker.registerData(PorcupineEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> ATTACKING = DataTracker.registerData(PorcupineEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> ENRAGED = DataTracker.registerData(PorcupineEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private boolean damageLinkActive;
    private float damageRedirected;

    private float redirectedDamageTaken;
    private int movementCooldownTicks;




    public final AnimationState idleAnimationState = new AnimationState(), attackAnimationState = new AnimationState();
    public int idleAnimationTimeout = 0, attackAnimationTimeout = 0;

    public int FireballCooldown = 100;
    public int BlastCooldown = 150;
    public int PossessionCooldown = 120;
    public int CallOfTheDeadCooldown = 400;
    public int AttackDowntime = 10;


    public PorcupineEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    public static PorcupineEntity createWithOwner(World world, LivingEntity owner) {
        PorcupineEntity porcupine = new PorcupineEntity(ModEntities.PORCUPINE, world);
        porcupine.setOwner((PlayerEntity) owner);
        return porcupine;
    }

    public static DefaultAttributeContainer.Builder createPorcupineAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 50)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.6f)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0);
    }

    @Override
    public boolean tryAttack(Entity target) {
        if (!(target instanceof LivingEntity)) {
            return false;
        }
        this.movementCooldownTicks = 10;
        this.getWorld().sendEntityStatus(this, EntityStatuses.PLAY_ATTACK_SOUND);
        this.playSound(SoundEvents.ENTITY_HOGLIN_ATTACK, 1.0f, this.getSoundPitch());
        return Hoglin.tryAttack(this, (LivingEntity) target);
    }

    @Override
    protected void knockback(LivingEntity target) {
        if (this.isAdult()) {
            Hoglin.knockback(this, target);
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        boolean bl = super.damage(source, amount);
        if (this.getWorld().isClient) {
            return false;
        }
        if (bl && source.getAttacker() instanceof LivingEntity) {
            PorcupineBrain.onAttacked(this, (LivingEntity) source.getAttacker());
        }
        return bl;
    }

    public boolean hasAliveOwner() {
        return this.getOwner() != null && this.getOwner().isAlive();
    }

    @Override
    public boolean canAttackWithOwner(LivingEntity target, LivingEntity owner) {
        if (target instanceof PorcupineEntity) {
            PorcupineEntity porcupine = (PorcupineEntity)target;
            return porcupine.getOwner() != owner;
        }
        if (target instanceof PlayerEntity && owner instanceof PlayerEntity && !((PlayerEntity)owner).shouldDamagePlayer((PlayerEntity)target)) {
            return false;
        }
        return true;
      //  return !(target instanceof TameableEntity) || !((TameableEntity)target).isTamed();
    }

    public void queryDamageLinkStatus() {
        MCCourseMod.LOGGER.info("HAS INIT MANAGER");
        if (hasAliveOwner()) {
            LivingEntity owner = this.getOwner();
            if (this.distanceTo(owner) <= 30) {
                if (!this.isDamageLinkActive() && !this.getBrain().hasMemoryModule(ModMemoryModuleTypes.REDIRECT_DAMAGE_COOLDOWN))
                    this.setDamageLinkActive(true);
                if (this.isDamageLinkActive() && this.getBrain().hasMemoryModule(ModMemoryModuleTypes.REDIRECT_DAMAGE_COOLDOWN))
                    this.setDamageLinkActive(false);
            } else {
                this.setDamageLinkActive(false);
            }
        } else {
            this.setDamageLinkActive(false);
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return super.isInvulnerableTo(damageSource) ||
                damageSource.isOf(DamageTypes.EXPLOSION) ||
                damageSource.isOf(DamageTypes.PLAYER_EXPLOSION) ||
                damageSource.isOf(DamageTypes.FIREBALL) ||
                damageSource.isOf(DamageTypes.IN_FIRE) ||
                damageSource.isOf(DamageTypes.ON_FIRE) ||
                damageSource.isOf(DamageTypes.UNATTRIBUTED_FIREBALL) ||
                damageSource.isOf(DamageTypes.FALL);
    }

    protected Brain.Profile<PorcupineEntity> createBrainProfile() {
        return Brain.createProfile(PorcupineUtil.MEMORY_MODULE_TYPES, PorcupineUtil.SENSOR_TYPES);
    }

    @Override
    protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        return PorcupineBrain.create(this.createBrainProfile().deserialize(dynamic));
    }

    public Brain<PorcupineEntity> getBrain() {
        return (Brain<PorcupineEntity>) super.getBrain();
    }



    public boolean isDamageLinkActive() {
        return damageLinkActive;
    }

    public void setDamageLinkActive(boolean active) {
        this.damageLinkActive = active;
    }

    public float getDamageRedirected() {
        return damageRedirected;
    }

    public void setDamageRedirected(float damageRedirected) {
        this.damageRedirected = damageRedirected;
    }

    @Override
    protected void mobTick() {


        this.getWorld().getProfiler().push("porcupineBrain");
        this.getBrain().tick((ServerWorld) this.getWorld(), this);
        this.getWorld().getProfiler().pop();
        PorcupineBrain.refreshActivities(this);

        this.queryDamageLinkStatus();

        if (this.isDamageLinkActive()) {
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 20, 0));
            this.getOwner().addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 20, 0));
        }
        MCCourseMod.LOGGER.info("STATUS OF DAMAGE LINK: " + (this.isDamageLinkActive() ? "open" : "closed"));

        if (this.getBrain().getOptionalMemory(ModMemoryModuleTypes.REDIRECTED_DAMAGE_TAKEN).orElse(0f) >= 30f) {
            this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 10, false, World.ExplosionSourceType.TNT);
            this.playSound(SoundEvents.ENTITY_WITHER_HURT, 20f, 1.6f);
            this.getBrain().forget(ModMemoryModuleTypes.REDIRECTED_DAMAGE_TAKEN);
        }
    }


    @Override
    public void tickMovement() {
        if (this.movementCooldownTicks > 0) {
            --this.movementCooldownTicks;
        }
        super.tickMovement();
    }

    @Override
    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        if (world.getRandom().nextFloat() < 0.2f) {
            this.setBaby(true);
        }
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return !this.isPersistent();
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ActionResult actionResult = super.interactMob(player, hand);
        if (actionResult.isAccepted()) {
            this.setPersistent();
        }
        return actionResult;
    }

    @Override
    public void handleStatus(byte status) {
        if (status == EntityStatuses.PLAY_ATTACK_SOUND) {
            this.movementCooldownTicks = 10;
            this.playSound(SoundEvents.ENTITY_HOGLIN_ATTACK, 1.0f, this.getSoundPitch());
        } else {
            super.handleStatus(status);
        }
    }

    @Override
    public int getMovementCooldownTicks() {
        return this.movementCooldownTicks;
    }

    @Override
    public boolean shouldDropXp() {
        return true;
    }

    @Override
    public int getXpToDrop() {
        return this.experiencePoints;
    }


    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(Items.CRIMSON_FUNGUS);
    }

    public boolean isAdult() {
        return !this.isBaby();
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(BABY, false);
        this.dataTracker.startTracking(ATTACKING, false);
        this.dataTracker.startTracking(ENRAGED, false);
    }

    public void setAttacking(boolean attacking) {
        this.dataTracker.set(ATTACKING, attacking);
    }

    public boolean isAttacking() {
        return this.dataTracker.get(ATTACKING);
    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(40) + 80;
            this.idleAnimationState.start(this.age);
        } else {
            --this.idleAnimationTimeout;
        }

        if (this.isAttacking() && attackAnimationTimeout <= 0) {
            attackAnimationTimeout = 40; // Length of attack animation in ticks
            attackAnimationState.start(this.age);
        } else {
            --this.attackAnimationTimeout;
        }

        if (!this.isAttacking()) {
            attackAnimationState.stop();
        }
    }

    protected void updateLimbs(float v) {
        float f;
        if (this.getPose() == EntityPose.STANDING) {
            f = Math.min(v * 6.0F, 1.0F);
        } else {
            f = 0.0F;
        }

        this.limbAnimator.updateLimbs(f, 0.2F);
    }

    @Override
    public EntityView method_48926() {
        return this.getWorld();
    }
}


