package net.mac.mccourse.entity.ai.Activity;

import net.mac.mccourse.MCCourseMod;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModActivities {
    public static final Activity FIREBALL = register("fireball");

    private static Activity register(String key) {
        return Registry.register(Registries.ACTIVITY, key, new Activity(key));
    }

    public static void registerModActivities(){
        MCCourseMod.LOGGER.info("Registering Mod Activities for " + MCCourseMod.MOD_ID);
    }
}
