package net.mac.mccourse;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.mac.mccourse.network.DashPacket;
import net.mac.mccourse.util.DashUtil;
import net.mac.mccourse.util.IPlayerDashSaver;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class DashKeybind {
    private static KeyBinding dashKeyBinding;

    public static void registerKeybind() {
        // Initialize and register the KeyBinding
        dashKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.mccourse.dash_key", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding
                GLFW.GLFW_KEY_TAB, // The keycode of the key (G key in this case)
                "key.mccourse.category" // The translation key of the keybinding's category
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (dashKeyBinding.wasPressed()) {
                onDashKeyPressed();
            }
        });
    }

    private static void onDashKeyPressed() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        DashPacket.sendDashPacket();
        IPlayerDashSaver playerDashData = (IPlayerDashSaver) player;
        if (DashUtil.canDash(playerDashData, player)){
            DashUtil.use(playerDashData, player);
        }
    }
}
