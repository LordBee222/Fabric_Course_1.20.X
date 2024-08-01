package net.mac.mccourse.block.custom;

import net.mac.mccourse.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;

public class PotionCauldronBehavior implements CauldronBehavior {
    public static final Map<Item, CauldronBehavior> POTION_CAULDRON_BEHAVIOR = CauldronBehavior.createMap();
    public static final CauldronBehavior FILL_WITH_POTION = (state, world, pos, player, hand, stack) -> {
        if (!world.isClient) {
            player.setStackInHand(hand, new ItemStack(Items.BUCKET));
            world.setBlockState(pos, (BlockState) ModBlocks.POTION_CAULDRON.getDefaultState().with(LeveledCauldronBlock.LEVEL, 3), 3);
            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
        return ActionResult.SUCCESS;
    };

    @Override
    public ActionResult interact(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack) {
        int level = state.get(LeveledCauldronBlock.LEVEL);
        Item item = stack.getItem();

        // Interaction with milk bucket
        if (item == Items.MILK_BUCKET && level < 3) {
            if (!world.isClient) {
                player.setStackInHand(hand, new ItemStack(Items.BUCKET));
                world.setBlockState(pos, state.with(LeveledCauldronBlock.LEVEL, level + 1), 3);
                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
            return ActionResult.SUCCESS;
        }

        // Interaction with glowstone dust
        if (item == Items.GLOWSTONE_DUST && level < 3) {
            if (!world.isClient) {
                stack.decrement(1); // Decrease the stack size by 1
                if (!player.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE))) {
                    player.dropItem(new ItemStack(Items.GLASS_BOTTLE), false);
                }
                world.setBlockState(pos, state.with(LeveledCauldronBlock.LEVEL, level + 1), 3);
                world.playSound(null, pos, SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
            return ActionResult.SUCCESS;
        }

        // Interaction with empty bottle
        if (item == Items.GLASS_BOTTLE && level > 0) {
            if (!world.isClient) {
                stack.decrement(1);
                if (!player.getInventory().insertStack(new ItemStack(Items.GLOWSTONE_DUST))) {
                    player.dropItem(new ItemStack(Items.GLOWSTONE_DUST), false);
                }
                int newLevel = level - 1;
                if (newLevel == 0) {
                    world.setBlockState(pos, Blocks.CAULDRON.getDefaultState(), 3);
                } else {
                    world.setBlockState(pos, state.with(LeveledCauldronBlock.LEVEL, newLevel), 3);
                }
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    public static void registerBehaviour() {
        // Register behavior for interacting with an empty cauldron with a milk bucket
        CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR.put(Items.MILK_BUCKET, FILL_WITH_POTION);

        // Register behavior for interacting with an empty cauldron with glowstone dust
        CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR.put(Items.GLOWSTONE_DUST, (state, world, pos, player, hand, stack) -> {
            if (!world.isClient) {
                stack.decrement(1);
                if (!player.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE))) {
                    player.dropItem(new ItemStack(Items.GLASS_BOTTLE), false);
                }
                world.setBlockState(pos, ModBlocks.POTION_CAULDRON.getDefaultState().with(LeveledCauldronBlock.LEVEL, 1), 3);
                world.playSound(null, pos, SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
            return ActionResult.SUCCESS;
        });

        // Register custom behaviors for potion cauldron
        POTION_CAULDRON_BEHAVIOR.put(Items.MILK_BUCKET, FILL_WITH_POTION);
        POTION_CAULDRON_BEHAVIOR.put(Items.GLOWSTONE_DUST, (state, world, pos, player, hand, stack) -> {
            int level = state.get(LeveledCauldronBlock.LEVEL);
            if (level < 3) {
                if (!world.isClient) {
                    stack.decrement(1);
                    if (!player.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE))) {
                        player.dropItem(new ItemStack(Items.GLASS_BOTTLE), false);
                    }
                    world.setBlockState(pos, state.with(LeveledCauldronBlock.LEVEL, level + 1), 3);
                    world.playSound(null, pos, SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
                return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        });
        POTION_CAULDRON_BEHAVIOR.put(Items.GLASS_BOTTLE, (state, world, pos, player, hand, stack) -> {
            int level = state.get(LeveledCauldronBlock.LEVEL);
            if (level > 0) {
                if (!world.isClient) {
                    stack.decrement(1);
                    if (!player.getInventory().insertStack(new ItemStack(Items.GLOWSTONE_DUST))) {
                        player.dropItem(new ItemStack(Items.GLOWSTONE_DUST), false);
                    }
                    int newLevel = level - 1;
                    if (newLevel == 0) {
                        world.setBlockState(pos, Blocks.CAULDRON.getDefaultState(), 3);
                    } else {
                        world.setBlockState(pos, state.with(LeveledCauldronBlock.LEVEL, newLevel), 3);
                    }
                    world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
                return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        });
    }
}
