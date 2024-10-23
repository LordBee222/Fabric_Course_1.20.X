package net.mac.mccourse.entity.goals;

import net.mac.mccourse.entity.custom.SoulmellowEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldEvents;

import java.util.EnumSet;

public class UseHauntingGoal extends Goal {
    private final SoulmellowEntity soulmellow;
    private int projectilesToFire;
    private int cooldown;

    public UseHauntingGoal(SoulmellowEntity soulmellow) {
        this.soulmellow = soulmellow;
        this.cooldown = 0;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    @Override
    public boolean canStart() {
        LivingEntity target = this.soulmellow.getTarget();

        // Does Target Exist, Can be targeted & Is not on cooldown
        return target != null && target.isAlive() && this.soulmellow.canTarget(target);
    }

    @Override
    public void tick() {
        if (this.cooldown != 0){
            cooldown--;
            return;
        }

        // Get Target
        LivingEntity livingEntity = this.soulmellow.getTarget();
        if (livingEntity == null) {
            return;
        }

        // Can See Target?
        boolean bl = this.soulmellow.getVisibilityCache().canSee(livingEntity);

        // Distance to Target
        double d = this.soulmellow.squaredDistanceTo(livingEntity);

        Vec3d vec3d = this.soulmellow.getRotationVec(1.0f);
        double f = livingEntity.getX() - (this.soulmellow.getX() + vec3d.x * 4.0);
        double g = livingEntity.getBodyY(0.5) - (0.5 + this.soulmellow.getBodyY(0.5));
        double h = livingEntity.getZ() - (this.soulmellow.getZ() + vec3d.z * 4.0);
        FireballEntity fireball = new FireballEntity(this.soulmellow.getWorld(), this.soulmellow, f, g, h, 3);
        fireball.setPosition(this.soulmellow.getX() + vec3d.x * 4.0, this.soulmellow.getBodyY(0.5) + 0.5, fireball.getZ() + vec3d.z * 4.0);
        this.cooldown = 100;
        super.tick();
    }
}
