package net.mac.mccourse.util;

import net.mac.mccourse.MCCourseMod;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static class Blocks{
        public static final TagKey<Block> METAL_DETECTOR_DETECTABLE_BLOCKS =
                createBlockTag("metal_detector_detectable_blocks");

        public static final TagKey<Block> PAXEL_MINABLE =
                createBlockTag("minable/paxel");

        private static TagKey<Block> createBlockTag(String name){
            return TagKey.of(RegistryKeys.BLOCK, new Identifier(MCCourseMod.MOD_ID, name));
        }
        private static TagKey<Block> createCommonBlockTag(String name){
            return TagKey.of(RegistryKeys.BLOCK, new Identifier("c", name));
        }
    }


    public static class Items{
        public static final TagKey<Item> SLINGER_PROJECTILES =
                createItemTag("slinger_projectiles");

        private static TagKey<Item> createItemTag(String name){
            return TagKey.of(RegistryKeys.ITEM, new Identifier(MCCourseMod.MOD_ID, name));
        }
        private static TagKey<Item> createCommonItemTag(String name){
            return TagKey.of(RegistryKeys.ITEM, new Identifier("c", name));
        }
    }
}
