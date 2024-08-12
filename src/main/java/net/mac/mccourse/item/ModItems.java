package net.mac.mccourse.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.block.ModBlocks;
import net.mac.mccourse.item.custom.*;
import net.minecraft.item.*;
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


    public static final Item PINK_GARNET_SWORD = registerItem("pink_garnet_sword",
            new ModPoisonSwordItem(ModToolMaterial.PINK_GARNET, 2, 2, new FabricItemSettings()));

    public static final Item PINK_GARNET_PICKAXE = registerItem("pink_garnet_pickaxe",
            new PickaxeItem(ModToolMaterial.PINK_GARNET, 2, 2, new FabricItemSettings()));

    public static final Item PINK_GARNET_AXE = registerItem("pink_garnet_axe",
            new AxeItem(ModToolMaterial.PINK_GARNET, 2, 2, new FabricItemSettings()));

    public static final Item PINK_GARNET_SHOVEL = registerItem("pink_garnet_shovel",
            new ShovelItem(ModToolMaterial.PINK_GARNET, 2, 2, new FabricItemSettings()));

    public static final Item PINK_GARNET_HOE = registerItem("pink_garnet_hoe",
            new HoeItem(ModToolMaterial.PINK_GARNET, 2, 2,new FabricItemSettings()));

    public static final Item PINK_GARNET_PAXEL = registerItem("pink_garnet_paxel",
            new PaxelItem(ModToolMaterial.PINK_GARNET, 2, 2,new FabricItemSettings()));

    public static final Item PINK_GARNET_HELMET = registerItem("pink_garnet_helmet",
            new ModArmorItem(ModArmorMaterials.PINK_GARNET, ArmorItem.Type.HELMET, new FabricItemSettings()));
    public static final Item PINK_GARNET_CHESTPLATE = registerItem("pink_garnet_chestplate",
            new ModArmorItem(ModArmorMaterials.PINK_GARNET, ArmorItem.Type.CHESTPLATE,  new FabricItemSettings()));
    public static final Item PINK_GARNET_LEGGINGS = registerItem("pink_garnet_leggings",
            new ModArmorItem(ModArmorMaterials.PINK_GARNET, ArmorItem.Type.LEGGINGS,  new FabricItemSettings()));
    public static final Item PINK_GARNET_BOOTS = registerItem("pink_garnet_boots",
            new ModArmorItem(ModArmorMaterials.PINK_GARNET, ArmorItem.Type.BOOTS,  new FabricItemSettings()));

    public static final Item PINK_GARNET_HORSE_ARMOR = registerItem("pink_garnet_horse_armor",
            new HorseArmorItem(14, "pink_garnet",  new FabricItemSettings()));

    public static final Item DATA_TABLET = registerItem("data_tablet",
            new DataTabletItem(new FabricItemSettings().maxCount(1)));

    public static final Item CAULIFLOWER_SEEDS = registerItem("cauliflower_seeds",
            new AliasedBlockItem(ModBlocks.CAULIFLOWER_CROP, new FabricItemSettings()));

    public static final Item POWDERED_POTION = registerItem("powdered_potion",
            new PowderedPotionItem(new FabricItemSettings()));

    public static final Item SCOPED_CROSSBOW = registerItem("scoped_crossbow",
            new ScopedCrossbowItem(new FabricItemSettings().maxCount(1)));

    public static final Item SLINGER = registerItem("slinger",
            new SlingerItem(new FabricItemSettings().maxCount(1)));

    public static final Item DICE = registerItem("dice",
            new DiceItem(new FabricItemSettings()));

    public static final Item DYNAMITE = registerItem("dynamite",
            new DynamiteItem(new FabricItemSettings().maxCount(16)));

    public static final Item BOOM_SLIME = registerItem("boom_slime",
            new BoomSlimeItem(new FabricItemSettings().maxCount(16)));

    public static final Item CEREMONIAL_BLADE = registerItem("ceremonial_blade",
            new CeremonialBladeItem(ToolMaterials.NETHERITE, -2, 2, new FabricItemSettings()));


    /*
    public static final Item CHARGED_POTION = registerItem("charged_potion",
            new ChargedPotionItem(new FabricItemSettings().maxCount(1)));

    public static final Item CHARGED_SPLASH_POTION = registerItem("charged_splash_potion",
            new ChargedSplashPotionItem(new FabricItemSettings().maxCount(1)));

    public static final Item CHARGED_LINGERING_POTION = registerItem("charged_lingering_potion",
            new ChargedLingeringPotionItem(new FabricItemSettings().maxCount(1)));

     */

    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, new Identifier(MCCourseMod.MOD_ID, name), item);
    }

    public static void registerModItems(){
        MCCourseMod.LOGGER.info("Registering Mod Items for " + MCCourseMod.MOD_ID);
    }
}
