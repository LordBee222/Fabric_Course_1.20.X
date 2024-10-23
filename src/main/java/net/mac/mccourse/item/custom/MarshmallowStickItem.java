package net.mac.mccourse.item.custom;

import net.mac.mccourse.item.ModFoodComponents;
import net.minecraft.block.CampfireBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MarshmallowStickItem extends Item {
    private static final int TOASTED_TIME = 100, GOLDEN_TIME = 300, BURNT_TIME = 400;
    private static final String COOK_TIME_KEY = "CookTime", STATE_KEY = "State";

    public MarshmallowStickItem(Settings settings) {
        super(settings);
    }

/********************************************************************************************************
 /// Custom Behavior Methods
 /// isNearCampfire: Checks if player is close to a campfire block
 /// createMarshmallowNbt: Creates nbt from a State and a Cook Time
 /*********************************************************************************************************/
    private boolean isNearCampfire(World world, PlayerEntity player) {
        BlockPos playerPos = player.getBlockPos();
        for (BlockPos pos : BlockPos.iterate(playerPos.add(-1, -1, -1), playerPos.add(1, 1, 1))) {
            if (world.getBlockState(pos).getBlock() instanceof CampfireBlock) {
                return true;
            }
        }
        return false;
    }

    private void updateMarshmallowState(ItemStack stack, int cookTime) {
        String state = cookTime >= BURNT_TIME ? "burnt" : cookTime >= GOLDEN_TIME ? "golden" : cookTime >= TOASTED_TIME ? "toasted" : "uncooked";
        stack.getOrCreateNbt().putInt(COOK_TIME_KEY, cookTime);
        stack.getOrCreateNbt().putString(STATE_KEY, state);
    }

/********************************************************************************************************
 /// Overrides
 /// Methods: inventoryTick, appendTooltip, allowNbtUpdateAnimation, isFood, getFoodComponent
 /*********************************************************************************************************/
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient && selected && entity instanceof PlayerEntity player && isNearCampfire(world, player)) {
            int cookTime = stack.getOrCreateNbt().getInt(COOK_TIME_KEY) + 1;
            if (cookTime <= BURNT_TIME) {
                updateMarshmallowState(stack, cookTime);
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (stack.hasNbt() && stack.getNbt().contains(STATE_KEY)) {
            tooltip.add(Text.translatable("mccourse.item.marshmallow_on_stick.tooltip." + stack.getOrCreateNbt().getString(STATE_KEY)));
        }
    }

    @Override
    public boolean allowNbtUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
        return false;
    }

    @Override
    public boolean isFood() {
        return true;
    }

    @Nullable
    @Override
    public FoodComponent getFoodComponent() {
        return ModFoodComponents.MARSHMALLOW;
    }
}
