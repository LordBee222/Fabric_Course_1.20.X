package net.mac.mccourse.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.HashMap;
import java.util.Map;

public class MyDashComponent implements AutoSyncedComponent, CommonTickingComponent {
    private final PlayerEntity player;
    private boolean hasDashesLeft;
    private int dashCount;
    private final String hasDashesLeftKey = "hasDashesLeft", dashCountKey = "dashCount";


    public MyDashComponent(PlayerEntity player) {
        this.player = player;
    }


    @Override
    public void tick() {
        if (player.isOnGround()){
            resetDash();
        }
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        hasDashesLeft = tag.getBoolean(hasDashesLeftKey);
        dashCount = tag.getInt(dashCountKey);
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean(hasDashesLeftKey, hasDashesLeft);
        tag.putInt(dashCountKey, dashCount);
    }

    public void resetDash(){
        this.dashCount = 1;
    }

    public boolean canDash(){
        return !player.isOnGround() && dashCount >= 1;
    }

    public void use(){

    }
}
