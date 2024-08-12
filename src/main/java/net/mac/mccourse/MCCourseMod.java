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
import net.mac.mccourse.item.ModItemGroup;
import net.mac.mccourse.item.ModItems;
import net.mac.mccourse.network.DashPacket;
import net.mac.mccourse.network.DoubleJumpPacket;
import net.mac.mccourse.network.ExplodePacket;
import net.mac.mccourse.potion.ModPotions;
import net.mac.mccourse.sound.ModSounds;
import net.mac.mccourse.util.ModRegistries;
import net.minecraft.client.render.RenderLayer;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MCCourseMod implements ModInitializer {
	public static final String MOD_ID = "mccourse";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static boolean zoom = false;
	public static double zoomMultiplayer = 1.0F;
	public static String chargedPotionKey = "charged";
	public static final TrackedData<Boolean> DOUBLE_JUMP_ATTRIBUTE = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

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
		DashPacket.registerReceivers();
		DoubleJumpPacket.registerReceivers();
		DashKeybind.registerKeybind();
		DoubleJumpKeybind.registerKeybind();
	}

	public static boolean hasDoubleJumped(PlayerEntity player) {
		return player.getDataTracker().get(DOUBLE_JUMP_ATTRIBUTE);
	}

	public static void setDoubleJumped(PlayerEntity player, boolean hasJumped) {
		player.getDataTracker().set(DOUBLE_JUMP_ATTRIBUTE, hasJumped);
	}
}