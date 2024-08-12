package net.mac.mccourse.item.custom;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.network.DashPacket;
import net.mac.mccourse.util.DashUtil;
import net.mac.mccourse.util.IPlayerDashSaver;
import net.mac.mccourse.util.ModTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public class PaxelItem extends MiningToolItem {

    public PaxelItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(attackDamage, attackSpeed, material, ModTags.Blocks.PAXEL_MINABLE, settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (!context.getWorld().isClient()) {
            context.getWorld().setBlockState(context.getBlockPos(), Blocks.END_PORTAL.getDefaultState());
        }
       return ActionResult.SUCCESS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        IPlayerDashSaver playerDashData = (IPlayerDashSaver) user;
            if (DashUtil.canDash(playerDashData, user)){
                DashUtil.use(playerDashData, user);
            }
        return super.use(world, user, hand);
    }
}
