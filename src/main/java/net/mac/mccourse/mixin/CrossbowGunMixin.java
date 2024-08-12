package net.mac.mccourse.mixin;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.enchantment.ModEnchantments;
import net.mac.mccourse.util.CrossbowUtil;
import net.mac.mccourse.util.Ticker;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtInt;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

public class CrossbowGunMixin {

    /********************************************************************************************************
     /// Sonic Enchantment Mixin
     /// Affected Methods: getSpeed
     /*********************************************************************************************************/
    @Mixin(CrossbowItem.class)
    public static abstract class SonicEnchantmentMixin {
        @Inject(method = "getSpeed", at = @At("HEAD"), cancellable = true)
        private static void getSpeed(ItemStack stack, CallbackInfoReturnable<Float> cir) {
            int level = EnchantmentHelper.getLevel(ModEnchantments.SONIC, stack);
            float speed;
            if (CrossbowItem.hasProjectile(stack, Items.FIREWORK_ROCKET)) {
                speed = 1.6f;
            } else {
                speed = 3.15f;
            }
            speed *= ((1 + level) * 0.5);

            cir.setReturnValue(speed);
        }
    }

    /********************************************************************************************************
     /// Forceful Enchantment Mixin
     /// Affected Methods: getRange
     /*********************************************************************************************************/
    @Mixin(CrossbowItem.class)
    public static abstract class ForcefulEnchantmentMixin {
        @Inject(method = "getRange", at = @At("HEAD"), cancellable = true)
        private void getRange(CallbackInfoReturnable<Integer> cir) {
            CrossbowItem crossbowItem = (CrossbowItem) (Object) this;
            int baseRange = 8;
            ItemStack stack = crossbowItem.getDefaultStack();
            int level = EnchantmentHelper.getLevel(ModEnchantments.FORCEFUL, stack);
            int range = baseRange * (1 + level);
            cir.setReturnValue(range);
        }
    }

    /********************************************************************************************************
     // Magazine Enchantment Mixin
     // Affected Methods: onStoppedUsing, postShoot & use.
     *********************************************************************************************************/
    @Mixin(CrossbowItem.class)
    public static abstract class MagazineEnchantmentMixin {
        private static final String SHOTS_TAG = "shots";

        @Inject(method = "onStoppedUsing", at = @At("HEAD"))
        private void addShotsTag(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
            if (!world.isClient) {
                if (EnchantmentHelper.getLevel(ModEnchantments.COMPACITY, stack) >= 1) {
                    NbtCompound nbt = stack.getOrCreateNbt();
                    int chargedShots;
                    if (EnchantmentHelper.getLevel(ModEnchantments.COMPACITY, stack) >= 1) {
                        int level = EnchantmentHelper.getLevel(ModEnchantments.COMPACITY, stack);
                        chargedShots = (int) (3.5 * level * level - 7.5 * level + 6);
                    } else {
                        chargedShots = 2;
                    }
                    if (!CrossbowItem.isCharged(stack)) {
                        nbt.put(SHOTS_TAG, NbtInt.of(chargedShots));
                        MCCourseMod.LOGGER.info("ADDED 5 SHOTS TO CROSSBOW");
                    } else {
                        int shots = nbt.getInt(SHOTS_TAG);
                        nbt.putInt(SHOTS_TAG, shots--);
                    }
                }
            }
        }

        @Inject(method = "postShoot", at = @At("HEAD"), cancellable = true)
        private static void postShoot(World world, LivingEntity entity, ItemStack stack, CallbackInfo ci) {
            NbtCompound nbt = stack.getOrCreateNbt();
            int cooldownTime;
            if (EnchantmentHelper.getLevel(ModEnchantments.COMPACITY, stack) >= 1) {
                if (EnchantmentHelper.getLevel(ModEnchantments.TRIGGER_HAPPY, stack) >= 1) {
                    int level = EnchantmentHelper.getLevel(ModEnchantments.TRIGGER_HAPPY, stack);
                    cooldownTime = -5 * level + 20;
                } else {
                    cooldownTime = 20;
                }
                if (nbt.contains(SHOTS_TAG)) {
                    int shots = nbt.getInt(SHOTS_TAG);
                    if (shots == 1) {
                        CrossbowUtil.clearProjectiles(stack);
                        CrossbowItem.setCharged(stack, false);
                    }
                    if (shots > 0) {
                        if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
                            shots--;
                            nbt.putInt(SHOTS_TAG, shots);
                            if (!world.isClient) {
                                Criteria.SHOT_CROSSBOW.trigger(serverPlayerEntity, stack);
                            }
                            serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                            serverPlayerEntity.getItemCooldownManager().set(stack.getItem(), cooldownTime);
                        }
                        ci.cancel();
                    } else if (shots == 0) {
                        CrossbowUtil.clearProjectiles(stack);
                    }
                }
            }
        }

        @Inject(method = "use", at = @At("HEAD"), cancellable = true)
        private void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
            ItemStack itemStack = user.getStackInHand(hand);
            NbtCompound nbt = itemStack.getOrCreateNbt();
            if (EnchantmentHelper.getLevel(ModEnchantments.COMPACITY, itemStack) >= 1) {
                if (CrossbowItem.isCharged(itemStack) && !user.getItemCooldownManager().isCoolingDown(itemStack.getItem())) {
                    if (nbt.contains(SHOTS_TAG)) {
                        int shots = nbt.getInt(SHOTS_TAG);
                        if (shots > 0) {
                            CrossbowItem.shootAll(world, user, hand, itemStack, CrossbowUtil.getSpeed(itemStack), 1.0f);
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

    /********************************************************************************************************
     // Recoil & Recoil Control Enchantment Mixin
     // Affected Methods: shoot
     *********************************************************************************************************/

    @Mixin(Item.class)
    public static abstract class CrossbowRecoil {

        @Unique
        private static final String TICKER_KEY = "ticker";

        @Unique
        private static int getTickerTicks(ItemStack stack){
            return stack.getNbt() == null ? 0 : stack.getNbt().getInt(TICKER_KEY);
        }

        @Unique
        private static boolean hasTicker(ItemStack stack){
            return stack.getNbt().contains(TICKER_KEY);
        }

        @Unique
        private static void setTickerTicks(ItemStack stack, int ticks){
            NbtCompound nbtCompound = stack.getOrCreateNbt();
            nbtCompound.putInt(TICKER_KEY, ticks);
        }

        @Inject(method = "inventoryTick", at = @At("HEAD"))
        private void inventoryTicker(ItemStack stack, World world, Entity entity, int slot, boolean selected, CallbackInfo ci){
           if (stack.isOf(Items.CROSSBOW)){
               Ticker ticker = new Ticker(stack);
               ticker.tick();
           }
        }
    }
}




