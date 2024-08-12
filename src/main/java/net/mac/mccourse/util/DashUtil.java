package net.mac.mccourse.util;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.enchantment.GaleforceEnchantment;
import net.mac.mccourse.network.DashPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class DashUtil {
    public static final String hasDashedKey = "hasDashed";

    public static boolean canDash(IPlayerDashSaver dashSaver, PlayerEntity player) {
        return !player.isOnGround() && hasDash(player) && !hasUsedDash(dashSaver) && !player.isSwimming();
    }

    public static boolean hasDash(PlayerEntity player) {
        return GaleforceEnchantment.getGaleforce(player) > 0;
    }

    public static boolean hasUsedDash(IPlayerDashSaver dashSaver) {
        return dashSaver.getDashData().getBoolean(hasDashedKey);
    }

    public static boolean shouldResetDashes(IPlayerDashSaver dashData, PlayerEntity player) {
        boolean hasDashed = dashData.getDashData().getBoolean(hasDashedKey);
        return hasDash(player) && hasDashed && player.isOnGround();
    }

    public static void resetDashes(IPlayerDashSaver dashData, PlayerEntity player) {
        dashData.getDashData().putBoolean(hasDashedKey, false);
    }

    public static void use(IPlayerDashSaver dashSaver, PlayerEntity player) {
        dashSaver.getDashData().putBoolean(hasDashedKey, true);

        //DashPacket.sendDashPacket();
        Vec3d velocity = player.getRotationVec(1.0F).normalize();
        player.setVelocity(velocity.getX() , velocity.getY(), velocity.getZ());
        player.fallDistance = 0;
        player.playSound(SoundEvents.ENTITY_GOAT_LONG_JUMP, 1, 1);
        for (int i = 0; i < 8; i++) {
            player.getWorld().addParticle(ParticleTypes.CLOUD, player.getParticleX(1), player.getRandomBodyY(), player.getParticleZ(1), 0, 0, 0);
        }
    }
}
