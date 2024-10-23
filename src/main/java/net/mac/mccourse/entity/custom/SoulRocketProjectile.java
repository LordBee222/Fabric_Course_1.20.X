package net.mac.mccourse.entity.custom;

import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.mac.mccourse.entity.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SoulRocketProjectile extends PersistentProjectileEntity {
    private LivingEntity target;
    private int speed;

    public SoulRocketProjectile(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public SoulRocketProjectile(World world, LivingEntity owner, LivingEntity target, int speed) {
        super(ModEntities.SOUL_ROCKET_PROJECTILE, owner , world);
        this.target = target;
        this.speed = speed;
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    public void tick() {
        super.tick();
        Vec3d targetDirection;
        if (this.target != null && this.target.isAlive()){
            targetDirection = new Vec3d(this.target.getX() - this.getX(), this.target.getY() - this.getY(), this.target.getZ() - this.getZ());
            targetDirection = targetDirection.normalize();
            this.setVelocity(targetDirection.x * this.speed, targetDirection.y * this.speed, targetDirection.z * this.speed);
        }
        this.move(MovementType.SELF, this.getVelocity());
        if (this.isOnGround() || this.collidesWithEntity(target)) {
            this.remove(RemovalReason.DISCARDED);
        }
    }

    private boolean collidesWithEntity(Entity target) {
        return this.getBoundingBox().intersects(target.getBoundingBox());
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (hitResult.getType() == HitResult.Type.BLOCK){
            this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 4, World.ExplosionSourceType.MOB);
        }
    }

    @Override
    protected ItemStack asItemStack() {
        return null;
    }


}
