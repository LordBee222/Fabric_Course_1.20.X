package net.mac.mccourse;

import net.fabricmc.api.ModInitializer;

import net.mac.mccourse.block.ModBlocks;
import net.mac.mccourse.item.ModItemGroup;
import net.mac.mccourse.item.ModItems;
import net.mac.mccourse.util.ModRegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MCCourseMod implements ModInitializer {
	public static final String MOD_ID = "mccourse";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModItemGroup.registerItemGroups();
		ModRegistries.registerModStuffs();
		LOGGER.info("Hello Fabric world!");
	}
}