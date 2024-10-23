package net.mac.mccourse.item.custom;

import net.mac.mccourse.MCCourseMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class SniperItem  extends Item {
    public SniperItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            HitResult hitResult = getTarget(user, world, 100.0D); // Perform combined raycast with a range of 100 blocks
            MCCourseMod.LOGGER.info("DID RAYCAST");
            if (hitResult != null) {
                if (hitResult.getType() == HitResult.Type.ENTITY) {
                    EntityHitResult entityHitResult = (EntityHitResult) hitResult;
                    Entity target = entityHitResult.getEntity();
                    if (target instanceof LivingEntity) {
                        float f = 8.0f;
                        DamageSource damageSource = user.getDamageSources().playerAttack(user);
                        target.damage(damageSource, f);
                        MCCourseMod.LOGGER.info("Was Living");
                    }
                } else if (hitResult.getType() == HitResult.Type.BLOCK) {
                    BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                    BlockPos blockPos = blockHitResult.getBlockPos();
                    MCCourseMod.LOGGER.info("Was Block");
                }
            }
            user.getItemCooldownManager().set(this, 20); // Cooldown
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    private HitResult getTarget(PlayerEntity player, World world, double range) {
        Vec3d startPos = player.getCameraPosVec(1.0F);
        Vec3d direction = player.getRotationVec(1.0F);
        Vec3d endPos = startPos.add(direction.multiply(range));
        HitResult blockHitResult = world.raycast(new RaycastContext(startPos, endPos, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player));
        Box searchBox = player.getBoundingBox().stretch(direction.multiply(range)).expand(1.0D, 1.0D, 1.0D);
        EntityHitResult entityHitResult = raycastEntities(world, player, startPos, endPos, searchBox);
        if (entityHitResult != null && entityHitResult.getPos().distanceTo(startPos) < blockHitResult.getPos().distanceTo(startPos)) {
            return entityHitResult;
        } else {
            return blockHitResult;
        }
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        return ActionResult.FAIL;
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        return ActionResult.FAIL;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity player && selected) {
            player.sendMessage(Text.literal("Left-click detected!"), true);
        }
    }



    private EntityHitResult raycastEntities(World world, PlayerEntity player, Vec3d startPos, Vec3d endPos, Box searchBox) {
        return ProjectileUtil.raycast(player, startPos, endPos, searchBox, (entity) -> !entity.isSpectator() && entity.isAlive(), endPos.distanceTo(startPos));
    }
}
