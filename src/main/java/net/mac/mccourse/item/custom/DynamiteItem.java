package net.mac.mccourse.item.custom;

import net.mac.mccourse.entity.ModEntities;
import net.mac.mccourse.entity.custom.BombEntity;
import net.mac.mccourse.entity.custom.DiceProjectileEntity;
import net.mac.mccourse.entity.custom.DynamiteEntity;
import net.mac.mccourse.entity.custom.StickyExplosiveEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Objects;

public class DynamiteItem extends Item {
    public DynamiteItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.NEUTRAL, 1f, 1f);
        if (!world.isClient) {
            BombEntity entity = new BombEntity(user, user.getWorld(), EntityType.FALLING_BLOCK, Math.round(50F), 1f);
            entity.setItem(this.getDefaultStack());
            entity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 1.0F);
            entity.setPos(user.getX(), user.getY() + (double) user.getStandingEyeHeight() - 0.10000000149011612D, user.getZ());
            entity.setOwner(user);
            world.spawnEntity(entity);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }
}
