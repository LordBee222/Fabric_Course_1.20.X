package net.mac.mccourse.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class NbtRecipe extends ShapelessRecipe {

    public NbtRecipe(Identifier id, String group, CraftingRecipeCategory category, ItemStack output, DefaultedList<Ingredient> input) {
        super(id, group, category, output, input);
    }
}
