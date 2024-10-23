package net.mac.mccourse.entity.layer;

import net.mac.mccourse.MCCourseMod;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class ModModelLayers {
    public static final EntityModelLayer MELLOWPAD =
            new EntityModelLayer(new Identifier(MCCourseMod.MOD_ID, "mellowpad"), "main");

    public static final EntityModelLayer PORCUPINE =
            new EntityModelLayer(new Identifier(MCCourseMod.MOD_ID, "porcupine"), "main");
}
