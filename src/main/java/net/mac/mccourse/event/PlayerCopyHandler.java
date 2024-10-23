package net.mac.mccourse.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.mac.mccourse.util.IEntityDataSaver;
import net.mac.mccourse.util.IEntityLodgedTridentSaver;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerCopyHandler implements ServerPlayerEvents.CopyFrom {
    @Override
    public void copyFromPlayer(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        //((IEntityDataSaver) newPlayer).getPersistentData().putIntArray("homepos",
              //  ((IEntityDataSaver)oldPlayer).getPersistentData().getIntArray("homepos"));

    }
}
