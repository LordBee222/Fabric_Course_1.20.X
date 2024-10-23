package net.mac.mccourse.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.mac.mccourse.CurvingHomingRaycast;
import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.util.FireworkStarUtil;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

public class SoulSpeakPacket {
    public static final Identifier PACKET_ID = new Identifier(MCCourseMod.MOD_ID, "soul_speak_packet");

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(PACKET_ID, (server, player, handler, buf, responseSender) -> {

            server.execute(() -> {
                ExecuteOnServer(player);
            });
        });
    }

    private static void ExecuteOnServer(ServerPlayerEntity player) {
            if (!player.getWorld().isClient){
                double range = 100.0;
                new CurvingHomingRaycast(player, range);
        }
    }

    public static void send() {
        ClientPlayNetworking.send(PACKET_ID, new PacketByteBuf(Unpooled.buffer()));
    }
}
