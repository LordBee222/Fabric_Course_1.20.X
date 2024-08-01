package net.mac.mccourse.mixin;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.util.FireworkStarUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        ItemEntity entity = (ItemEntity) (Object) this;
        World world = entity.getWorld();
        ItemStack stack = entity.getStack();

        if (stack.getItem() == Items.FIREWORK_STAR && stack.hasNbt()){
            BlockPos pos = entity.getBlockPos();
            BlockState blockState = world.getBlockState(pos);
            Block block = blockState.getBlock();

            if (entity.age >= 10){
                if (world.isClient()) {
                    FireworkStarUtil.ExplodeDroppedFireworkStar(world, entity.getX(), entity.getY(), entity.getZ(), stack);
                    sendPacketToServer(entity);
                }
            } else if (block == Blocks.FIRE ||
                    block == Blocks.LAVA ||
                    block == Blocks.SOUL_FIRE ||
                    (block == Blocks.CAMPFIRE && blockState.get(Properties.LIT)) ||
                    (block == Blocks.SOUL_CAMPFIRE && blockState.get(Properties.LIT))){
                if (world.isClient()){
                    FireworkStarUtil.ExplodeDroppedFireworkStar(world, entity.getX(), entity.getY(), entity.getZ(), stack);
                    sendPacketToServer(entity);
                }
            }
        }
    }

    private void sendPacketToServer(ItemEntity entity) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(entity.getId());
        Identifier packetId = new Identifier(MCCourseMod.MOD_ID, "explode_packet");
        ClientPlayNetworking.send(packetId, buf);
    }
}
