package net.mac.mccourse.item.custom;

import net.mac.mccourse.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class ModPoisonSwordItem extends SwordItem {
    public ModPoisonSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (world instanceof ServerWorld serverWorld) {


            Vec3d startPos = player.getCameraPosVec(1.0F);
            Vec3d direction = player.getRotationVec(1.0F).normalize().multiply(100);
            Vec3d endPos = startPos.add(direction);

            // Perform the raycast
            BlockHitResult hitResult = world.raycast(new RaycastContext(
                    startPos, endPos,
                    RaycastContext.ShapeType.COLLIDER,
                    RaycastContext.FluidHandling.NONE,
                    player));

            Vec3d hitPos = hitResult.getPos();

            // Step 2: Sample points along the ray's path
            double step = 0.2; // Smaller step = more particles
            Vec3d rayDirection = hitPos.subtract(startPos).normalize(); // Normalize direction
            double distance = startPos.distanceTo(hitPos);

            for (double i = 0; i < distance; i += step) {
                Vec3d particlePos = startPos.add(rayDirection.multiply(i));
                // Step 3: Spawn particles at the sampled position
                serverWorld.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME, particlePos.getX(), particlePos.getY(), particlePos.getZ(), 1, 0.2, 0.2, 0.2, 0.1);
            }

            world.createExplosion(null, hitPos.getX(), hitPos.getY(), hitPos.getZ(), 5, false, World.ExplosionSourceType.TNT);

        }
        return super.use(world, player, hand);
    }

}
