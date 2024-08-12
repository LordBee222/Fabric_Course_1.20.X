package net.mac.mccourse.item.custom;

import net.mac.mccourse.entity.custom.DiceProjectileEntity;
import net.mac.mccourse.entity.custom.DynamiteEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class DynamiteItem extends Item {
    public DynamiteItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.NEUTRAL, 1f, 1f);
        if (!world.isClient) {
            DynamiteEntity dynamiteEntity = new DynamiteEntity(user, world);
            dynamiteEntity.setItem(itemStack);
            dynamiteEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 0.5f, 0f);
            world.spawnEntity(dynamiteEntity);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }
}
