package net.mac.mccourse.entity.custom;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.entity.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public class DiceProjectileEntity extends ThrownItemEntity {
    private int bounceCount = 0;  // Counter to keep track of bounces
    private static final Random random = new Random();


    public DiceProjectileEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public DiceProjectileEntity(LivingEntity livingEntity, World world) {
        super(ModEntities.THROWN_DICE_PROJECTILE, livingEntity, world);
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    protected Item getDefaultItem() {
        return null;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        Vec3d flyDir = this.getRotationVec(1.0F);
        if (blockHitResult != null) {
            if (bounceCount >= 10) {
                if (this.getWorld() instanceof ServerWorld) {
                    this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 4, false, World.ExplosionSourceType.TNT);
                    this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.BLOCK_SLIME_BLOCK_BREAK, SoundCategory.NEUTRAL, 1f, 1f);
                }
                discard();
            }
            Direction hitDirection = blockHitResult.getSide();
            Vec3d normalVec = new Vec3d(hitDirection.getOffsetX(), hitDirection.getOffsetY(), hitDirection.getOffsetZ());
            Vec3d velocity = this.getVelocity();
            double dotProduct = normalVec.dotProduct(velocity);
            Vec3d result = normalVec.multiply(dotProduct / normalVec.lengthSquared());
            Vec3d bounceDir = velocity.subtract(result.multiply(2));

            // Apply a slight variation in speed
            //double speedMultiplier = 0.5 + Math.random() * 0.25;    // Remove *0.25 for no dropoff
            //double speedMultiplier = 0.5 + Math.random();    // Remove *0.25 for no dropoff
            double speedMultiplier = 0.9;  // Remove *0.25 for no dropoff

            this.setVelocity(bounceDir.multiply(speedMultiplier));

            // Increment the bounce counter
            bounceCount++;
            MCCourseMod.LOGGER.info("BOUNCES: " + bounceCount);

            // Play the bounce sound
            this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_SLIME_JUMP, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        }
    }
}

