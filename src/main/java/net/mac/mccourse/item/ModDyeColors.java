package net.mac.mccourse.item;

import net.minecraft.block.MapColor;
import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.Nullable;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

public enum ModDyeColors implements StringIdentifiable {
    HAZEL(16, "hazel", 16351261, MapColor.BROWN, 16351261, 16351261);

    private static final IntFunction<ModDyeColors> BY_ID = value -> Arrays.stream(values()).filter(color -> color.id == value).findFirst().orElse(null);
    private static final Int2ObjectOpenHashMap<ModDyeColors> BY_FIREWORK_COLOR = new Int2ObjectOpenHashMap<>(Arrays.stream(values()).collect(Collectors.toMap(color -> color.fireworkColor, color -> color)));
    private final int id;
    private final String name;
    private final MapColor mapColor;
    private final float[] colorComponents;
    private final int fireworkColor;
    private final int signColor;
    private final int color;

    ModDyeColors(int id, String name, int color, MapColor mapColor, int fireworkColor, int signColor) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.mapColor = mapColor;
        this.fireworkColor = fireworkColor;
        this.signColor = signColor;
        int r = (color & 0xFF0000) >> 16;
        int g = (color & 0x00FF00) >> 8;
        int b = (color & 0x0000FF);
        this.colorComponents = new float[]{(float) r / 255.0f, (float) g / 255.0f, (float) b / 255.0f};
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public float[] getColorComponents() {
        return this.colorComponents;
    }

    public MapColor getMapColor() {
        return this.mapColor;
    }

    public int getFireworkColor() {
        return this.fireworkColor;
    }

    public int getSignColor() {
        return this.signColor;
    }

    @Nullable
    public static ModDyeColors byId(int id) {
        return BY_ID.apply(id);
    }

    @Nullable
    public static ModDyeColors byName(String name) {
        for (ModDyeColors color : values()) {
            if (color.name.equals(name)) {
                return color;
            }
        }
        return null;
    }

    @Nullable
    public static ModDyeColors byFireworkColor(int color) {
        return BY_FIREWORK_COLOR.get(color);
    }

    @Override
    public String asString() {
        return this.name;
    }
}
