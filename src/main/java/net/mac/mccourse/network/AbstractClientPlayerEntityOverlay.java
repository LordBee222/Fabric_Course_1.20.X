package net.mac.mccourse.network;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.feature.SkinOverlayOwner;
import net.minecraft.client.world.ClientWorld;

public class AbstractClientPlayerEntityOverlay extends AbstractClientPlayerEntity implements SkinOverlayOwner {
    public AbstractClientPlayerEntityOverlay(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Override
    public boolean shouldRenderOverlay() {
        return true;
    }
}
