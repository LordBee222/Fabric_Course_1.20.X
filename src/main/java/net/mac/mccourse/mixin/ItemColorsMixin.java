package net.mac.mccourse.mixin;

import net.mac.mccourse.item.ModItems;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemColors.class)
public abstract class ItemColorsMixin {
@Inject(method = "create(Lnet/minecraft/client/color/block/BlockColors;)Lnet/minecraft/client/color/item/ItemColors;",
        at = @At("RETURN"))
private static void injectCustomCode(BlockColors blockColors, CallbackInfoReturnable<ItemColors> cir) {
    ItemColors itemColors = cir.getReturnValue();
    itemColors.register((stack, tintIndex) -> tintIndex > 0 ? -1 : PotionUtil.getColor(stack),
            ModItems.CHARGED_POTION, ModItems.CHARGED_SPLASH_POTION, ModItems.CHARGED_LINGERING_POTION);
}
}
