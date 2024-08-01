package net.mac.mccourse.mixin;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.enchantment.ModEnchantments;
import net.mac.mccourse.util.CrossbowUtil;
import net.mac.mccourse.util.IShotCounter;
import net.mac.mccourse.util.ReflectionUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Method;

import static net.mac.mccourse.util.CrossbowUtil.clearProjectiles;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {




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

    @Inject(method = "getRange", at = @At("HEAD"), cancellable = true)
    private void getRange(CallbackInfoReturnable<Integer> cir) {
        CrossbowItem crossbowItem = (CrossbowItem) (Object) this;
        int baseRange = 8;
        ItemStack stack = crossbowItem.getDefaultStack();
        int level = EnchantmentHelper.getLevel(ModEnchantments.FORCEFUL, stack);
        int range = baseRange * (1 + level);
        cir.setReturnValue(range);
    }

    @Inject(method = "shoot", at = @At("HEAD"), cancellable = true)
    private static void shoot(World world, LivingEntity shooter, Hand hand, ItemStack crossbow, ItemStack projectile, float soundPitch, boolean creative, float speed, float divergence, float simulated, CallbackInfo ci) {
        ProjectileEntity projectileEntity;
        if (world.isClient) {
            return;
        }
        boolean bl = projectile.isOf(Items.FIREWORK_ROCKET);
        if (bl) {
            projectileEntity = new FireworkRocketEntity(world, projectile, shooter, shooter.getX(), shooter.getEyeY() - (double) 0.15f, shooter.getZ(), true);
        } else {
            projectileEntity = ReflectionUtils.createArrow(world, shooter, crossbow, projectile);
            if (creative || simulated != 0.0f) {
                ((PersistentProjectileEntity) projectileEntity).pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
            }
        }
        if (shooter instanceof CrossbowUser) {
            CrossbowUser crossbowUser = (CrossbowUser) shooter;
            crossbowUser.shoot(crossbowUser.getTarget(), crossbow, projectileEntity, simulated);
        } else {
            Vec3d vec3d = shooter.getOppositeRotationVector(1.0f);
            Quaternionf quaternionf = new Quaternionf().setAngleAxis(simulated * ((float) Math.PI / 180), vec3d.x, vec3d.y, vec3d.z);
            Vec3d vec3d2 = shooter.getRotationVec(1.0f);
            Vector3f vector3f = vec3d2.toVector3f().rotate(quaternionf);
            projectileEntity.setVelocity(vector3f.x(), vector3f.y(), vector3f.z(), speed, divergence);
        }
        crossbow.damage(bl ? 3 : 1, shooter, e -> e.sendToolBreakStatus(hand));
        world.spawnEntity(projectileEntity);

        // Change the sound based on the projectile speed
        if (speed > 6.0f) {
            world.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.ITEM_CROSSBOW_SHOOT, SoundCategory.PLAYERS, 1.0f, 0.65F);
        } else {
            world.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.ITEM_CROSSBOW_SHOOT, SoundCategory.PLAYERS, 1.0f, soundPitch);
        }

        ci.cancel();
    }

    @Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;setCharged(Lnet/minecraft/item/ItemStack;Z)V"))
    private void onUse(ItemStack stack, boolean charged) {
        NbtCompound nbtCompound = stack.getNbt();
        if (stack.getItem() instanceof CrossbowItem crossbow) {
            int shots = nbtCompound.getInt(CrossbowUtil.SHOTS_KEY);
            if (shots >=1){
                CrossbowItem.setCharged(stack, true);
            } else{
                CrossbowItem.setCharged(stack, false);
            }
            //System.out.println("Shots: " + shots);
                //CrossbowItem.setCharged(stack, true);
            }
    }

    @Redirect(method = "clearProjectiles", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtCompound;getList(Ljava/lang/String;I)Lnet/minecraft/nbt/NbtList;"))
    private static NbtList getList(NbtCompound instance, String key, int type) {
        MCCourseMod.LOGGER.info("instance : " + instance.asString());
        NbtList nbtList = instance.getList("ChargedProjectiles", NbtElement.LIST_TYPE);
        MCCourseMod.LOGGER.info("List : " + nbtList.asString());
        return nbtList;
    }

    @Redirect(method = "postShoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;clearProjectiles(Lnet/minecraft/item/ItemStack;)V"))
    private static void redirectClear(ItemStack crossbow) {
        NbtCompound nbtCompound = crossbow.getNbt();
        if (crossbow.getItem() instanceof CrossbowItem crossbowItem) {
            int shots = nbtCompound.getInt(CrossbowUtil.SHOTS_KEY);
                if (shots > 0){
                    shots--;
                    MCCourseMod.LOGGER.info("Shots--: " + shots);
                    nbtCompound.putInt(CrossbowUtil.SHOTS_KEY, shots);
                } else{
                    clearProjectiles(crossbow);
                    MCCourseMod.LOGGER.info("Cleared: " + shots);
                    CrossbowUtil.clearProjectiles(crossbow);
                }
                //System.out.println("Shots: " + shots);
            crossbow.setNbt(nbtCompound);
            MCCourseMod.LOGGER.info("Shots: " + shots);
            MCCourseMod.LOGGER.info("NBT: " + nbtCompound.asString());

        }
    }

    @Redirect(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;setCharged(Lnet/minecraft/item/ItemStack;Z)V"))
    private void setShots(ItemStack stack, boolean charged) {
        if (stack.getItem() instanceof CrossbowItem crossbow) {
            NbtCompound nbtCompound = stack.getOrCreateNbt();
            nbtCompound.putInt(CrossbowUtil.SHOTS_KEY, 5);
            MCCourseMod.LOGGER.info("Set Shots");
            CrossbowItem.setCharged(stack, true);
            }
    }


}
