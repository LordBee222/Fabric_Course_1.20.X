package net.mac.mccourse.entity.custom;

import net.mac.mccourse.entity.ModEntities;
import net.mac.mccourse.item.ModItems;
import net.mac.mccourse.world.CustomExplosion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

public class BombEntity extends ThrownItemEntity {
    private float explosionRadius = 3f;
    public int ticksUntilRemoval;
    public EntityType<? extends Entity> entityToSpawn;
    public int amount;
    public float velocity;

    public BombEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
        this.setExplosionRadius(3f);
        this.ticksUntilRemoval = -1;
    }

    public BombEntity(LivingEntity owner, World world, EntityType<? extends Entity> entityToSpawn, int amount, float velocity) {
        super(ModEntities.BOMB, owner, world);
        this.setExplosionRadius(3f);
        this.ticksUntilRemoval = -1;
        this.entityToSpawn = entityToSpawn;
        this.amount = amount;
        this.velocity = velocity;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        if (!this.getWorld().isClient) {
            for (int i = 0; i < amount; i++) {
                Entity entity = entityToSpawn.create(this.getWorld());
                if (entity instanceof ProjectileEntity) {
                    ((ProjectileEntity) entity).setOwner(this.getOwner());

                }
                entity.setPos(this.getX(), this.getY(), this.getZ());
                entity.updateTrackedPosition(this.getX(), this.getY() + .5, this.getZ());   
                entity.setVelocity(random.nextGaussian() * velocity, random.nextGaussian() * velocity, random.nextGaussian() * velocity);
                this.getWorld().spawnEntity(entity);
                this.getWorld().addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getParticleX(1), this.getY(), this.getParticleZ(1), 0, 0, 0);

            }
        }
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }


    @Override
    protected Item getDefaultItem() {
        return ModItems.BOOM_SLIME;
    }

    public float getExplosionRadius() {
        return this.explosionRadius;
    }

    public void setExplosionRadius(float float_1) {
        this.explosionRadius = float_1;
    }

}
