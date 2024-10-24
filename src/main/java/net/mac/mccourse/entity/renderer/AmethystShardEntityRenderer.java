package net.mac.mccourse.entity.renderer;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.entity.custom.AmethystShardEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

public class AmethystShardEntityRenderer extends ProjectileEntityRenderer<AmethystShardEntity> {
    public static final Identifier TEXTURE = new Identifier(MCCourseMod.MOD_ID, "textures/entity/projectiles/amethyst_shard.png");

    public AmethystShardEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }



    public Identifier getTexture(AmethystShardEntity amethystShardEntity) {
        return TEXTURE;
    }
}
