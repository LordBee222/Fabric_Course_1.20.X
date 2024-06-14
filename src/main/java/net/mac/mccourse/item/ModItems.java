package net.mac.mccourse.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.item.custom.BoomHoneyItem;
import net.mac.mccourse.item.custom.DynamiteItem;
import net.mac.mccourse.item.custom.LavaRodItem;
import net.mac.mccourse.item.custom.MetalDetectorItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item PINK_GARNET = registerItem("pink_garnet",
            new Item(new FabricItemSettings()));

    public static final Item RAW_PINK_GARNET = registerItem("raw_pink_garnet",
            new Item(new FabricItemSettings().fireproof().maxCount(16)));

    public static final Item METAL_DETECTOR = registerItem("metal_detector",
            new MetalDetectorItem(new FabricItemSettings().maxCount(1).maxDamage(256)));


    public static final Item CAULIFLOWER = registerItem("cauliflower",
            new Item(new FabricItemSettings().food(ModFoodComponents.CAULIFLOWER)));

    public static final Item PEAT_BRICK = registerItem("peat_brick",
            new Item(new FabricItemSettings()));

    public static final Item DYNAMITE = registerItem("dynamite",
            new DynamiteItem(new FabricItemSettings().maxCount(16)));

    public static final Item BOOM_SLIME = registerItem("boom_slime",
            new LavaRodItem(new FabricItemSettings().fireproof().maxCount(16)));

    public static final Item BOOM_HONEY = registerItem("boom_honey",
            new BoomHoneyItem(new FabricItemSettings().maxCount(16)));



    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, new Identifier(MCCourseMod.MOD_ID, name), item);
    }

    public static void registerModItems(){
        MCCourseMod.LOGGER.info("Registering Mod Items for " + MCCourseMod.MOD_ID);
    }
}
