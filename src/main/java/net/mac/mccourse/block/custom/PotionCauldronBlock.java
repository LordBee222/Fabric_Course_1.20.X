package net.mac.mccourse.block.custom;

import net.mac.mccourse.MCCourseMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Predicate;

public class PotionCauldronBlock extends LeveledCauldronBlock {
    public static final BooleanProperty HAS_HASTE = BooleanProperty.of("has_haste");
    public static final BooleanProperty HEATED = BooleanProperty.of("heated");

    public PotionCauldronBlock(Settings settings, Predicate<Biome.Precipitation> precipitationPredicate, Map<Item, CauldronBehavior> behaviorMap) {
        super(settings, precipitationPredicate, behaviorMap);
        this.setDefaultState(this.getDefaultState().with(LEVEL, 1).with(HEATED, false));
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (this.isEntityTouchingFluid(state, pos, entity)){
            if (entity instanceof PlayerEntity player){
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 20, 0));
            }
        }
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        World worldAccess = ctx.getWorld();
        return this.getDefaultState().with(HEATED, this.isHeatedBaseBlock(worldAccess.getBlockState(blockPos.down())));
    }

    private boolean isHeatedBaseBlock(BlockState state) {
        return state.isOf(Blocks.FIRE) || state.isOf(Blocks.CAMPFIRE) || state.isOf(Blocks.SOUL_CAMPFIRE)
                || state.isOf(Blocks.LAVA);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LEVEL, HEATED);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.DOWN) {
            return state.with(HEATED, this.isHeatedBaseBlock(neighborState));
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!world.isClient){
            if (isHeatedBaseBlock(state)){
            }


            if (state.get(HEATED)){
                spawnParticles(world, pos, random, state);
            }
            world.scheduleBlockTick(pos, this, 20);
        }
    }

    private void spawnParticles(ServerWorld world, BlockPos pos, Random random, BlockState state) {
        MCCourseMod.LOGGER.info("SPAWNED PARTICLES");
        for (int i = 0; i < 10; i++) {
            double x = pos.getX() + 0.5 + random.nextGaussian() * 0.6;
            double y = pos.getY() + 0.5 + random.nextGaussian() * 0.6;
            double z = pos.getZ() + 0.5 + random.nextGaussian() * 0.6;
            world.spawnParticles(ParticleTypes.SOUL, x, y, z, 1, 0, 0, 0, 0);
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        world.scheduleBlockTick(pos, this, 20);
    }
}