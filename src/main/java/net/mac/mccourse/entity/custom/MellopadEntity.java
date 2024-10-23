package net.mac.mccourse.entity.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MellopadEntity extends Entity {
    public MellopadEntity(EntityType<?> type, World world) {
        super(type, world);
    }


    public boolean isPlayerTouchingEntity(PlayerEntity player) {
        // Get the bounding boxes
        Box playerBoundingBox = player.getBoundingBox();
        Box entityBoundingBox = this.getBoundingBox();

        // Check for intersection
        return playerBoundingBox.intersects(entityBoundingBox);
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }
}
