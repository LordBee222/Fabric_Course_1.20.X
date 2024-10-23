package net.mac.mccourse.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class CalibratedCrossbowUtil {
    private static final java.util.Random RANDOM = new java.util.Random();


    public static void spawnParticlesAlongRay(ServerWorld world, Vec3d startPos, Vec3d endPos) {
        double distance = startPos.distanceTo(endPos);
        int particleCount = (int) (distance * 4);

        for (int i = 0; i <= particleCount; i++) {
            double lerpFactor = (double) i / particleCount;
            double x = startPos.x + (endPos.x - startPos.x) * lerpFactor;
            double y = startPos.y + (endPos.y - startPos.y) * lerpFactor;
            double z = startPos.z + (endPos.z - startPos.z) * lerpFactor;

            world.spawnParticles(ParticleTypes.SMOKE, x, y, z, 1, 0, 0, 0, 0);
        }
    }

    public static Vec3d performHipfireRaycast(World world, LivingEntity user, double range) {
        float baseYaw = user.getYaw();
        float basePitch = user.getPitch();
        float spread = 20.0F; // Adjust the spread angle range
        float offsetYaw = baseYaw + (RANDOM.nextFloat() - 0.5F) * spread;
        float offsetPitch = basePitch + (RANDOM.nextFloat() - 0.5F) * spread;
        Vec3d direction = Vec3d.fromPolar(offsetPitch, offsetYaw).normalize();
        Vec3d startPos = user.getCameraPosVec(1.0F);
        Vec3d endPos = startPos.add(direction.multiply(range));
        HitResult hitResult = world.raycast(new RaycastContext(startPos, endPos, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, user));
        if (hitResult.getType() == HitResult.Type.BLOCK || hitResult.getType() == HitResult.Type.ENTITY) return hitResult.getPos(); // Return the hit position
        return null; // No hit
    }


    public static Vec3d getAdsTarget(LivingEntity player, World world, double range) {
        Vec3d startPos = player.getCameraPosVec(1.0F);
        Vec3d direction = player.getRotationVec(1.0F);
        Vec3d endPos = startPos.add(direction.multiply(range));
        HitResult blockHitResult = world.raycast(new RaycastContext(startPos, endPos, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player));
        Box searchBox = player.getBoundingBox().stretch(direction.multiply(range)).expand(1.0D, 1.0D, 1.0D);
        EntityHitResult entityHitResult = raycastEntities(world, player, startPos, endPos, searchBox);
        if (entityHitResult != null && entityHitResult.getPos().distanceTo(startPos) < blockHitResult.getPos().distanceTo(startPos)) {
            return entityHitResult.getPos();
        } else {
            return blockHitResult.getPos();
        }
    }

    public static EntityHitResult raycastEntities(World world, LivingEntity player, Vec3d startPos, Vec3d endPos, Box searchBox) {
        return ProjectileUtil.raycast(player, startPos, endPos, searchBox, (entity) -> !entity.isSpectator() && entity.isAlive(), endPos.distanceTo(startPos));
    }

    public static Vec3d getSpreadDirection(LivingEntity user) {
        // Get the player's view direction (pitch and yaw)
        float baseYaw = user.getYaw();
        float basePitch = user.getPitch();

        // Introduce random spread (small offsets)
        float spread = 20.0F; // Adjust the spread angle range
        float offsetYaw = baseYaw + (RANDOM.nextFloat() - 0.5F) * spread;
        float offsetPitch = basePitch + (RANDOM.nextFloat() - 0.5F) * spread;

        // Calculate the direction vector with the new pitch and yaw
        return Vec3d.fromPolar(offsetPitch, offsetYaw).normalize();
    }
}
