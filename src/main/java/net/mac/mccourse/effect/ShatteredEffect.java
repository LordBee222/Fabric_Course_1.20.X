package net.mac.mccourse.effect;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.mixin.TridentEntityAccessor;
import net.mac.mccourse.util.ILodgedTrident;
import net.mac.mccourse.util.LodgedTridentUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ShatteredEffect extends StatusEffect {

    protected ShatteredEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {

        super.onRemoved(entity, attributes, amplifier);
        if (!entity.getWorld().isClient){
            if (((ILodgedTrident)entity).hasLodgedTrident()){
                TridentEntity trident = LodgedTridentUtil.createTridentEntity(entity);
                trident.setOwner(((ILodgedTrident)entity).getLodgedTridentOwner());
                MCCourseMod.LOGGER.info("OWNER TYPE: " + ((ILodgedTrident)entity).getLodgedTridentOwner());
                trident.setPos(entity.getX(), entity.getY(), entity.getZ());
                trident.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
                MCCourseMod.LOGGER.info("ABOUT TO SPAWN DATA: " + (((TridentEntityAccessor)trident).getTridentStack()).getNbt());
                entity.getWorld().spawnEntity(trident);
                ((ILodgedTrident)entity).removeLodgedTrident();
            }
        }
    }
}
