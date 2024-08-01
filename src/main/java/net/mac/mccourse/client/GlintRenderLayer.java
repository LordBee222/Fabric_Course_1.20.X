package net.mac.mccourse.client;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mac.mccourse.MCCourseMod;
import net.minecraft.client.render.*;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class GlintRenderLayer extends RenderLayer {
    public static List<RenderLayer> glintColor = newRenderList(GlintRenderLayer::buildGlintRenderLayer);
    public static List<RenderLayer> glintDirectColor = newRenderList(GlintRenderLayer::buildGlintDirectRenderLayer);
    public static List<RenderLayer> translucentGlintColor = newRenderList(GlintRenderLayer::buildTranslucentGlint);
    public static void addGlintTypes(Object2ObjectLinkedOpenHashMap<RenderLayer, BufferBuilder> map) {
        addGlintTypes(map, glintColor);
        addGlintTypes(map, glintDirectColor);
        addGlintTypes(map, translucentGlintColor);
    }
    public GlintRenderLayer(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
    }
    private static List<RenderLayer> newRenderList(Function<String, RenderLayer> func) {
        ArrayList<RenderLayer> list = new ArrayList<>(DyeColor.values().length);
        for (DyeColor color : DyeColor.values())
            list.add(func.apply(color.getName()));
        list.add(func.apply("rainbow"));
        list.add(func.apply("light"));
        list.add(func.apply("none"));
        return list;
    }
    public static void addGlintTypes(Object2ObjectLinkedOpenHashMap<RenderLayer, BufferBuilder> map, List<RenderLayer> typeList) {
        for(RenderLayer renderType : typeList)
            if (!map.containsKey(renderType))
                map.put(renderType, new BufferBuilder(renderType.getExpectedBufferSize()));
    }
    private static RenderLayer buildGlintRenderLayer(String name) {
        //final Identifier res = new Identifier(MCCourseMod.MOD_ID, "textures/misc/glint_" + name + ".png");
        final Identifier charged_glint = new Identifier(MCCourseMod.MOD_ID, "textures/misc/charged_glint.png");


        return RenderLayer.of("glint_" + name, VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.QUADS, 256, MultiPhaseParameters.builder()
                .program(RenderPhase.GLINT_PROGRAM)
                .texture(new Texture(charged_glint, true, false))
                .writeMaskState(COLOR_MASK)
                .cull(DISABLE_CULLING)
                .depthTest(EQUAL_DEPTH_TEST)
                .transparency(GLINT_TRANSPARENCY)
                .texturing(GLINT_TEXTURING)
                .build(false));
    }

    private static RenderLayer buildGlintDirectRenderLayer(String name) {
        //final Identifier res = new Identifier(MCCourseMod.MOD_ID, "textures/misc/glint_" + name + ".png");
        final Identifier charged_glint = new Identifier(MCCourseMod.MOD_ID, "textures/misc/charged_glint.png");

        return RenderLayer.of("glint_direct_" + name, VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.QUADS, 256, MultiPhaseParameters.builder()
                .program(RenderPhase.DIRECT_GLINT_PROGRAM)
                .texture(new Texture(charged_glint, true, false))
                .writeMaskState(COLOR_MASK)
                .cull(DISABLE_CULLING)
                .depthTest(EQUAL_DEPTH_TEST)
                .transparency(GLINT_TRANSPARENCY)
                .texturing(GLINT_TEXTURING)
                .build(false));
    }

    private static RenderLayer buildTranslucentGlint(String name) {
        final Identifier res = new Identifier(MCCourseMod.MOD_ID, "textures/misc/glint_" + name + ".png");
        return RenderLayer.of("glint_translucent_" + name, VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.QUADS, 256, RenderLayer.MultiPhaseParameters.builder()
                .program(TRANSLUCENT_GLINT_PROGRAM)
                .texture(new Texture(res, true, false))
                .writeMaskState(COLOR_MASK)
                .cull(DISABLE_CULLING)
                .depthTest(EQUAL_DEPTH_TEST)
                .transparency(GLINT_TRANSPARENCY)
                .texturing(GLINT_TEXTURING)
                //.target(IT)
                .target(ITEM_ENTITY_TARGET)
                .build(false));
    }

}
