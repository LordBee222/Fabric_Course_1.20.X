package net.mac.mccourse.entity.ai;

import net.mac.mccourse.MCCourseMod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ModMemoryModuleTypes {


    public static final MemoryModuleType<Boolean> ATTACK_ON_COOLDOWN = register("attack_on_cooldown");
    public static final MemoryModuleType<Boolean> UNLEASHED_SOULS_COOLDOWN = register("unleashed_souls_cooldown");
    public static final MemoryModuleType<Boolean> FIREBALL_COOLDOWN = register("fireball_cooldown");
    public static final MemoryModuleType<Boolean> POSSESSION_COOLDOWN = register("possession_cooldown");
    public static final MemoryModuleType<Boolean> CALL_OF_THE_DEAD_COOLDOWN = register("call_of_the_dead_cooldown");

    public static final MemoryModuleType<Integer> POSSESSION_WAVES_FIRED = register("possession_waves_fired");
    public static final MemoryModuleType<Boolean> POSSESSION_TIME_OUT = register("possession_time_out");
    public static final MemoryModuleType<Integer> LIFEFORCE_TIME = register("lifeforce_time");

    public static final MemoryModuleType<Boolean> DAMAGE_LINK_COOLDOWN = register("damage_link_cooldown");
    public static final MemoryModuleType<Integer> DAMAGE_LINK_TICKS = register("damage_link_ticks");

    public static final MemoryModuleType<PorcupineUtil.PorcupineStages> STAGE = register("stage");
    public static final MemoryModuleType<PlayerEntity> OWNER = register("owner");
    public static final MemoryModuleType<Float> REDIRECTED_DAMAGE_TAKEN = register("redirected_damage_taken");
    public static final MemoryModuleType<Boolean> PROVOKED = register("provoked");
    public static final MemoryModuleType<Boolean> REDIRECT_DAMAGE_COOLDOWN = register("redirect_damage_cooldown");


    private static <U> MemoryModuleType<U> register(String id) {
        return Registry.register(Registries.MEMORY_MODULE_TYPE, new Identifier(id), new MemoryModuleType<U>(Optional.empty()));
    }

    public static void registerModMemoryModules(){
        MCCourseMod.LOGGER.info("Registering Mod Memory Modules for " + MCCourseMod.MOD_ID);
    }
}
