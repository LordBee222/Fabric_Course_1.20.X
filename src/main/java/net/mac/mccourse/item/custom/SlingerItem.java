package net.mac.mccourse.item.custom;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.entity.custom.BoomSlimeEntity;
import net.mac.mccourse.entity.custom.DiceProjectileEntity;
import net.mac.mccourse.entity.custom.DynamiteEntity;
import net.mac.mccourse.item.ModItems;
import net.mac.mccourse.util.ModTags;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class SlingerItem extends RangedWeaponItem implements Vanishable {
    public static final Predicate<ItemStack> SLINGER_PROJECTILES = stack -> stack.isIn(ModTags.Items.SLINGER_PROJECTILES);

    public SlingerItem(Settings settings) {
        super(settings);
    }

    /********************************************************************************************************
     /// getRange
     /// Purpose: Gets Projectile Range
     /*********************************************************************************************************/
    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity)) {
            return;
        }
        PlayerEntity playerEntity = (PlayerEntity) user;
        ItemStack itemStack = playerEntity.getOffHandStack();
        if (!itemStack.isEmpty() && isValidProjectile(itemStack) && !itemStack.isOf(ModItems.SLINGER)) {
            fireProjectile(world, playerEntity, itemStack, stack);
        }
    }

    /********************************************************************************************************
     /// getRange
     /// Purpose: Gets Projectile Range
     /*********************************************************************************************************/
    private void fireProjectile(World world, PlayerEntity playerEntity, ItemStack projectileStack, ItemStack stack) {
        Entity projectile = createProjectile(world, playerEntity, projectileStack.getItem(), projectileStack.copy());
        if (projectile != null) {
            float f = getPullProgress(this.getMaxUseTime(stack) - playerEntity.getItemUseTimeLeft());
            if (projectile instanceof ProjectileEntity projectileEntity) {
                projectileEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0f, f * 3.0f, 1.0f);
                world.spawnEntity(projectileEntity);
            } else {
                Vec3d lookVec = playerEntity.getRotationVec(1.0F);
                Vec3d velocity = lookVec.multiply(f * 3.0);
                Vec3d playerPos = playerEntity.getEyePos().add(lookVec.multiply(1.5));
                projectile.setVelocity(velocity);
                projectile.setPosition(playerPos);
                if (projectile instanceof ItemEntity itemEntity){
                    itemEntity.setPickupDelay(15);
                }
                world.spawnEntity(projectile);
            }
            stack.damage(1, playerEntity, p -> p.sendToolBreakStatus(playerEntity.getActiveHand()));
            world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0f, 1.0f / (world.getRandom().nextFloat() * 0.4f + 1.2f) + f * 0.5f);
            playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));

            if (!playerEntity.getAbilities().creativeMode){
                if (projectileStack.isOf(Items.PUFFERFISH_BUCKET) || projectileStack.isOf(Items.COD_BUCKET) || projectileStack.isOf(Items.SALMON_BUCKET) || projectileStack.isOf(Items.TROPICAL_FISH_BUCKET) || projectileStack.isOf(Items.AXOLOTL_BUCKET) || projectileStack.isOf(Items.TADPOLE_BUCKET)){
                    playerEntity.setStackInHand(Hand.OFF_HAND, new ItemStack(Items.WATER_BUCKET));
                } else {
                    projectileStack.decrement(1);
                }
            }
        }
    }

    /********************************************************************************************************
     /// getRange
     /// Purpose: Gets Projectile Range
     /*********************************************************************************************************/
    private Entity createProjectile(World world, PlayerEntity playerEntity, Item projectileItem, ItemStack stack) {
        if (projectileItem == Items.ENDER_PEARL) {
            return new EnderPearlEntity(world, playerEntity);
        } else if (projectileItem == Items.EGG) {
            return new EggEntity(world, playerEntity);
        } else if (projectileItem == Items.SNOWBALL) {
            return new SnowballEntity(world, playerEntity);
        } else if (projectileItem == Items.FIRE_CHARGE) {
            return new FireballEntity(world, playerEntity, 0, 0, 0, 4);
        } else if (projectileItem == Items.PUFFERFISH_BUCKET){
            return new PufferfishEntity(EntityType.PUFFERFISH, world);
        } else if (projectileItem == Items.COD_BUCKET) {
            return new CodEntity(EntityType.COD, world);
        } else if (projectileItem == Items.SALMON_BUCKET) {
            return new SalmonEntity(EntityType.SALMON, world);
        } else if (projectileItem == Items.TROPICAL_FISH_BUCKET) {
            return new TropicalFishEntity(EntityType.TROPICAL_FISH, world);
        } else if (projectileItem == Items.AXOLOTL_BUCKET) {
            AxolotlEntity axolotl = new AxolotlEntity(EntityType.AXOLOTL, world);
            axolotl.copyDataFromNbt(stack.getNbt());
            axolotl.setFromBucket(true);
            if (!stack.getName().getString().contains("Bucket of Axolotl")){
                axolotl.setCustomName(stack.getName());
            }
            MCCourseMod.LOGGER.info("STACK NBT: " + stack.getNbt());
            return axolotl;
        } else if (projectileItem == Items.TADPOLE_BUCKET) {
            return new TadpoleEntity(EntityType.TADPOLE, world);
        } else if (projectileItem == ModItems.DICE) {
            DiceProjectileEntity diceProjectileEntity = new DiceProjectileEntity(playerEntity, world);
            diceProjectileEntity.setItem(stack);
            return diceProjectileEntity;
        } else if (projectileItem == ModItems.DYNAMITE) {
            DynamiteEntity dynamiteEntity = new DynamiteEntity(playerEntity, world);
            dynamiteEntity.setItem(stack);
            return dynamiteEntity;
        } else  if (projectileItem == ModItems.BOOM_SLIME){
            BoomSlimeEntity boomSlimeEntity = new BoomSlimeEntity(playerEntity, world);
            boomSlimeEntity.setItem(stack);
            return boomSlimeEntity;
        } else if (projectileItem == Items.SPLASH_POTION) {
            PotionEntity potion = new PotionEntity(world, playerEntity);
            potion.setItem(stack);
            return potion;
        } else if (projectileItem == Items.LINGERING_POTION){
            PotionEntity potion = new PotionEntity(world, playerEntity);
            potion.setItem(stack);
            return potion;
        } else {
            ItemStack stackOf1 = stack;
            stackOf1.setCount(1);
            return new ItemEntity(world, playerEntity.getX(), playerEntity.getEyeY(), playerEntity.getZ(), stackOf1);
        }
    }

    public static boolean isValidProjectile(ItemStack stack) {
        return true;
    }

    /********************************************************************************************************
     /// getRange
     /// Purpose: Gets Projectile Range
     /*********************************************************************************************************/
    public static float getPullProgress(int useTicks) {
        float f = (float) useTicks / 20.0f;
        if ((f = (f * f + f * 2.0f) / 3.0f) > 1.0f) {
            f = 1.0f;
        }
        return f;
    }

    /********************************************************************************************************
     /// getRange
     /// Purpose: Gets Projectile Range
     /*********************************************************************************************************/
    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    /********************************************************************************************************
     /// getRange
     /// Purpose: Gets Projectile Range
     /*********************************************************************************************************/
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    /********************************************************************************************************
     /// getRange
     /// Purpose: Gets Projectile Range
     /*********************************************************************************************************/
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        boolean hasProjectile = !user.getOffHandStack().isEmpty() && isValidProjectile(user.getOffHandStack());
        if (user.getAbilities().creativeMode || hasProjectile) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
        return TypedActionResult.fail(itemStack);
    }

    /********************************************************************************************************
     /// getRange
     /// Purpose: Gets Projectile Range
     /*********************************************************************************************************/
    @Override
    public Predicate<ItemStack> getProjectiles() {
        return SLINGER_PROJECTILES;
    }

    /********************************************************************************************************
     /// getRange
     /// Purpose: Gets Projectile Range
     /*********************************************************************************************************/
    @Override
    public int getRange() {
        return 10;
    }
}
