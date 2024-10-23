package net.mac.mccourse;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.List;

public class CurvingHomingRaycast {
    private static final double SPEED_BLOCKS_PER_SECOND = 10.0;
    private static final int TICKS_PER_SECOND = 20;
    private static final double SPEED_BLOCKS_PER_TICK = SPEED_BLOCKS_PER_SECOND / TICKS_PER_SECOND;

    private Vec3d currentPosition;
    private Vec3d currentDirection;
    private double remainingDistance;
    private PlayerEntity user;
    private World world;
    private boolean active;
    private LivingEntity target;

    public CurvingHomingRaycast(PlayerEntity user, double range) {
        this.user = user;
        this.world = user.getWorld();
        this.currentPosition = user.getCameraPosVec(1.0F);
        this.currentDirection = user.getRotationVec(1.0F).normalize();
        this.remainingDistance = range;
        this.active = true;

        // Find the nearest target (mob) within a certain range
        this.target = findNearestTarget(user, range);

        // Register the tick event
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (this.active) {
                this.update();
            }
        });
    }

    private LivingEntity findNearestTarget(PlayerEntity user, double range) {
        List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, user.getBoundingBox().expand(range), e -> e != user);
        return entities.stream()
                .min((e1, e2) -> Double.compare(user.distanceTo(e1), user.distanceTo(e2)))
                .orElse(null);
    }

    private void update() {
        if (target == null) {
            this.active = false;
            return;
        }

        // Calculate direction towards the target
        Vec3d targetDirection = target.getPos().subtract(currentPosition).normalize();

        // Add curvature by gradually adjusting the current direction toward the target direction
        double curveFactor = 0.1; // Adjust the curve smoothness (lower value = smoother curve)
        currentDirection = currentDirection.add(targetDirection.subtract(currentDirection).multiply(curveFactor)).normalize();

        // Move the ray forward by the speed per tick
        double stepDistance = Math.min(SPEED_BLOCKS_PER_TICK, remainingDistance);
        Vec3d nextPosition = currentPosition.add(currentDirection.multiply(stepDistance));

        spawnParticleTrail(currentPosition, nextPosition);

        // Perform a raycast for this small step
        HitResult hitResult = world.raycast(new RaycastContext(
                currentPosition,
                nextPosition,
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.NONE,
                user
        ));

        if (hitResult.getType() != HitResult.Type.MISS) {
            // Handle collision
            onCollision(hitResult);
            this.active = false;
            return;
        }

        // Update position and remaining distance
        currentPosition = nextPosition;
        remainingDistance -= stepDistance;

        if (remainingDistance <= 0) {
            // Reached the max range without hitting anything
            onFinish();
            this.active = false;
        }
    }

    private void spawnParticleTrail(Vec3d start, Vec3d end) {
        double distance = start.distanceTo(end);
        int particleCount = (int) (distance * 1); // Number of particles based on distance
        for (int i = 0; i <= particleCount; i++) {
            MCCourseMod.LOGGER.info("SPAWNED PARTICLES");
            double progress = (double) i / particleCount;
            //Vec3d interpolatedPos = start.lerp(end, progress);
            double lerpFactor = (double) i / particleCount;
            double x = start.x + (end.x - start.x) * lerpFactor;
            double y = start.y + (end.y - start.y) * lerpFactor;
            double z = start.z + (end.z - start.z) * lerpFactor;
            world.createExplosion(null, x, y, z, 4.0F, World.ExplosionSourceType.TNT);
            world.addParticle(ParticleTypes.SONIC_BOOM, x, y, z, 0, 0, 0);
        }
    }

    private void onCollision(HitResult hitResult) {
        // Handle what happens when the ray hits something
        System.out.println("Hit something at: " + hitResult.getPos());
    }

    private void onFinish() {
        // Handle what happens when the ray reaches the max range
        System.out.println("Raycast finished at: " + currentPosition);
    }
}
