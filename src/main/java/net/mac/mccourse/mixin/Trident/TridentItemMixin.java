package net.mac.mccourse.mixin.Trident;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.effect.ModEffects;
import net.mac.mccourse.enchantment.trident.LightweightEnchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

public class TridentItemMixin {

    /********************************************************************************************************
     /// OverrideTridentOnStoppedUsing
     /// Purpose: Blocks Vanilla Effect Particles For Soaked
     /// Affected Class: TridentItem
     /// Affected Methods: onStoppedUsing
     /*********************************************************************************************************/
    @Mixin(TridentItem.class)
    public static abstract class OverrideTridentOnStoppedUsing {
        /**
         * @author LordBee222
         * @reason Override Trident Behavior
         */
        @Overwrite
        public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
            if (!(user instanceof PlayerEntity)) {
                return;
            }
            PlayerEntity playerEntity = (PlayerEntity) user;
            int i = stack.getMaxUseTime() - remainingUseTicks;
            int lightweightLevel = LightweightEnchantment.getLevel(stack);
            if (LightweightEnchantment.hasLightweight(stack)) {
                if (i < LightweightEnchantment.getLightweightChargeTicks(lightweightLevel)) {
                    return;
                }
            } else {
                if (i < 10) {
                    return;
                }
            }
            int j = EnchantmentHelper.getRiptide(stack);
            NbtCompound nbtCompound = stack.getOrCreateNbt();
            int reservoirCharges = nbtCompound.getInt(ReservoirMixin.Reservoir_Charges_Key);
            if (j > 0) {
                if (playerEntity.isTouchingWaterOrRain() || playerEntity.hasStatusEffect(ModEffects.SOAKED)) {
                } else if (reservoirCharges > 0) {
                    reservoirCharges = reservoirCharges - 1;
                    nbtCompound.putInt(ReservoirMixin.Reservoir_Charges_Key, reservoirCharges);
                    stack.setNbt(nbtCompound);
                } else {
                    return;
                }
            }
            if (!world.isClient) {
                stack.damage(1, playerEntity, p -> p.sendToolBreakStatus(user.getActiveHand()));
                if (j == 0) {
                    TridentEntity tridentEntity = new TridentEntity(world, playerEntity, stack);
                    tridentEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0f, (float) LightweightEnchantment.getLightweightTridentSpeed(lightweightLevel) + (float) j * 0.5f, 1.0f);
                    if (playerEntity.getAbilities().creativeMode) {
                        tridentEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                    }
                    world.spawnEntity(tridentEntity);
                    world.playSoundFromEntity(null, tridentEntity, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0f, 1.0f);
                    if (!playerEntity.getAbilities().creativeMode) {
                        playerEntity.getInventory().removeOne(stack);
                    }
                }
            }
            playerEntity.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
            if (j > 0) {
                float f = playerEntity.getYaw();
                float g = playerEntity.getPitch();
                float h = -MathHelper.sin(f * ((float) Math.PI / 180)) * MathHelper.cos(g * ((float) Math.PI / 180));
                float k = -MathHelper.sin(g * ((float) Math.PI / 180));
                float l = MathHelper.cos(f * ((float) Math.PI / 180)) * MathHelper.cos(g * ((float) Math.PI / 180));
                float m = MathHelper.sqrt(h * h + k * k + l * l);
                float n = 3.0f * ((1.0f + (float) j) / 4.0f);
                playerEntity.addVelocity(h *= n / m, k *= n / m, l *= n / m);
                playerEntity.useRiptide(20);
                if (playerEntity.isOnGround()) {
                    float o = 1.1999999f;
                    playerEntity.move(MovementType.SELF, new Vec3d(0.0, 1.1999999284744263, 0.0));
                }
                SoundEvent soundEvent = j >= 3 ? SoundEvents.ITEM_TRIDENT_RIPTIDE_3 : (j == 2 ? SoundEvents.ITEM_TRIDENT_RIPTIDE_2 : SoundEvents.ITEM_TRIDENT_RIPTIDE_1);
                world.playSoundFromEntity(null, playerEntity, soundEvent, SoundCategory.PLAYERS, 1.0f, 1.0f);
            }
        }

        @Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isTouchingWaterOrRain()Z"))
        private boolean onUse(PlayerEntity instance) {
            ItemStack[] stacks = {instance.getMainHandStack(), instance.getOffHandStack()};
            ItemStack mainHandStack = instance.getMainHandStack();
            ItemStack offHandStack = instance.getOffHandStack();
            NbtCompound mainHandNBT = mainHandStack.getOrCreateNbt();
            NbtCompound offHandNBT = offHandStack.getOrCreateNbt();
            int reservoirCharges = mainHandNBT.getInt(ReservoirMixin.Reservoir_Charges_Key);
            MCCourseMod.LOGGER.info("Main Hand Charges: " + reservoirCharges);
            if (reservoirCharges == 0) {
                reservoirCharges = offHandNBT.getInt(ReservoirMixin.Reservoir_Charges_Key);
                MCCourseMod.LOGGER.info("Off Hand Charges: " + reservoirCharges);
            }
            if (instance.isTouchingWaterOrRain() || instance.hasStatusEffect(ModEffects.SOAKED) || reservoirCharges > 0) {
                MCCourseMod.LOGGER.info("RETURNED true");
                return true;
            } else {
                MCCourseMod.LOGGER.info("RETURNED false");
                return false;
            }
        }
    }
}
