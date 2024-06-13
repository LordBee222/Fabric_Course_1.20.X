package net.mac.mccourse.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class BoomHoneyItem extends Item {
    public static final int MAX_USE_TIME = 1200;

    public BoomHoneyItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user.isSneaking()) {
            return this.useExplosionFunction(world, user, hand);
        } else {
            user.playSound(SoundEvents.ITEM_SPYGLASS_USE, 1.0f, 1.0f);
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            user.setCurrentHand(hand);
            return TypedActionResult.consume(user.getStackInHand(hand));
        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return MAX_USE_TIME;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPYGLASS;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        this.playStopUsingSound(user);
    }

    private void playStopUsingSound(LivingEntity user) {
        user.playSound(SoundEvents.ITEM_SPYGLASS_STOP_USING, 1.0f, 1.0f);
    }

    private TypedActionResult<ItemStack> useExplosionFunction(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (user.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(itemStack);
        }
        world.playSound(null, user.getX(), user.getEyeY(), user.getZ(), SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.NEUTRAL, 0.5f, 2f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        if (!world.isClient) {
            Vec3d look = user.getRotationVec(1.0F);
            Vec3d start = user.getCameraPosVec(1.0F);
            Vec3d end = start.add(look.x * 1000, look.y * 1000, look.z * 1000);

            BlockHitResult hitResult = world.raycast(new RaycastContext(start, end, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, user));
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos hitPos = hitResult.getBlockPos();
                world.createExplosion(user, hitPos.getX(), hitPos.getY(), hitPos.getZ(), 5.0F, true, World.ExplosionSourceType.TNT);
            }
            double distance = start.distanceTo(hitResult.getPos());
            Vec3d direction = look.normalize();
            for (double d = 0; d < distance; d += 1.0) {
                Vec3d point = start.add(direction.multiply(d));
                world.addParticle(ParticleTypes.SMOKE, point.x, point.y, point.z, 0, 0, 0);
            }
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        user.getItemCooldownManager().set(this, 10); // 10 ticks = 0.5 seconds
        return TypedActionResult.success(itemStack, world.isClient());
    }
}