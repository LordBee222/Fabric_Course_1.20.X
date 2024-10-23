package net.mac.mccourse.item.custom;

import net.mac.mccourse.entity.custom.CustomisableBombEntity;
import net.mac.mccourse.entity.custom.DynamiteEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class BombItem extends Item {
    private CustomisableBombEntity.TriggerType triggerType;
    private CustomisableBombEntity.BlastType blastType;
    private int blastPower;

    public BombItem(Settings settings, int blastPower, CustomisableBombEntity.TriggerType triggerType, CustomisableBombEntity.BlastType blastType) {
        super(settings);
        this.blastPower = blastPower;
        this.triggerType = triggerType;
        this.blastType = blastType;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        throwBomb(world, user, user.getStackInHand(hand));
        return TypedActionResult.success(user.getMainHandStack());
    }


    private void throwBomb(World world, PlayerEntity player, ItemStack stack){
        CustomisableBombEntity bomb = new CustomisableBombEntity(player, world, this.blastPower, this.triggerType, this.blastType);
        bomb.setVelocity(player, player.getPitch(), player.getYaw(), 0.0f, 1.4F, 1.0f);
        world.spawnEntity(bomb);
    }
}
