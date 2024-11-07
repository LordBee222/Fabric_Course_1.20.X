package net.mac.mccourse.item.custom;

import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.mac.mccourse.util.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

public class ModPoisonSwordItem extends SwordItem {
    public ModPoisonSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (world instanceof ServerWorld serverWorld) {

            float spreadAngle = 20.0f; // Adjust the angle as needed for your desired spread

            Vec3d startPos = player.getEyePos();
            Vec3d direction = player.getRotationVec(1.0F);

            castRay(world, player, startPos, direction);

            Vec3d leftDirection = direction.rotateY((float) Math.toRadians(spreadAngle));
            castRay(world, player, startPos, leftDirection);

            Vec3d rightDirection = direction.rotateY((float) Math.toRadians(-spreadAngle));
            castRay(world, player, startPos, rightDirection);
        }
        return TypedActionResult.success(player.getMainHandStack());
    }

    private void castRay(World world, PlayerEntity player, Vec3d start, Vec3d direction) {
        if (world instanceof  ServerWorld serverWorld) {
            double maxDistance = 100.0; // Set to desired range
            Vec3d endPos = start.add(direction.multiply(maxDistance));
            BlockHitResult hitResult = world.raycast(new RaycastContext(start, endPos, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player));
            Vec3d hitPos = hitResult.getPos();

            double step = 1;
            Vec3d rayDirection = hitPos.subtract(start).normalize();
            double distance = start.distanceTo(hitPos);

            for (double i = 0; i < distance; i += step) {
                Vec3d particlePos = start.add(rayDirection.multiply(i));
                serverWorld.spawnParticles(ParticleTypes.SONIC_BOOM, particlePos.getX(), particlePos.getY(), particlePos.getZ(), 1, 0.2, 0.2, 0.2, 0.1);
            }

            world.createExplosion(null, hitPos.getX(), hitPos.getY(), hitPos.getZ(), 5, false, World.ExplosionSourceType.TNT);
        }
    }


}
