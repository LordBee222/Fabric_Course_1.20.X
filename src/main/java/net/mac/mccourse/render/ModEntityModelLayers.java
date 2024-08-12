package net.mac.mccourse.render;

import com.google.common.collect.Sets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mac.mccourse.MCCourseMod;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;

import java.util.Set;

@Environment(value= EnvType.CLIENT)
public class ModEntityModelLayers {
    private static final String MAIN = "main";
    private static final Set<EntityModelLayer> LAYERS = Sets.newHashSet();

    public static final EntityModelLayer PLAYER_CHARGED = ModEntityModelLayers.createOuterArmor("player");



    private static EntityModelLayer register(String id, String layer) {
        EntityModelLayer entityModelLayer = ModEntityModelLayers.create(id, layer);
        if (!LAYERS.add(entityModelLayer)) {
            throw new IllegalStateException("Duplicate registration for " + entityModelLayer);
        }
        return entityModelLayer;
    }

    private static EntityModelLayer create(String id, String layer) {
        return new EntityModelLayer(new Identifier("minecraft", id), layer);
    }

    private static EntityModelLayer createOuterArmor(String id) {
        return ModEntityModelLayers.register(id, "outer_armor");
    }
}
