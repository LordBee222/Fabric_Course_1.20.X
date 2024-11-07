package net.mac.mccourse.entity.custom;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.entity.ModEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;

import java.util.List;
import java.util.Random;

public class HomingRocketEntity extends ProjectileEntity implements FlyingItemEntity{
    private static final double SPEED = 0.5;
    private static final float EXPLOSION_RADIUS = 5.0F;
    private LivingEntity target;
    private int ticksTillHoming;


    public HomingRocketEntity(EntityType<? extends HomingRocketEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        if (blockHitResult != null) {
            if (this.getWorld() instanceof ServerWorld) {
                MCCourseMod.LOGGER.info("HIT BLOCK");
                this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 255, false, World.ExplosionSourceType.TNT);
            }
            discard();
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (this.getWorld() instanceof ServerWorld) {
            MCCourseMod.LOGGER.info("HIT Collision");
            this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 255, false, World.ExplosionSourceType.TNT);
            this.discard();
        }
    }

    public HomingRocketEntity(World world, LivingEntity target, LivingEntity owner) {
        this(ModEntities.HOMING_ROCKET, world);
        this.target = target;
        this.setOwner(owner);
        this.setPosition(owner.getX(), owner.getY() + 0.5f, owner.getZ());  // Start above the target\
        this.ticksTillHoming = owner.getRandom().nextBetween(25, 100);
        this.setVelocity(new Vec3d(0, 0.1, 0));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            if (target != null) {
                if (this.target.isDead() && this.getOwner() instanceof LivingEntity owner && owner.getBrain().hasMemoryModule(MemoryModuleType.ATTACK_TARGET))
                    this.target = owner.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).get();
                if (this.ticksTillHoming > 0) --this.ticksTillHoming;
                if (ticksTillHoming == 0) {
                    if (target != null && target.isAlive()) {
                        //Vec3d targetCenterPos = target.getPos().add(0, target.getHeight() / 2.0, 0);
                        Vec3d direction = target.getPos().add(0, target.getHeight() / 2.0, 0).subtract(this.getPos()).normalize();

                        Vec3d velocity = direction.multiply(SPEED);

                        this.setVelocity(velocity);
                        //this.move(MovementType.SELF, this.getVelocity());


                        if (this.squaredDistanceTo(target.getPos().add(0, target.getHeight() / 2.0, 0)) < 0.1)
                            explode();

                    }
                }
                this.move(MovementType.SELF, this.getVelocity());
                serverWorld.spawnParticles(ParticleTypes.SCULK_SOUL, this.getX(), this.getY(), this.getZ(), 5, 0.1, 0.1, 0.1, 0.02);
            } else {
                this.discard();
            }
        }
    }

    @Override
    protected boolean canHit(Entity entity) {
        return super.canHit(entity) && !entity.noClip;
    }

    @Override
    public boolean canHit() {
        return true;
    }

    private void explode() {
        this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), EXPLOSION_RADIUS, World.ExplosionSourceType.MOB);
        this.playSound(SoundEvents.ENTITY_ELDER_GUARDIAN_DEATH, 1.0f, 2.0f);
        //this.explode();

        List<Entity> entities = this.getWorld().getOtherEntities(this, new Box(
                this.getX() - 6, this.getY() - 6, this.getZ() - 6,
                this.getX() + 6, this.getY() + 6, this.getZ() + 6
        ));

        if (this.getWorld() instanceof ServerWorld server) server.spawnParticles(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);

        for (Entity target : entities) {
            Vec3d knockbackDirection = target.getPos().subtract(target.getPos()).normalize();
            Vec3d addedVelocity = new Vec3d(knockbackDirection.x * 3.75, 1.5, knockbackDirection.z * 3.75);
            target.addVelocity(addedVelocity.x, addedVelocity.y, addedVelocity.z);
            target.velocityModified = true;
        }


        //this.target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 200, 0));

        this.discard();
    }

    private void destroy() {
        this.discard();
        this.getWorld().emitGameEvent(GameEvent.ENTITY_DAMAGE, this.getPos(), GameEvent.Emitter.of(this));
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (!this.getWorld().isClient) {
            this.playSound(SoundEvents.PARTICLE_SOUL_ESCAPE, 1.0f, 2.0f);
            ((ServerWorld) this.getWorld()).spawnParticles(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 15, 0.2, 0.2, 0.2, 0.0);
            this.destroy();
        }
        return true;
    }

    @Override
    protected void initDataTracker() {
    }

    @Override
    public ItemStack getStack() {
        return new ItemStack(Items.SOUL_LANTERN);
    }
}