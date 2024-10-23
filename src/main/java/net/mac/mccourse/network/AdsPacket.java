package net.mac.mccourse.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.item.custom.CalibratedCrossbowItem;
import net.mac.mccourse.util.DashUtil;
import net.mac.mccourse.util.IPlayerDashSaver;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class AdsPacket {
    public static final Identifier ADS_PACKET_ID = new Identifier(MCCourseMod.MOD_ID, "ads");


    public static void registerReceivers() {
        ServerPlayNetworking.registerGlobalReceiver(ADS_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            boolean shouldAds = buf.readBoolean();

            server.execute(() -> {
                if (player != null) {
                    //CalibratedCrossbowItem.doAds(player, shouldAds);
                }
            });
        });
    }

    public static void sendPacket(boolean shouldAds) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBoolean(shouldAds);
        ClientPlayNetworking.send(ADS_PACKET_ID, buf);
    }
}
