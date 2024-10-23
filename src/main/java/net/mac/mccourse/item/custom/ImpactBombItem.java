package net.mac.mccourse.item.custom;

import net.mac.mccourse.entity.custom.CustomisableBombEntity;
import net.mac.mccourse.entity.custom.DynamiteEntity;
import net.mac.mccourse.entity.custom.ShardEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ImpactBombItem extends Item {
    private CustomisableBombEntity.TriggerType triggerType;
    private CustomisableBombEntity.BlastType blastType;
    private int fuse, blastPower;

    public ImpactBombItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        throwBomb(world, user);
        return super.use(world, user, hand);
    }

    private void throwBomb(World world, PlayerEntity player){
        ShardEntity shard = new ShardEntity(player, world);
        world.spawnEntity(shard);
    }
}
