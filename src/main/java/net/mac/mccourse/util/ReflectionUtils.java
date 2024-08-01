package net.mac.mccourse.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.lang.reflect.Method;

public class ReflectionUtils {
    private static Method createArrowMethod;

    static {
        try {
            createArrowMethod = CrossbowItem.class.getDeclaredMethod("createArrow", World.class, LivingEntity.class, ItemStack.class, ItemStack.class);
            createArrowMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Failed to find method 'createArrow' in CrossbowItem class", e);
        }
    }

    public static PersistentProjectileEntity createArrow(World world, LivingEntity shooter, ItemStack crossbow, ItemStack arrow) {
        try {
            return (PersistentProjectileEntity) createArrowMethod.invoke(null, world, shooter, crossbow, arrow);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke 'createArrow' method in CrossbowItem class", e);
        }
    }
}
