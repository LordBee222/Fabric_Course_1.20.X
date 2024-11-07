package net.mac.mccourse.entity.custom;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import net.mac.mccourse.entity.ModEntities;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class SoulShulkerBulletEntity extends ShulkerBulletEntity implements FlyingItemEntity {
    @Nullable private Entity target;
    @Nullable private Direction direction;
    private int stepCount;
    private double targetX;
    private double targetY;
    private double targetZ;
    @Nullable private UUID targetUuid;

    public SoulShulkerBulletEntity(World world, LivingEntity owner, Entity target, Direction.Axis axis) {
        this(ModEntities.SOUL_SHULKER_BULLET, world);
        this.setOwner(owner);
        BlockPos blockPos = owner.getBlockPos();
        double d = (double)blockPos.getX() + 0;
        double e = (double)blockPos.getY() + 5;
        double f = (double)blockPos.getZ() + 0;
        this.refreshPositionAndAngles(d, e, f, this.getYaw(), this.getPitch());
        this.target = target;
        this.direction = Direction.UP;
        this.changeTargetDirection(axis);
    }

    public SoulShulkerBulletEntity(EntityType<? extends SoulShulkerBulletEntity> entityType, World world) {
        super(entityType, world);
    }

    private void changeTargetDirection(@Nullable Direction.Axis axis) {
        BlockPos blockPos;
        double d = 0.5;
        if (this.target == null) {
            blockPos = this.getBlockPos().down();
        } else {
            d = (double)this.target.getHeight() * 0.5;
            blockPos = BlockPos.ofFloored(this.target.getX(), this.target.getY() + d, this.target.getZ());
        }
        double e = (double)blockPos.getX() + 0.5;
        double f = (double)blockPos.getY() + d;
        double g = (double)blockPos.getZ() + 0.5;
        Direction direction = null;
        if (!blockPos.isWithinDistance(this.getPos(), 2.0)) {
            BlockPos blockPos2 = this.getBlockPos();
            ArrayList<Direction> list = Lists.newArrayList();
            if (axis != Direction.Axis.X) {
                if (blockPos2.getX() < blockPos.getX() && this.getWorld().isAir(blockPos2.east())) {
                    list.add(Direction.EAST);
                } else if (blockPos2.getX() > blockPos.getX() && this.getWorld().isAir(blockPos2.west())) {
                    list.add(Direction.WEST);
                }
            }
            if (axis != Direction.Axis.Y) {
                if (blockPos2.getY() < blockPos.getY() && this.getWorld().isAir(blockPos2.up())) {
                    list.add(Direction.UP);
                } else if (blockPos2.getY() > blockPos.getY() && this.getWorld().isAir(blockPos2.down())) {
                    list.add(Direction.DOWN);
                }
            }
            if (axis != Direction.Axis.Z) {
                if (blockPos2.getZ() < blockPos.getZ() && this.getWorld().isAir(blockPos2.south())) {
                    list.add(Direction.SOUTH);
                } else if (blockPos2.getZ() > blockPos.getZ() && this.getWorld().isAir(blockPos2.north())) {
                    list.add(Direction.NORTH);
                }
            }
            direction = Direction.random(this.random);
            if (list.isEmpty()) {
                for (int i = 5; !this.getWorld().isAir(blockPos2.offset(direction)) && i > 0; --i) {
                    direction = Direction.random(this.random);
                }
            } else {
                direction = (Direction)list.get(this.random.nextInt(list.size()));
            }
            e = this.getX() + (double)direction.getOffsetX();
            f = this.getY() + (double)direction.getOffsetY();
            g = this.getZ() + (double)direction.getOffsetZ();
        }
        this.setDirection(direction);
        double h = e - this.getX();
        double j = f - this.getY();
        double k = g - this.getZ();
        double l = Math.sqrt(h * h + j * j + k * k);
        if (l == 0.0) {
            this.targetX = 0.0;
            this.targetY = 0.0;
            this.targetZ = 0.0;
        } else {
            this.targetX = h / l * 0.15;
            this.targetY = j / l * 0.15;
            this.targetZ = k / l * 0.15;
        }
        this.velocityDirty = true;
        this.stepCount = 10 + this.random.nextInt(5) * 10;



        /*BlockPos targetPos;
        double targetHeightOffset = 0.5;

        // Determine target position: halfway up target entity if it exists, or directly below current position
        if (this.target == null) {
            targetPos = this.getBlockPos().down();
        } else {
            targetHeightOffset = this.target.getHeight() * 0.5;
            targetPos = BlockPos.ofFloored(this.target.getX(), this.target.getY() + targetHeightOffset, this.target.getZ());
        }

        // Coordinates for potential new direction
        double targetX = targetPos.getX() + 0.5;
        double targetY = targetPos.getY() + targetHeightOffset;
        double targetZ = targetPos.getZ() + 0.5;
        Direction direction = null;

        // Check if target is within 2 blocks; if not, find a new direction towards the target
        if (!targetPos.isWithinDistance(this.getPos(), 2.0)) {
            BlockPos currentPos = this.getBlockPos();
            ArrayList<Direction> possibleDirections = Lists.newArrayList();

            // Check along each axis (skipping the provided axis)
            if (axis != Direction.Axis.X) {
                if (currentPos.getX() < targetPos.getX() && this.getWorld().isAir(currentPos.east())) {
                    possibleDirections.add(Direction.EAST);
                } else if (currentPos.getX() > targetPos.getX() && this.getWorld().isAir(currentPos.west())) {
                    possibleDirections.add(Direction.WEST);
                }
            }
            if (axis != Direction.Axis.Y) {
                if (currentPos.getY() < targetPos.getY() && this.getWorld().isAir(currentPos.up())) {
                    possibleDirections.add(Direction.UP);
                } else if (currentPos.getY() > targetPos.getY() && this.getWorld().isAir(currentPos.down())) {
                    possibleDirections.add(Direction.DOWN);
                }
            }
            if (axis != Direction.Axis.Z) {
                if (currentPos.getZ() < targetPos.getZ() && this.getWorld().isAir(currentPos.south())) {
                    possibleDirections.add(Direction.SOUTH);
                } else if (currentPos.getZ() > targetPos.getZ() && this.getWorld().isAir(currentPos.north())) {
                    possibleDirections.add(Direction.NORTH);
                }
            }

            // Randomize direction if no viable path is found
            direction = Direction.random(this.random);
            if (possibleDirections.isEmpty()) {
                int attempts = 5;
                while (!this.getWorld().isAir(currentPos.offset(direction)) && attempts-- > 0) {
                    direction = Direction.random(this.random);
                }
            } else {
                direction = possibleDirections.get(this.random.nextInt(possibleDirections.size()));
            }

            // Update target coordinates based on chosen direction
            targetX = this.getX() + direction.getOffsetX();
            targetY = this.getY() + direction.getOffsetY();
            targetZ = this.getZ() + direction.getOffsetZ();
        }

        // Set new direction and calculate velocity components
        this.setDirection(direction);
        double deltaX = targetX - this.getX();
        double deltaY = targetY - this.getY();
        double deltaZ = targetZ - this.getZ();
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);

        // Avoid division by zero; normalize velocity towards target direction
        double speedFactor = 0.15; // Increase this value to make the entity move faster
        if (distance == 0.0) {
            this.targetX = 0.0;
            this.targetY = 0.0;
            this.targetZ = 0.0;
        } else {
            this.targetX = deltaX / distance * speedFactor;
            this.targetY = deltaY / distance * speedFactor;
            this.targetZ = deltaZ / distance * speedFactor;
        }

        this.velocityDirty = true;
        this.stepCount = 10 + this.random.nextInt(5) * 10; // Set step duration with randomness

         */
    }

    @Override
    public void tick() {

        /*
        super.tick();

        Vec3d velocity = this.getVelocity();

        if (!this.getWorld().isClient) {
            // Check and assign the target if not already done
            if (this.target == null && this.targetUuid != null) {
                this.target = ((ServerWorld) this.getWorld()).getEntity(this.targetUuid);
                if (this.target == null) {
                    this.targetUuid = null; // Clear UUID if target is not found
                }
            }

            // Apply homing behavior if target is valid
            if (this.target != null && this.target.isAlive() && (!(this.target instanceof PlayerEntity) || !this.target.isSpectator())) {
                // Gradually adjust target direction with a slight acceleration
                this.targetX = MathHelper.clamp(this.targetX * 1.025, -1.0, 1.0);
                this.targetY = MathHelper.clamp(this.targetY * 1.025, -1.0, 1.0);
                this.targetZ = MathHelper.clamp(this.targetZ * 1.025, -1.0, 1.0);

                // Adjust velocity toward the target's location
                this.setVelocity(velocity.add(
                        (this.targetX - velocity.x) * 0.2,
                        (this.targetY - velocity.y) * 0.2,
                        (this.targetZ - velocity.z) * 0.2
                ));
            } else if (!this.hasNoGravity()) {
                // Apply gravity if no target
                this.setVelocity(velocity.add(0.0, -0.04, 0.0));
            }

            // Check for collisions and handle if hit
            HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
            if (hitResult.getType() != HitResult.Type.MISS) {
                this.onCollision(hitResult);
            }
        }

        // Move entity according to current velocity
        this.setPosition(this.getX() + velocity.x, this.getY() + velocity.y, this.getZ() + velocity.z);

        // Update rotation based on velocity
        ProjectileUtil.setRotationFromVelocity(this, 0.5f);

        if (this.getWorld().isClient) {
            this.getWorld().addParticle(ParticleTypes.SOUL_FIRE_FLAME,
                    this.getX() - velocity.x,
                    this.getY() - velocity.y + 0.15,
                    this.getZ() - velocity.z,
                    0.0, 0.0, 0.0
            );
        }

        // Additional server-side directional adjustments
        if (!this.getWorld().isClient && this.target != null && !this.target.isRemoved()) {
            if (this.stepCount > 0) {
                this.stepCount--;
                if (this.stepCount == 0) {
                    // Change direction when step count expires
                    this.changeTargetDirection(this.direction == null ? null : this.direction.getAxis());
                }
            }

            if (this.direction != null) {
                BlockPos currentPos = this.getBlockPos();
                Direction.Axis axis = this.direction.getAxis();

                // Adjust target direction based on proximity to obstacles or the target
                if (this.getWorld().isTopSolid(currentPos.offset(this.direction), this)) {
                    this.changeTargetDirection(axis);
                } else {
                    BlockPos targetPos = this.target.getBlockPos();
                    if ((axis == Direction.Axis.X && currentPos.getX() == targetPos.getX()) ||
                            (axis == Direction.Axis.Z && currentPos.getZ() == targetPos.getZ()) ||
                            (axis == Direction.Axis.Y && currentPos.getY() == targetPos.getY())) {
                        this.changeTargetDirection(axis);
                    }
                }
            }
        }

         */


        Vec3d vec3d;
        super.tick();
        if (!this.getWorld().isClient) {
            if (this.target == null && this.targetUuid != null) {
                this.target = ((ServerWorld)this.getWorld()).getEntity(this.targetUuid);
                if (this.target == null) {
                    this.targetUuid = null;
                }
            }
            if (!(this.target == null || !this.target.isAlive() || this.target instanceof PlayerEntity && this.target.isSpectator())) {
                this.targetX = MathHelper.clamp(this.targetX * 1.025, -1.0, 1.0);
                this.targetY = MathHelper.clamp(this.targetY * 1.025, -1.0, 1.0);
                this.targetZ = MathHelper.clamp(this.targetZ * 1.025, -1.0, 1.0);
                vec3d = this.getVelocity();
                this.setVelocity(vec3d.add((this.targetX - vec3d.x) * 0.2, (this.targetY - vec3d.y) * 0.2, (this.targetZ - vec3d.z) * 0.2));
            } else if (!this.hasNoGravity()) {
                this.setVelocity(this.getVelocity().add(0.0, -0.04, 0.0));
            }
            HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
            if (hitResult.getType() != HitResult.Type.MISS) {
                this.onCollision(hitResult);
            }
        }
        this.checkBlockCollision();
        vec3d = this.getVelocity();
        this.setPosition(this.getX() + vec3d.x, this.getY() + vec3d.y, this.getZ() + vec3d.z);
        ProjectileUtil.setRotationFromVelocity(this, 0.5f);
        if (this.getWorld().isClient) {
            this.getWorld().addParticle(ParticleTypes.END_ROD, this.getX() - vec3d.x, this.getY() - vec3d.y + 0.15, this.getZ() - vec3d.z, 0.0, 0.0, 0.0);
        } else if (this.target != null && !this.target.isRemoved()) {
            if (this.stepCount > 0) {
                --this.stepCount;
                if (this.stepCount == 0) {
                    this.changeTargetDirection(this.direction == null ? null : this.direction.getAxis());
                }
            }
            if (this.direction != null) {
                BlockPos blockPos = this.getBlockPos();
                Direction.Axis axis = this.direction.getAxis();
                if (this.getWorld().isTopSolid(blockPos.offset(this.direction), this)) {
                    this.changeTargetDirection(axis);
                } else {
                    BlockPos blockPos2 = this.target.getBlockPos();
                    if (axis == Direction.Axis.X && blockPos.getX() == blockPos2.getX() || axis == Direction.Axis.Z && blockPos.getZ() == blockPos2.getZ() || axis == Direction.Axis.Y && blockPos.getY() == blockPos2.getY()) {
                        this.changeTargetDirection(axis);
                    }
                }
            }
        }
    }

    private void setDirection(@Nullable Direction direction) {
        this.direction = direction;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);

        Entity hitEntity = entityHitResult.getEntity();
        Entity owner = this.getOwner();
        LivingEntity ownerAsLiving = (owner instanceof LivingEntity) ? (LivingEntity) owner : null;
        boolean didDamage = hitEntity.damage(this.getDamageSources().mobProjectile(this, ownerAsLiving), 4.0f);
        if (didDamage) {
            this.applyDamageEffects(ownerAsLiving, hitEntity);
            if (hitEntity instanceof LivingEntity) {
                this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 5, World.ExplosionSourceType.MOB);
            }
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        ((ServerWorld)this.getWorld()).spawnParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 2, 0.2, 0.2, 0.2, 0.0);
        this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 5, World.ExplosionSourceType.MOB);
        this.playSound(SoundEvents.ENTITY_SHULKER_BULLET_HIT, 1.0f, 1.0f);
    }

    @Override
    public ItemStack getStack() {
        return new ItemStack(Items.SOUL_TORCH);
    }
}