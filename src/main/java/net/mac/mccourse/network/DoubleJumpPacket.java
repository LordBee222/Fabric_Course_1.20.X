package net.mac.mccourse.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.util.DashUtil;
import net.mac.mccourse.util.DoubleJumpUtil;
import net.mac.mccourse.util.IPlayerDashSaver;
import net.mac.mccourse.util.IPlayerDoubleJumpSaver;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class DoubleJumpPacket {
    public static final Identifier DOUBLE_JUMP_PACKET_ID = new Identifier(MCCourseMod.MOD_ID, "double_jump");


    public static void registerReceivers() {
        ServerPlayNetworking.registerGlobalReceiver(DOUBLE_JUMP_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                if (player != null) {
                    preformDoubleJump(player);
                }
            });
        });
    }

    public static void preformDoubleJump(PlayerEntity user){
        IPlayerDoubleJumpSaver playerJumpData = (IPlayerDoubleJumpSaver) user;
        if (DoubleJumpUtil.canJump(playerJumpData, user)){
            DoubleJumpUtil.use(playerJumpData, user);
            MCCourseMod.LOGGER.info("DID JUMP");
        }
    }

    public static void sendDoubleJumpPacket() {
        ClientPlayNetworking.send(DOUBLE_JUMP_PACKET_ID, new PacketByteBuf(Unpooled.buffer()));
    }
}
