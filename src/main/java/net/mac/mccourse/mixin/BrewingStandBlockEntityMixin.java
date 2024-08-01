package net.mac.mccourse.mixin;

import net.mac.mccourse.item.ModItems;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrewingStandBlockEntity.class)
public abstract class BrewingStandBlockEntityMixin {

    @Inject(method = "isValid", at = @At("HEAD"), cancellable = true)
    private void injectIsValid(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        BrewingStandBlockEntity brewingStand = (BrewingStandBlockEntity) (Object) this;

        if (slot == 3) {
            if (BrewingRecipeRegistry.isValidIngredient(stack)) {
                cir.setReturnValue(true);
                return;
            }
        } else if (slot == 4) {
            if (stack.isOf(Items.BLAZE_POWDER)) {
                cir.setReturnValue(true);
                return;
            }
        } else if ((stack.isOf(Items.POTION) || stack.isOf(Items.SPLASH_POTION) || stack.isOf(Items.LINGERING_POTION) || stack.isOf(Items.GLASS_BOTTLE) ||
                stack.isOf(ModItems.CHARGED_POTION) || stack.isOf(ModItems.CHARGED_SPLASH_POTION) || stack.isOf(ModItems.CHARGED_LINGERING_POTION)) &&
                brewingStand.getStack(slot).isEmpty()) {
            cir.setReturnValue(true);
            return;
        }
        cir.setReturnValue(false);
    }
}
