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
import net.mac.mccourse.entity.ModEntities;
import net.mac.mccourse.entity.ai.Activity.ModActivities;
import net.mac.mccourse.entity.ai.ModMemoryModuleTypes;
import net.mac.mccourse.entity.ai.ModSensorTypes;
import net.mac.mccourse.entity.brain.ModActivity;
import net.mac.mccourse.item.KeyBinds.AdsKeybind;
import net.mac.mccourse.item.ModItemGroup;
import net.mac.mccourse.item.ModItems;
import net.mac.mccourse.network.*;
import net.mac.mccourse.potion.ModPotions;
import net.mac.mccourse.sound.ModSounds;
import net.mac.mccourse.util.ModRegistries;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
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
		ModEntities.registerModEntities();
		//DashPacket.registerReceivers();
		//AdsPacket.registerReceivers();
		//DoubleJumpPacket.registerReceivers();
		//DashKeybind.registerKeybind();
		//AdsKeybind.registerKeybind();
		//DoubleJumpKeybind.registerKeybind();
		//SoulSpeakKeybind.registerKeybind();
		//SoulSpeakPacket.register();
		ModActivities.registerModActivities();
		ModMemoryModuleTypes.registerModMemoryModules();
		ModSensorTypes.registerModSensors();
	}

	public static boolean isStoned(@Nullable LivingEntity entity) {
		return entity != null &&
				entity.hasStatusEffect(ModEffects.STONED) &&
				!entity.isSpectator() &&
				!(entity instanceof PlayerEntity player && player.isCreative());
	}
}