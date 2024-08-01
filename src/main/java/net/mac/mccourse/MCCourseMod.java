package net.mac.mccourse;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;

import net.mac.mccourse.block.ModBlocks;
import net.mac.mccourse.block.custom.PotionCauldronBehavior;
import net.mac.mccourse.client.GlintRenderLayer;
import net.mac.mccourse.effect.ModEffects;
import net.mac.mccourse.effect.TotemSicknessEffect;
import net.mac.mccourse.enchantment.ModEnchantments;
import net.mac.mccourse.item.ModItemGroup;
import net.mac.mccourse.item.ModItems;
import net.mac.mccourse.network.ExplodePacket;
import net.mac.mccourse.potion.ModPotions;
import net.mac.mccourse.sound.ModSounds;
import net.mac.mccourse.util.ModRegistries;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MCCourseMod implements ModInitializer {
	public static final String MOD_ID = "mccourse";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static boolean zoom = false;
	public static double zoomMultiplayer = 1.0F;
	public static String chargedPotionKey = "charged";

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModItemGroup.registerItemGroups();
		ModRegistries.registerModStuffs();
		ModEnchantments.registerModEnchantments();
		PotionCauldronBehavior.registerBehaviour();
		ModSounds.registerSounds();
		ExplodePacket.register();
		ModEffects.registerEffects();
		ModPotions.registerPotions();
		LOGGER.info("Hello Fabric world!");
	}
}