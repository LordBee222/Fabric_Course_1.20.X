package net.mac.mccourse.util;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.mac.mccourse.command.NbtGetCommand;
import net.mac.mccourse.command.NbtSetCommand;
import net.mac.mccourse.command.ReturnHomeCommand;
import net.mac.mccourse.command.SetHomeCommand;
import net.mac.mccourse.event.AttackEntityHandler;
import net.mac.mccourse.event.PlayerCopyHandler;
import net.mac.mccourse.event.onUseItemHandler;
import net.mac.mccourse.item.ModItems;
import net.mac.mccourse.mixin.BrewingRecipeRegisterMixin;
import net.mac.mccourse.potion.ModPotions;
import net.minecraft.block.ComposterBlock;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;

public class ModRegistries {
    public static void registerModStuffs(){
        registerFuels();
        registerModCompostables();
        registerCommands();
        registerEvents();
        registerPotionRecipes();
        registerPotionTypes();
        registerPotionItemRecipes();
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

    }

    private static void registerEvents(){
        AttackEntityCallback.EVENT.register(new AttackEntityHandler());
        ServerPlayerEvents.COPY_FROM.register(new PlayerCopyHandler());
        UseItemCallback.EVENT.register(new onUseItemHandler());
    }

    private static void registerPotionRecipes(){
        BrewingRecipeRegisterMixin.invokeRegisterPotionRecipe(Potions.AWKWARD, ModItems.PINK_GARNET, ModPotions.TOTEM_SICKNESS_POTION);
    }

    private static void registerPotionTypes(){
        BrewingRecipeRegisterMixin.invokeRegisterPotionType(ModItems.CHARGED_POTION);
        BrewingRecipeRegisterMixin.invokeRegisterPotionType(ModItems.CHARGED_SPLASH_POTION);
        BrewingRecipeRegisterMixin.invokeRegisterPotionType(ModItems.CHARGED_LINGERING_POTION);

    }

    private static void registerPotionItemRecipes(){
        BrewingRecipeRegisterMixin.invokeRegisterItemRecipe(Items.POTION, Items.COPPER_INGOT, ModItems.CHARGED_POTION);
        BrewingRecipeRegisterMixin.invokeRegisterItemRecipe(Items.SPLASH_POTION, Items.COPPER_INGOT, ModItems.CHARGED_SPLASH_POTION);
        BrewingRecipeRegisterMixin.invokeRegisterItemRecipe(Items.LINGERING_POTION, Items.COPPER_INGOT, ModItems.CHARGED_LINGERING_POTION);

    }
}
