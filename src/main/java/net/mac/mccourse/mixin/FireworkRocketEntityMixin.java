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
    //double radius = 3;
    //double verticalVelocity = 1.5;
    //double horizontalVelocity = 3.75;

    double radius = 3;
    double verticalVelocity = 1.5;
    double horizontalVelocity = 3.75;

    @Inject(method = "explode", at = @At("HEAD"))
    private void explode(CallbackInfo info) {
        FireworkRocketEntity fireworkEntity = (FireworkRocketEntity) (Object) this;
        World world = fireworkEntity.getWorld();
        if (!world.isClient) {
            if (fireworkEntity.wasShotAtAngle()) {

                List<Entity> entities = world.getOtherEntities(fireworkEntity, new Box(
                        fireworkEntity.getX() - radius, fireworkEntity.getY() - radius, fireworkEntity.getZ() - radius,
                        fireworkEntity.getX() + radius, fireworkEntity.getY() + radius, fireworkEntity.getZ() + radius
                ));

                for (Entity entity : entities) {
                       /* if (entity instanceof PlayerEntity playerEntity) {
                            Vec3d knockbackDirection = playerEntity.getPos().subtract(fireworkEntity.getPos()).normalize();
                            Vec3d addedVelocity = new Vec3d(knockbackDirection.x * horizontalVelocity, verticalVelocity, knockbackDirection.z * horizontalVelocity);
                            playerEntity.addVelocity(addedVelocity.x, addedVelocity.y, addedVelocity.z);
                            playerEntity.velocityModified = true;
                        }

                        */

                    Vec3d knockbackDirection = entity.getPos().subtract(fireworkEntity.getPos()).normalize();
                    Vec3d addedVelocity = new Vec3d(knockbackDirection.x * horizontalVelocity, verticalVelocity, knockbackDirection.z * horizontalVelocity);
                    entity.addVelocity(addedVelocity.x, addedVelocity.y, addedVelocity.z);
                    entity.velocityModified = true;

                }
            }
        }
    }
}