package net.mac.mccourse.mixin;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityAccessor {
    @Accessor("pitch")
    void setPitch(float pitch);

    @Accessor("yaw")
    void setYaw(float yaw);

    @Invoker("setRotation")
    void callSetRotation(float yaw, float pitch);
}
