package net.mac.mccourse;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.mac.mccourse.network.DashPacket;
import net.mac.mccourse.network.DoubleJumpPacket;
import net.mac.mccourse.util.DashUtil;
import net.mac.mccourse.util.DoubleJumpUtil;
import net.mac.mccourse.util.IPlayerDashSaver;
import net.mac.mccourse.util.IPlayerDoubleJumpSaver;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class DoubleJumpKeybind {
    private static KeyBinding dashKeyBinding;

    public static void registerKeybind() {
        // Initialize and register the KeyBinding
        dashKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.mccourse.double_jump_key",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_TAB,
                "key.mccourse.category"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (dashKeyBinding.wasPressed()) {
                onDoubleJumpKeyPressed();
            }
        });
    }

    private static void onDoubleJumpKeyPressed() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        DoubleJumpPacket.sendDoubleJumpPacket();
        IPlayerDoubleJumpSaver playerJumpData = (IPlayerDoubleJumpSaver) player;
        if (DoubleJumpUtil.canJump(playerJumpData, player)){
            DoubleJumpUtil.use(playerJumpData, player);
        }
    }
}
