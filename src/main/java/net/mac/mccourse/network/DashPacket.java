package net.mac.mccourse.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.util.DashUtil;
import net.mac.mccourse.util.IPlayerDashSaver;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class DashPacket {
    public static final Identifier DASH_PACKET_ID = new Identifier(MCCourseMod.MOD_ID, "dash");


    public static void registerReceivers() {
        ServerPlayNetworking.registerGlobalReceiver(DASH_PACKET_ID, (server, player, handler, buf, responseSender) -> {
             //boolean data = buf.readBoolean();
            server.execute(() -> {
                if (player != null) {
                    preformDash(player);
                }
            });
        });
    }

    public static void preformDash(PlayerEntity user){
        IPlayerDashSaver playerDashData = (IPlayerDashSaver) user;
        if (DashUtil.canDash(playerDashData, user)){
            DashUtil.use(playerDashData, user);
            MCCourseMod.LOGGER.info("DID DASH");
        }
    }

    public static void sendDashPacket() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBoolean(true);
        ClientPlayNetworking.send(DASH_PACKET_ID, new PacketByteBuf(Unpooled.buffer()));
    }
}
