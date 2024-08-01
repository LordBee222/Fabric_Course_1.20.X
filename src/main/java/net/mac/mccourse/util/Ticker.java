package net.mac.mccourse.util;

import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.player.PlayerEntity;

public class Ticker {
    private final ServerWorld world;
    private final PlayerEntity player;
    private int ticksRemaining;

    public Ticker(ServerWorld world, PlayerEntity player, int duration) {
        this.world = world;
        this.player = player;
        this.ticksRemaining = duration;
        world.getServer().getOverworld().getServer().submit(this::tick);
    }

    private void tick() {
        if (ticksRemaining > 0) {
            emitSmoke(world, player);
            ticksRemaining--;
            world.getServer().getOverworld().getServer().submit(this::tick);
        }
    }

    private void emitSmoke(ServerWorld world, PlayerEntity player) {
        Vec3d playerPos = player.getPos();
        world.spawnParticles(ParticleTypes.SMOKE, playerPos.x, playerPos.y, playerPos.z, 1, 0.0, 0.0, 0.0, 0.0);
    }
}
