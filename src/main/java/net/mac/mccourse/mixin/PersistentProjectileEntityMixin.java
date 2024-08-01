package net.mac.mccourse.mixin;

import net.mac.mccourse.MCCourseMod;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin {
    @Redirect(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;ceil(D)I"))
    private int modifyDamage(double value){
        MCCourseMod.LOGGER.info("PROJECTILE DAMAGE: " + (int) Math.min(value, 15));
        return (int) Math.min(value, 15);
    }
}
