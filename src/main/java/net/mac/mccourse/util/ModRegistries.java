package net.mac.mccourse.util;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.mac.mccourse.command.*;
import net.mac.mccourse.entity.ModEntities;
import net.mac.mccourse.entity.custom.PorcupineEntity;
import net.mac.mccourse.event.AttackEntityHandler;
import net.mac.mccourse.event.PlayerCopyHandler;
import net.mac.mccourse.event.onUseItemHandler;
import net.mac.mccourse.item.ModItems;
import net.mac.mccourse.mixin.BrewingRecipeRegisterMixin;
import net.mac.mccourse.potion.ModPotions;
import net.minecraft.block.ComposterBlock;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;

public class ModRegistries {
    public static void registerModStuffs(){
        registerFuels();
        registerModCompostables();
        registerCommands();
        registerEvents();
        registerPotionRecipes();
        registerPotionTypes();
        registerPotionItemRecipes();
        registerAttributes();
    }

    private static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(ModEntities.PORCUPINE, PorcupineEntity.createPorcupineAttributes());
    }

    private static void registerFuels(){
        FuelRegistry registry = FuelRegistry.INSTANCE;
        registry.add(ModItems.PEAT_BRICK, 200);
    }

    private static void registerModCompostables(){
        ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(ModItems.CAULIFLOWER, 0.5f);
        ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(ModItems.CAULIFLOWER_SEEDS, 0.25f);
    }

    private static void registerCommands(){
        CommandRegistrationCallback.EVENT.register(SetHomeCommand::register);
        CommandRegistrationCallback.EVENT.register(ReturnHomeCommand::register);
        CommandRegistrationCallback.EVENT.register(NbtGetCommand::register);
        CommandRegistrationCallback.EVENT.register(NbtSetCommand::register);
        CommandRegistrationCallback.EVENT.register(DashSetCommand::register);
        CommandRegistrationCallback.EVENT.register(DJSetCommand::register);
    }

    private static void registerEvents(){
        AttackEntityCallback.EVENT.register(new AttackEntityHandler());
        ServerPlayerEvents.COPY_FROM.register(new PlayerCopyHandler());
        UseItemCallback.EVENT.register(new onUseItemHandler());
    }

    private static void registerPotionRecipes(){
        BrewingRecipeRegisterMixin.invokeRegisterPotionRecipe(Potions.AWKWARD, ModItems.PINK_GARNET, ModPotions.TOTEM_SICKNESS_POTION);

        BrewingRecipeRegistry.registerPotionRecipe(ModPotions.SPLASHED$WATER, Items.GLISTERING_MELON_SLICE, ModPotions.SPLASHED$MUNDANE);
        BrewingRecipeRegistry.registerPotionRecipe(ModPotions.SPLASHED$WATER, Items.GHAST_TEAR, ModPotions.SPLASHED$MUNDANE);
        BrewingRecipeRegistry.registerPotionRecipe(ModPotions.SPLASHED$WATER, Items.RABBIT_FOOT, ModPotions.SPLASHED$MUNDANE);
        BrewingRecipeRegistry.registerPotionRecipe(ModPotions.SPLASHED$WATER, Items.BLAZE_POWDER, ModPotions.SPLASHED$MUNDANE);
        BrewingRecipeRegistry.registerPotionRecipe(ModPotions.SPLASHED$WATER, Items.SPIDER_EYE, ModPotions.SPLASHED$MUNDANE);
        BrewingRecipeRegistry.registerPotionRecipe(ModPotions.SPLASHED$WATER, Items.SUGAR, ModPotions.SPLASHED$MUNDANE);
        BrewingRecipeRegistry.registerPotionRecipe(ModPotions.SPLASHED$WATER, Items.MAGMA_CREAM, ModPotions.SPLASHED$MUNDANE);
        BrewingRecipeRegistry.registerPotionRecipe(ModPotions.SPLASHED$WATER, Items.GLOWSTONE_DUST, ModPotions.SPLASHED$THICK);
        BrewingRecipeRegistry.registerPotionRecipe(ModPotions.SPLASHED$WATER, Items.REDSTONE, ModPotions.SPLASHED$MUNDANE);
        BrewingRecipeRegistry.registerPotionRecipe(ModPotions.SPLASHED$WATER, Items.NETHER_WART, ModPotions.SPLASHED$AWKWARD);
        BrewingRecipeRegistry.registerPotionRecipe(ModPotions.SPLASHED$AWKWARD, Items.GOLDEN_CARROT, Potions.NIGHT_VISION);
        BrewingRecipeRegistry.registerPotionRecipe(ModPotions.SPLASHED$AWKWARD, Items.MAGMA_CREAM, Potions.FIRE_RESISTANCE);
        BrewingRecipeRegistry.registerPotionRecipe(ModPotions.SPLASHED$AWKWARD, Items.RABBIT_FOOT, Potions.LEAPING);
        BrewingRecipeRegistry.registerPotionRecipe(ModPotions.SPLASHED$AWKWARD, Items.TURTLE_HELMET, Potions.TURTLE_MASTER);
        BrewingRecipeRegistry.registerPotionRecipe(ModPotions.SPLASHED$AWKWARD, Items.SUGAR, Potions.SWIFTNESS);
        BrewingRecipeRegistry.registerPotionRecipe(ModPotions.SPLASHED$AWKWARD, Items.PUFFERFISH, Potions.WATER_BREATHING);
        BrewingRecipeRegistry.registerPotionRecipe(ModPotions.SPLASHED$AWKWARD, Items.GLISTERING_MELON_SLICE, Potions.HEALING);
        BrewingRecipeRegistry.registerPotionRecipe(ModPotions.SPLASHED$AWKWARD, Items.SPIDER_EYE, Potions.POISON);
        BrewingRecipeRegistry.registerPotionRecipe(ModPotions.SPLASHED$AWKWARD, Items.GHAST_TEAR, Potions.REGENERATION);
        BrewingRecipeRegistry.registerPotionRecipe(ModPotions.SPLASHED$AWKWARD, Items.BLAZE_POWDER, Potions.STRENGTH);
        BrewingRecipeRegistry.registerPotionRecipe(ModPotions.SPLASHED$WATER, Items.FERMENTED_SPIDER_EYE, Potions.WEAKNESS);
        BrewingRecipeRegistry.registerPotionRecipe(ModPotions.SPLASHED$AWKWARD, Items.PHANTOM_MEMBRANE, Potions.SLOW_FALLING);
    }

    private static void registerPotionTypes(){
        //BrewingRecipeRegisterMixin.invokeRegisterPotionType(ModItems.CHARGED_POTION);
        //BrewingRecipeRegisterMixin.invokeRegisterPotionType(ModItems.CHARGED_SPLASH_POTION);
        //BrewingRecipeRegisterMixin.invokeRegisterPotionType(ModItems.CHARGED_LINGERING_POTION);

    }

    private static void registerPotionItemRecipes(){
       // BrewingRecipeRegisterMixin.invokeRegisterItemRecipe(Items.POTION, Items.COPPER_INGOT, ModItems.CHARGED_POTION);
       // BrewingRecipeRegisterMixin.invokeRegisterItemRecipe(Items.SPLASH_POTION, Items.COPPER_INGOT, ModItems.CHARGED_SPLASH_POTION);
       // BrewingRecipeRegisterMixin.invokeRegisterItemRecipe(Items.LINGERING_POTION, Items.COPPER_INGOT, ModItems.CHARGED_LINGERING_POTION);

    }
}
