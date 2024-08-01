package net.mac.mccourse.mixin;

import net.mac.mccourse.enchantment.ModEnchantments;
import net.mac.mccourse.util.InventoryUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityMixin {
    double radius = 1;
    double verticalVelocity = 0.5;
    double horizontalVelocity = 1.25;

    @Inject(method = "explode", at = @At("HEAD"))
    private void explode(CallbackInfo info) {
        FireworkRocketEntity fireworkEntity = (FireworkRocketEntity) (Object) this;
        World world = fireworkEntity.getWorld();
        if (!world.isClient) {

            // Check if the firework was shot from a crossbow with the custom enchantment
            Optional<ItemStack> crossbowItem = getCrossbowItem(fireworkEntity);
            if (crossbowItem.isPresent() && hasCustomEnchantment(crossbowItem.get())) {

                if (fireworkEntity.wasShotAtAngle()){
                    int jumperLevel = EnchantmentHelper.getLevel(ModEnchantments.JUMPER, crossbowItem.get());

                    double jumperRadius = radius * jumperLevel;
                    double jumperVerticalVelocity = verticalVelocity * jumperLevel;
                    double jumperHorizontalVelocity = horizontalVelocity * jumperLevel;

                    List<Entity> entities = world.getOtherEntities(fireworkEntity, new Box(
                            fireworkEntity.getX() - jumperRadius, fireworkEntity.getY() - jumperRadius, fireworkEntity.getZ() - jumperRadius,
                            fireworkEntity.getX() + jumperRadius, fireworkEntity.getY() + jumperRadius, fireworkEntity.getZ() + jumperRadius
                    ));

                    for (Entity entity : entities) {
                        if (entity instanceof PlayerEntity playerEntity) {
                            Vec3d knockbackDirection = playerEntity.getPos().subtract(fireworkEntity.getPos()).normalize();
                            Vec3d addedVelocity = new Vec3d(knockbackDirection.x * jumperHorizontalVelocity, jumperVerticalVelocity, knockbackDirection.z * jumperHorizontalVelocity);
                            playerEntity.addVelocity(addedVelocity.x, addedVelocity.y, addedVelocity.z);
                            playerEntity.velocityModified = true;
                        }
                    }
                }


            }
        }
    }

    private Optional<ItemStack> getCrossbowItem(FireworkRocketEntity fireworkEntity) {
        // Logic to determine and return the crossbow item that fired the firework
        // This requires tracking the source entity and checking its main-hand item
        Entity owner = fireworkEntity.getOwner();
        if (owner instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) owner;
            ItemStack mainHandItem = player.getMainHandStack();
            ItemStack offHandItem = player.getOffHandStack();
            if (mainHandItem.getItem() instanceof CrossbowItem) {
                return Optional.of(mainHandItem);
            } else if (offHandItem.getItem() instanceof  CrossbowItem) {
                return Optional.of(offHandItem);
            }
        }
        return Optional.empty();
    }

    private boolean hasCustomEnchantment(ItemStack crossbow) {
        return EnchantmentHelper.getLevel(ModEnchantments.JUMPER, crossbow) > 0;
    }

}