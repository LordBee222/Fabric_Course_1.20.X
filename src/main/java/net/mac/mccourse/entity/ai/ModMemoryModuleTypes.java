package net.mac.mccourse.entity.ai;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.entity.custom.PorcupineEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.mob.HoglinEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Optional;

public class ModMemoryModuleTypes {


    public static final MemoryModuleType<Boolean> ATTACK_ON_COOLDOWN = register("attack_on_cooldown");
    public static final MemoryModuleType<Boolean> UNLEASHED_SOULS_COOLDOWN = register("unleashed_souls_cooldown");
    public static final MemoryModuleType<Boolean> FIREBALL_COOLDOWN = register("fireball_cooldown");
    public static final MemoryModuleType<Boolean> POSSESSION_COOLDOWN = register("possession_cooldown");





    private static <U> MemoryModuleType<U> register(String id) {
        return Registry.register(Registries.MEMORY_MODULE_TYPE, new Identifier(id), new MemoryModuleType<U>(Optional.empty()));
    }

    public static void registerModMemoryModules(){
        MCCourseMod.LOGGER.info("Registering Mod Memory Modules for " + MCCourseMod.MOD_ID);
    }
}
