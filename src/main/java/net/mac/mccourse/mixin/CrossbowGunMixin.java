package net.mac.mccourse.mixin;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.util.CrossbowUtil;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtInt;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class CrossbowGunMixin {

    @Mixin(CrossbowItem.class)
    public static class MagazineEnchantmentMixin {
        private static final String SHOTS_TAG = "shots";

        @Inject(method = "onStoppedUsing", at = @At("HEAD"))
        private void addShotsTag(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
            if (!world.isClient) {
                    NbtCompound nbt = stack.getOrCreateNbt();
                    if (!CrossbowItem.isCharged(stack)) {
                        nbt.put(SHOTS_TAG, NbtInt.of(5));
                        MCCourseMod.LOGGER.info("ADDED 5 SHOTS TO CROSSBOW");
                        } else {
                            int shots = nbt.getInt(SHOTS_TAG);
                        nbt.putInt(SHOTS_TAG, shots--);
                        MCCourseMod.LOGGER.info("Decremented SHOTS, new shots: " + shots--);
                    }
                }
        }

        @Inject(method = "postShoot", at = @At("HEAD"), cancellable = true)
        private static void postShoot(World world, LivingEntity entity, ItemStack stack, CallbackInfo ci) {
            NbtCompound nbt = stack.getOrCreateNbt();
            if (nbt.contains(SHOTS_TAG)) {
                int shots = nbt.getInt(SHOTS_TAG);
                if (shots > 1) {

                    if (entity instanceof ServerPlayerEntity) {
                        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) entity;
                        shots--;
                        nbt.putInt(SHOTS_TAG, shots);
                        MCCourseMod.LOGGER.info("Decremented SHOTS: " + shots--);
                        if (!world.isClient) {
                            Criteria.SHOT_CROSSBOW.trigger(serverPlayerEntity, stack);
                        }
                        serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                    }
                    // Do not clear projectiles if shots are greater than 0
                    ci.cancel();
                } else {
                    CrossbowUtil.clearProjectiles(stack);
                }
            }
        }

        @Inject(method = "use", at = @At("HEAD"), cancellable = true)
        private void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
            ItemStack itemStack = user.getStackInHand(hand);
            NbtCompound nbt = itemStack.getOrCreateNbt();
            if (CrossbowItem.isCharged(itemStack)) {
                if (nbt.contains(SHOTS_TAG)) {
                    int shots = nbt.getInt(SHOTS_TAG);
                    MCCourseMod.LOGGER.info("GOT SHOTS: " + shots);
                    if (shots >= 1) {
                        CrossbowItem.shootAll(world, user, hand, itemStack, CrossbowUtil.getSpeed(itemStack), 1.0f);
                        // Do not set charged to false if shots are greater than 0
                        cir.setReturnValue(TypedActionResult.consume(itemStack));
                        return;
                    }
                }
                CrossbowItem.setCharged(itemStack, false);
                cir.setReturnValue(TypedActionResult.consume(itemStack));
                return;
            }
            if (!user.getProjectileType(itemStack).isEmpty()) {
                if (!CrossbowItem.isCharged(itemStack)) {
                    user.setCurrentHand(hand);
                }
                cir.setReturnValue(TypedActionResult.consume(itemStack));
                return;
            }
            cir.setReturnValue(TypedActionResult.fail(itemStack));
        }
    }
}
