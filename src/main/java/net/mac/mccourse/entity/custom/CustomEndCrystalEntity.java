package net.mac.mccourse.entity.custom;

import net.mac.mccourse.entity.ModEntities;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;


public class CustomEndCrystalEntity extends PersistentProjectileEntity {
    public int endCrystalAge;

    public CustomEndCrystalEntity(EntityType<? extends CustomEndCrystalEntity> entityType, World world) {
        super(entityType, world);
        this.intersectionChecked = true;
        this.endCrystalAge = this.random.nextInt(100000);

    }

    public CustomEndCrystalEntity(World world, LivingEntity owner) {
        this(ModEntities.CUSTOM_CRYSTAL, world);
        this.setPosition(owner.getX(), owner.getY(), owner.getZ());
        this.setOwner(owner);
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if (blockHitResult != null) {
            if (this.getWorld() instanceof ServerWorld) {
                    this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 255, false, World.ExplosionSourceType.TNT);
                }
            discard();
        }
    }

    @Override
    public void tick() {
        super.tick();
        ++this.endCrystalAge;
    }

    @Override
    protected ItemStack asItemStack() {
        return null;
    }

}
