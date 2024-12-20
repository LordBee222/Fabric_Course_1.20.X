package net.mac.mccourse.world;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

public class DestabilisingExplosion extends Explosion {
    public DestabilisingExplosion(World world, @Nullable Entity entity, double x, double y, double z, float power, boolean createFire, DestructionType destructionType) {
        super(world, entity, x, y, z, power, createFire, destructionType);
    }
}
