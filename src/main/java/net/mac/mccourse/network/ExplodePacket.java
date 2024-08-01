package net.mac.mccourse.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.util.FireworkStarUtil;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

public class ExplodePacket {

    public static final Identifier EXPLODE_PACKET_ID = new Identifier(MCCourseMod.MOD_ID, "explode_packet");

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(EXPLODE_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            int entityId = buf.readInt();


            server.execute(() -> {
                explodeOnServer(player, entityId);
            });
        });
    }

    private static void explodeOnServer(ServerPlayerEntity player, int entityId) {
        ServerWorld world = (ServerWorld) player.getWorld();
        ItemEntity entity = (ItemEntity) world.getEntityById(entityId);
        if (entity != null) {
            ItemStack itemStack = entity.getStack();
            if (itemStack.getCount() == 1){
                entity.discard();
            } else {
                itemStack.setCount(itemStack.getCount() - 1);
            }
            FireworkStarUtil.ExplodeDroppedFireworkStar(world, entity.getX(), entity.getY(), entity.getZ(), itemStack);

        }
    }
}
