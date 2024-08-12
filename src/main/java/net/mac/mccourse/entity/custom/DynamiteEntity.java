package net.mac.mccourse.entity.custom;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.entity.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public class DynamiteEntity extends ThrownItemEntity {

    public DynamiteEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public DynamiteEntity(LivingEntity livingEntity, World world) {
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
        if (blockHitResult != null) {
            if (this.getWorld() instanceof ServerWorld) {
                this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 4, false, World.ExplosionSourceType.TNT);
                this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.BLOCK_SLIME_BLOCK_BREAK, SoundCategory.NEUTRAL, 1f, 1f);
            }
            discard();
        }
        this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_SLIME_JUMP, SoundCategory.NEUTRAL, 1.0F, 1.0F);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (entityHitResult != null) {
            if (this.getWorld() instanceof ServerWorld) {
                this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 4, false, World.ExplosionSourceType.TNT);
                this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.BLOCK_SLIME_BLOCK_BREAK, SoundCategory.NEUTRAL, 1f, 1f);
            }
            discard();
        }
        this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_SLIME_JUMP, SoundCategory.NEUTRAL, 1.0F, 1.0F);
    }
}

