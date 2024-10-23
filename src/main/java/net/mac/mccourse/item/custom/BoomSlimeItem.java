package net.mac.mccourse.item.custom;

import net.mac.mccourse.entity.custom.BoomSlimeEntity;
import net.mac.mccourse.entity.custom.DiceProjectileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class BoomSlimeItem extends Item {
    public BoomSlimeItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_SLIME_BLOCK_BREAK, SoundCategory.NEUTRAL, 1f, 1f);
        if (!world.isClient) {
            BoomSlimeEntity boomSlimeEntity = new BoomSlimeEntity(user, world);
            //boomSlimeEntity.setItem(itemStack);
            boomSlimeEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 0.5f, 0f);
            world.spawnEntity(boomSlimeEntity);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }
}
