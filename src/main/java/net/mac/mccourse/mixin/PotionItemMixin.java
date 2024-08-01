package net.mac.mccourse.mixin;

import net.mac.mccourse.MCCourseMod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionItem.class)
public abstract class PotionItemMixin {

    @Inject(method = "getMaxUseTime", at = @At("HEAD"), cancellable = true)
    private void getMaxUseTimeCharged(ItemStack stack, CallbackInfoReturnable<Integer> cir){
        if (stack.getNbt().contains(MCCourseMod.chargedPotionKey)){
            cir.setReturnValue(15);
        } else {
            cir.setReturnValue(32);
        }
    }

    @Inject(method = "getTranslationKey", at = @At("HEAD"), cancellable = true)
    private void getTranslationKey(ItemStack stack, CallbackInfoReturnable<String> cir) {
        if (stack.getNbt() != null && stack.getNbt().contains(MCCourseMod.chargedPotionKey)) {
            String effectKey = "item.mccourse.charged_potion.effect.";
            String finalKey = PotionUtil.getPotion(stack).finishTranslationKey(effectKey);
            cir.setReturnValue(finalKey);
        }
    }

    @Redirect(method = "finishUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z"))
    private boolean chargedFinishedUsing(LivingEntity instance, StatusEffectInstance effect){
        Hand hand = instance.getActiveHand();
        ItemStack stack = instance.getStackInHand(hand);
        if (stack.getNbt().contains(MCCourseMod.chargedPotionKey)){
            effect = new StatusEffectInstance(effect.getEffectType(), effect.getDuration(), effect.getAmplifier() + 1);
            instance.addStatusEffect(new StatusEffectInstance(effect.getEffectType(), effect.getDuration(), effect.getAmplifier() + 1));
        }
        return instance.addStatusEffect(effect);
    }
}
