package net.mac.mccourse.mixin;

import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(BrewingRecipeRegistry.class)
public interface BrewingRecipeRegisterMixin {
    @Invoker("registerPotionRecipe")
    static void invokeRegisterPotionRecipe(Potion input, Item item, Potion output){
        throw new AssertionError();
    }

    @Invoker("registerPotionType")
    static void invokeRegisterPotionType(Item item){
        throw new AssertionError();
    }

    @Invoker("registerItemRecipe")
    static void invokeRegisterItemRecipe(Item input, Item ingredient, Item output){
        throw new AssertionError();
    }
}

