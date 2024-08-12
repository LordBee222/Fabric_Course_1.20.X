package net.mac.mccourse.util;

import net.mac.mccourse.enchantment.CloudwalkerEnchantment;
import net.mac.mccourse.enchantment.GaleforceEnchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

public class DoubleJumpUtil {
    public static final String jumpCountKey = "jumps";

    // Done
    public static boolean canJump(IPlayerDoubleJumpSaver jumpData, PlayerEntity player) {
        return !player.isOnGround() && hasCloudwalker(player) && hasJumpsLeft(jumpData) && !player.isSwimming();
    }

    // DONE
    public static boolean hasCloudwalker(PlayerEntity player) {
        return CloudwalkerEnchantment.getCloudwalker(player) > 0;
    }

    // DONE
    public static boolean hasJumpsLeft(IPlayerDoubleJumpSaver jumpData) {
        return jumpData.getDoubleJumpData().getInt(jumpCountKey) > 0;
    }

    // DONE
    public static boolean shouldResetJumps(IPlayerDoubleJumpSaver jumpData, PlayerEntity player) {
        int jumpsLeft = jumpData.getDoubleJumpData().getInt(jumpCountKey);
        int maxJumps = CloudwalkerEnchantment.getMaxJumps(player);
        boolean hasMaxJumps = jumpsLeft == maxJumps;
        return hasCloudwalker(player) && !hasMaxJumps && player.isOnGround();
    }

    // DONE
    public static void resetJumps(IPlayerDoubleJumpSaver jumpData, PlayerEntity player) {
        jumpData.getDoubleJumpData().putInt(jumpCountKey, CloudwalkerEnchantment.getMaxJumps(player));
    }

    public static void use(IPlayerDoubleJumpSaver jumpData, PlayerEntity player) {
        int jumpsLeft = jumpData.getDoubleJumpData().getInt(jumpCountKey);

        player.jump();
        player.setVelocity(player.getVelocity().getX(), player.getVelocity().getY() * 1.5, player.getVelocity().getZ());
        jumpsLeft--;

        jumpData.getDoubleJumpData().putInt(jumpCountKey, jumpsLeft);
        player.playSound(SoundEvents.ENTITY_GOAT_LONG_JUMP, 1, 1);
        for (int i = 0; i < 8; i++) {
            player.getWorld().addParticle(ParticleTypes.CLOUD, player.getParticleX(1), player.getY(), player.getParticleZ(1), 0, 0, 0);
        }
    }
}
