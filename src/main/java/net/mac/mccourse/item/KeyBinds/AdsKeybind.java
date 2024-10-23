package net.mac.mccourse.item.KeyBinds;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.mac.mccourse.item.custom.CalibratedCrossbowItem;
import net.mac.mccourse.network.AdsPacket;
import net.mac.mccourse.network.DashPacket;
import net.mac.mccourse.util.DashUtil;
import net.mac.mccourse.util.IPlayerDashSaver;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class AdsKeybind {
    private static KeyBinding AdsKeyBinding;

    public static void registerKeybind() {
        AdsKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.mccourse.ads_key",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_ALT,
                "key.mccourse.category"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world != null){
                if (client.player.getMainHandStack().getItem() instanceof CalibratedCrossbowItem calibratedCrossbow)
                calibratedCrossbow.doAds(AdsKeyBinding.isPressed());
            }
        });
    }


}
