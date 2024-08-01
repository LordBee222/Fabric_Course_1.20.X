package net.mac.mccourse.event;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.util.FireworkStarUtil;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;


public class onUseItemHandler implements UseItemCallback {
    @Override
    public TypedActionResult<ItemStack> interact(PlayerEntity player, World world, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        Item item = stack.getItem();

        if (stack.getItem() == Items.FIREWORK_STAR && !(player.getItemCooldownManager().isCoolingDown(item)) && stack.hasNbt()) {
            if (!player.getAbilities().creativeMode) {
                stack.decrement(1);
            }
            if (stack.getNbt().contains("Explosion")) {
                player.getItemCooldownManager().set(item, 20);
            }
            return FireworkStarUtil.Explode(world, player.getX(), player.getY(), player.getZ(), stack);
        }
        return TypedActionResult.pass(player.getStackInHand(hand));
    }
}
