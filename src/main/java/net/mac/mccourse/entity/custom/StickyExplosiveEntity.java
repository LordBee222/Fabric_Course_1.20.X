package net.mac.mccourse.entity.custom;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.entity.ModEntities;
import net.mac.mccourse.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;

public class StickyExplosiveEntity extends ThrownItemEntity {
    private int ticksStuck = 0;
    private boolean stuck;
    private Vec3d stuckPos;

    public StickyExplosiveEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public StickyExplosiveEntity(LivingEntity owner, World world) {
        super(ModEntities.BOOM_HONEY, owner, world);
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    protected Item getDefaultItem() {
        return null;
    }

    public void setStuck(boolean isStuck) {
        this.stuck = isStuck;
    }

    public void setStuckPos(double x, double y, double z) {
        this.stuckPos = new Vec3d(x, y, z);
    }

    public Vec3d getStuckPos() {
        return this.stuckPos;
    }

    @Override
    public void tick() {
        super.tick();

        // Check if the entity is stuck
        if (this.stuck) {
            MCCourseMod.LOGGER.info("STUCK at " + getStuckPos());
            this.ticksStuck++;
            this.setVelocity(0, 0, 0); // Stop the entity's motion
            this.setPosition(getStuckPos());

            if (this.ticksStuck >= 15) { // 20 ticks = 1 second
                double radius = 4;
                double verticalVelocity = 1.5;
                double horizontalVelocity = 3.75;

                List<Entity> entities = this.getWorld().getOtherEntities(this, new Box(
                        this.getX() - radius, this.getY() - radius, this.getZ() - radius,
                        this.getX() + radius, this.getY() + radius, this.getZ() + radius
                ));

                for (Entity entity : entities) {
                    Vec3d knockbackDirection = entity.getPos().subtract(this.getPos()).normalize();
                    Vec3d addedVelocity = new Vec3d(knockbackDirection.x * horizontalVelocity, verticalVelocity, knockbackDirection.z * horizontalVelocity);
                    entity.addVelocity(addedVelocity.x, addedVelocity.y, addedVelocity.z);
                    entity.velocityModified = true;

                }
                this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.NEUTRAL, 1f, 1f);
                this.getWorld().addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
                this.discard(); // Remove entity after explosion
            }
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if (blockHitResult != null) {
            if (this.getWorld() instanceof ServerWorld) {
                this.setNoGravity(true);
                this.setStuck(true);
                this.setStuckPos(this.getX(), this.getY(), this.getZ());
            }
        }
        this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_SLIME_JUMP, SoundCategory.NEUTRAL, 1.0F, 1.0F);
    }
}
