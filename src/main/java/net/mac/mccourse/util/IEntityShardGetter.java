package net.mac.mccourse.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;

public interface IEntityShardGetter {
    int getStuckShards();
    void setStuckShards(LivingEntity entity, int value);
    int getShardTimer();
    void setShardTimer(int value);
}




