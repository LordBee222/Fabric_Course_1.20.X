package net.mac.mccourse.block.custom;

import net.mac.mccourse.MCCourseMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PinkGarnetLampBlock extends Block {
   public static final BooleanProperty CLICKED = BooleanProperty.of("clicked");
   public static final IntProperty CHARGES = IntProperty.of("charges", 0, 3);

    public PinkGarnetLampBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(CLICKED, false).with(CHARGES, 0));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        if (!world.isClient &&  hand == Hand.MAIN_HAND){
            if (stack.isOf(Items.FLINT)){
                world.setBlockState(pos, state.cycle(CHARGES));
                MCCourseMod.LOGGER.info("Click With Flint");
            }
          //  stack.getItem().getName().getString().equals(Items.GLOWSTONE.getName());
            //world.setBlockState(pos, state.cycle(CLICKED));
        }
        return ActionResult.SUCCESS;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(CLICKED).add(CHARGES);
        //builder.add(CHARGES);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!world.isClient) {
            world.scheduleBlockTick(pos, this, 20);
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        world.scheduleBlockTick(pos, this, 20);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!world.isClient){
            int charges = state.get(CHARGES);
            spawnParticles(world, pos, random, state);
            world.scheduleBlockTick(pos, this, 20);
        }
    }

    private void spawnParticles(ServerWorld world, BlockPos pos, Random random, BlockState state) {
        int charges = state.get(CHARGES);
        for (int i = 0; i < charges; i++) {
            double x = pos.getX() + 0.5 + random.nextGaussian() * 0.6;
            double y = pos.getY() + 0.5 + random.nextGaussian() * 0.6;
            double z = pos.getZ() + 0.5 + random.nextGaussian() * 0.6;
            world.spawnParticles(ParticleTypes.SOUL, x, y, z, 1, 0, 0, 0, 0);
        }
    }
}
