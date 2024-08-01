package net.mac.mccourse.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class ModEntityDeathExplodeMixin {

    @Shadow public abstract boolean damage(DamageSource source, float amount);

    @Shadow public abstract void kill();

    @Inject(method = "onDeath", at = @At("HEAD"))
    protected void injectOnDeathMethod(DamageSource damageSource, CallbackInfo ci){
        LivingEntity entity = (LivingEntity) (Object) this;
        Entity killer = damageSource.getAttacker();
        World world = entity.getWorld();
        if (killer instanceof LivingEntity) {
            if(!world.isClient){
                LivingEntity livingKiller = (LivingEntity) killer;
                ItemStack stack = livingKiller.getMainHandStack();
                if (stack.isOf(Items.NETHERITE_SWORD)){
                    world.createExplosion(entity, entity.getX(), entity.getY(), entity.getZ(), 4.0f, World.ExplosionSourceType.BLOCK);
                }
            }
        }
    }
}
