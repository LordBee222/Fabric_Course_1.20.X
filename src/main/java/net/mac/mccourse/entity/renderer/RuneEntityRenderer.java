package net.mac.mccourse.entity.renderer;

import net.mac.mccourse.entity.custom.CustomEndCrystalEntity;
import net.mac.mccourse.entity.custom.PorcupineEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

public class RuneEntityRenderer extends EntityRenderer<CustomEndCrystalEntity> {
    private static final TrackedData<String> RUNE_CHAR = DataTracker.registerData(CustomEndCrystalEntity.class, TrackedDataHandlerRegistry.STRING);


    private static final Identifier
            TEXTURE_A = new Identifier("textures/entity/rune/rune_a.png"),
            TEXTURE_B = new Identifier("textures/entity/rune/rune_b.png"),
            TEXTURE_C = new Identifier("textures/entity/rune/rune_c.png"),
            TEXTURE_D = new Identifier("textures/entity/rune/rune_d.png"),
            TEXTURE_E = new Identifier("textures/entity/rune/rune_e.png"),
            TEXTURE_F = new Identifier("textures/entity/rune/rune_f.png"),
            TEXTURE_G = new Identifier("textures/entity/rune/rune_g.png"),
            TEXTURE_H = new Identifier("textures/entity/rune/rune_h.png"),
            TEXTURE_I = new Identifier("textures/entity/rune/rune_i.png"),
            TEXTURE_J = new Identifier("textures/entity/rune/rune_j.png"),
            TEXTURE_K = new Identifier("textures/entity/rune/rune_k.png"),
            TEXTURE_L = new Identifier("textures/entity/rune/rune_l.png"),
            TEXTURE_M = new Identifier("textures/entity/rune/rune_m.png"),
            TEXTURE_N = new Identifier("textures/entity/rune/rune_n.png"),
            TEXTURE_O = new Identifier("textures/entity/rune/rune_o.png"),
            TEXTURE_P = new Identifier("textures/entity/rune/rune_p.png"),
            TEXTURE_Q = new Identifier("textures/entity/rune/rune_q.png"),
            TEXTURE_R = new Identifier("textures/entity/rune/rune_r.png"),
            TEXTURE_S = new Identifier("textures/entity/rune/rune_s.png"),
            TEXTURE_T = new Identifier("textures/entity/rune/rune_t.png"),
            TEXTURE_U = new Identifier("textures/entity/rune/rune_u.png"),
            TEXTURE_V = new Identifier("textures/entity/rune/rune_v.png"),
            TEXTURE_W = new Identifier("textures/entity/rune/rune_w.png"),
            TEXTURE_X = new Identifier("textures/entity/rune/rune_x.png"),
            TEXTURE_Y = new Identifier("textures/entity/rune/rune_y.png"),
            TEXTURE_Z = new Identifier("textures/entity/rune/rune_z.png");


    public RuneEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(CustomEndCrystalEntity entity) {
        return getTexture(entity);
    }

    @Nullable
    private static Identifier getRuneTexture(CustomEndCrystalEntity entity) {
        String character = entity.getDataTracker().get(RUNE_CHAR);
        switch (character) {
            case "a":
                return TEXTURE_A;
            case "b":
                return TEXTURE_B;
            case "c":
                return TEXTURE_C;
            default:
                return null;
        }
    }

    @Override
    public void render(CustomEndCrystalEntity endCrystalEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {

    }


    @Override
    public boolean shouldRender(CustomEndCrystalEntity endCrystalEntity, Frustum frustum, double d, double e, double f) {
        return super.shouldRender(endCrystalEntity, frustum, d, e, f);
    }

}
