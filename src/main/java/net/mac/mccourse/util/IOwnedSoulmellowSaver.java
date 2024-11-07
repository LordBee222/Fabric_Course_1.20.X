package net.mac.mccourse.util;

import net.mac.mccourse.entity.custom.PorcupineEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.UUID;

public interface IOwnedSoulmellowSaver {
    UUID getOwnedSoulmellowId();
    void setSoulmellowOwner(PorcupineEntity porcupine, LivingEntity owner);
}
