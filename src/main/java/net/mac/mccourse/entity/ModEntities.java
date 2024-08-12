package net.mac.mccourse.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.entity.custom.BoomSlimeEntity;
import net.mac.mccourse.entity.custom.DiceProjectileEntity;
import net.mac.mccourse.entity.custom.DynamiteEntity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<DiceProjectileEntity> THROWN_DICE_PROJECTILE = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(MCCourseMod.MOD_ID, "dice_projectile"),
            FabricEntityTypeBuilder.<DiceProjectileEntity>create(SpawnGroup.CREATURE, DiceProjectileEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f)).build());

    public static final EntityType<DynamiteEntity> DYNAMITE = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(MCCourseMod.MOD_ID, "dynamite"),
            FabricEntityTypeBuilder.<DynamiteEntity>create(SpawnGroup.CREATURE, DynamiteEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f)).build());

    public static final EntityType<BoomSlimeEntity> BOOM_SLIME = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(MCCourseMod.MOD_ID, "boom_slime"),
            FabricEntityTypeBuilder.<BoomSlimeEntity>create(SpawnGroup.CREATURE, BoomSlimeEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f)).build());

    public static void registerModEntities() {
        MCCourseMod.LOGGER.info("Registering Mod Entities for " + MCCourseMod.MOD_ID);
    }
}
