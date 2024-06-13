package net.mac.mccourse.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BoomSlimeItem extends Item {
    public BoomSlimeItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (user.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(itemStack);
        }
        world.playSound(null, user.getX(), user.getEyeY(), user.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        if (!world.isClient) {
            Vec3d look = user.getRotationVec(1.0F);
            TntEntity tntEntity = new TntEntity(world, user.getX(), user.getEyeY(), user.getZ(), user);
            double velocityX = look.x * 1.5;
            double velocityY = look.y * 1.5;
            double velocityZ = look.z * 1.5;
            tntEntity.setVelocity(velocityX, velocityY, velocityZ);
            world.spawnEntity(tntEntity);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        user.getItemCooldownManager().set(this, 5); // 10 ticks = 0.5 seconds
        return TypedActionResult.success(itemStack, world.isClient());
    }
}
