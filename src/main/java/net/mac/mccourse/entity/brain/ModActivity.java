package net.mac.mccourse.entity.brain;

import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModActivity {
    public static final Activity ENRAGED = ModActivity.register("enraged");

    private static Activity register(String id) {
        return Registry.register(Registries.ACTIVITY, id, new Activity(id));
    }

}
