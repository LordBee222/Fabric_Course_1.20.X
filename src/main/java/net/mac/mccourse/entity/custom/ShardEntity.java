package net.mac.mccourse.entity.custom;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.entity.ModEntities;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class ShardEntity extends ThrownItemEntity {
    private static final TrackedData<Integer> FUSE = DataTracker.registerData(ShardEntity.class, TrackedDataHandlerRegistry.INTEGER);


    public ShardEntity(EntityType<? extends ShardEntity> entityType, World world) {
        super(entityType, world);
    }

    public ShardEntity(LivingEntity owner, World world) {
        super(ModEntities.SHARD,owner, world);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        int Fuse = this.getFuse();
        MCCourseMod.LOGGER.info("FUSE: " + Fuse);
    }

    private Integer getFuse(){
        return this.dataTracker.get(FUSE);
    }

    private void setFuse(int value){
        this.dataTracker.set(FUSE, value);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getWorld().isClient){
            this.setFuse((this.getFuse() + 1));
            MCCourseMod.LOGGER.info("Incremented Fuse");
        }
    }

    @Override
    protected Item getDefaultItem() {
        return Items.AMETHYST_SHARD;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(FUSE, 0);
    }
}
