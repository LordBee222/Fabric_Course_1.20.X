package net.mac.mccourse.util;

import net.mac.mccourse.MCCourseMod;
import net.minecraft.client.particle.FireworksSparkParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class FireworkStarUtil {
    public static TypedActionResult<ItemStack> Explode(World world, double x, double y, double z, ItemStack stack) {
        if (stack.hasNbt() && stack.getNbt().contains("Explosion")) {
            NbtCompound nbtCompound = new NbtCompound();
            NbtList explosions = new NbtList();
            explosions.add(stack.getNbt().getCompound("Explosion"));
            nbtCompound.put("Explosions", explosions);
            NbtCompound explosionNbt = stack.getNbt().getCompound("Explosion");
            Vec3d vec3d = Vec3d.ZERO;
            int invisibilityDuration = 100;
            world.playSound(null, x, y, z, SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.AMBIENT, 3.0f, 1.0f);
            world.addFireworkParticle(x, y, z, vec3d.x, vec3d.y, vec3d.z, nbtCompound);
            world.addFireworkParticle(x, y, z, vec3d.x, vec3d.y, vec3d.z, nbtCompound);
            world.addFireworkParticle(x, y, z, vec3d.x, vec3d.y, vec3d.z, nbtCompound);
            world.addFireworkParticle(x, y, z, vec3d.x, vec3d.y, vec3d.z, nbtCompound);
            world.addFireworkParticle(x, y, z, vec3d.x, vec3d.y, vec3d.z, nbtCompound);
            world.addFireworkParticle(x, y, z, vec3d.x, vec3d.y, vec3d.z, nbtCompound);
            world.addFireworkParticle(x, y, z, vec3d.x, vec3d.y, vec3d.z, nbtCompound);
            world.addFireworkParticle(x, y, z, vec3d.x, vec3d.y, vec3d.z, nbtCompound);
            world.addFireworkParticle(x, y, z, vec3d.x, vec3d.y, vec3d.z, nbtCompound);

            if (explosionNbt.getInt("Type") == 1) {
                invisibilityDuration = 160;
            }

            List<PlayerEntity> players = world.getEntitiesByClass(PlayerEntity.class, new Box(x - 3, y - 3, z - 3, x + 3, y + 3, z + 3), player -> true);
            for (PlayerEntity player : players) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, invisibilityDuration, 0)); // 100 ticks = 5 seconds
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, invisibilityDuration, 0)); // 100 ticks = 5 seconds
            }
            return TypedActionResult.success(stack);
        }
        return TypedActionResult.pass(stack);
    }

    public static void ExplodeDroppedFireworkStar(World world, double x, double y, double z, ItemStack stack){
        if (world.isClient){
            NbtCompound nbtCompound = new NbtCompound();
            NbtList explosions = new NbtList();
            explosions.add(stack.getNbt().getCompound("Explosion"));
            nbtCompound.put("Explosions", explosions);
            Vec3d vec3d = Vec3d.ZERO;

            world.addFireworkParticle(x, y, z, vec3d.x, vec3d.y, vec3d.z, nbtCompound);

        } else if (!world.isClient){
            int invisibilityDuration = 100;
            NbtCompound explosionNbt = stack.getNbt().getCompound("Explosion");

            if (explosionNbt.getInt("Type") == 1) {
                invisibilityDuration = 160;
            }

            List<PlayerEntity> players = world.getEntitiesByClass(PlayerEntity.class, new Box(x - 3, y - 3, z - 3, x + 3, y + 3, z + 3), player -> true);
            for (PlayerEntity player : players) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, invisibilityDuration, 0)); // 100 ticks = 5 seconds
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, invisibilityDuration, 0)); // 100 ticks = 5 seconds
            }
        }
        world.playSound(null, x, y, z, SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.AMBIENT, 3.0f, 1.0f);

    }
}
