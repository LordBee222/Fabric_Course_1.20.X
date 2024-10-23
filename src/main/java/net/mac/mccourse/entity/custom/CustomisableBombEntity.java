package net.mac.mccourse.entity.custom;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.entity.ModEntities;
import net.mac.mccourse.item.ModItems;
import net.mac.mccourse.world.CustomExplosion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class CustomisableBombEntity extends ThrownItemEntity {
    private TriggerType triggerType;
    private BlastType blastType;
    private int fuse, blastPower;
    private static final TrackedData<Integer> FUSE = DataTracker.registerData(CustomisableBombEntity.class, TrackedDataHandlerRegistry.INTEGER);


    public CustomisableBombEntity(EntityType<? extends CustomisableBombEntity> entityType, World world) {
        super(entityType, world);
        this.initDataTracker();
        this.triggerType = TriggerType.FUSE;  // Default values
        this.blastType = BlastType.NORMAL;
        this.blastPower = 1;
        this.dataTracker.set(FUSE, 0);

    }

    public CustomisableBombEntity(LivingEntity owner, World world, int blastPower, TriggerType triggerType, BlastType blastType) {
        super(ModEntities.CUSTOM_BOMB_ENTITY, owner, world);
        this.blastPower = blastPower;
        this.blastType = blastType;
        this.initDataTracker();
        this.triggerType = triggerType;
        this.dataTracker.set(FUSE, 0);
    }

    private void explode() {
        //CustomExplosion explosion = this.getExplosion();
        //explosion.collectBlocksAndDamageEntities();
        //explosion.affectWorld(true);
        //this.discard();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getWorld().isClient) {

            this.setVelocity(0, 0, 0);
            if (this.isFuseTicking() && this.triggerType == TriggerType.FUSE) {
                int fuse = this.getFuse();
                if (fuse >= 60) {
                    this.explode();
                } else {
                    this.setFuse(++fuse);
                }
            }
        } else if (this.getWorld().isClient && this.triggerType == TriggerType.FUSE && this.isFuseTicking()) {
            this.getWorld().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.3, this.getZ(), 0, 0, 0);
        }
        if (this.getWorld().getBlockState(this.getBlockPos()).isFullCube(this.getWorld(), this.getBlockPos())) {
            this.setPosition(this.prevX, this.prevY, this.prevZ);
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        if (!this.getWorld().isClient) {
            MCCourseMod.LOGGER.info("BEFORE SWITCH");
            switch (this.triggerType) {
                case FUSE -> {
                    if (!this.isFuseTicking()) {
                        MCCourseMod.LOGGER.info("THE THING IS A FUSE");
                        this.startFuse();
                    }
                }

                case IMPACT -> {
                    MCCourseMod.LOGGER.info("THE THING IS A IMPACT");
                    this.explode();
                }
            }
        }
    }

    @Override
    protected void initDataTracker() {
            this.dataTracker.startTracking(FUSE, 0);
    }

    @Override
    protected Item getDefaultItem() {
        return this.blastType == BlastType.NORMAL ? ModItems.DYNAMITE : this.blastType == BlastType.AMETHYST ? Items.AMETHYST_SHARD : null;
    }

    private boolean isFuseTicking() {
        return this.dataTracker.get(FUSE) > 1;
    }

    private int getFuse(){
        return this.dataTracker.get(FUSE);
    }

    private void setFuse(int value){
        this.dataTracker.set(FUSE, value);
    }

    private void startFuse() {
        this.dataTracker.set(FUSE, 1);
    }

    public CustomExplosion getExplosion() {
        return new CustomExplosion(this.getWorld(), this, this.getX(), this.getY(), this.getZ(), this.blastPower, false, Explosion.DestructionType.DESTROY);
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    public enum TriggerType {
        FUSE, IMPACT
    }

    public enum BlastType {
        NORMAL, AMETHYST
    }


}
