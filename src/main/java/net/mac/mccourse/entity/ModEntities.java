package net.mac.mccourse.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.entity.custom.*;
import net.minecraft.entity.Entity;
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

    public static final EntityType<StickyExplosiveEntity> BOOM_HONEY = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(MCCourseMod.MOD_ID, "boom_honey"),
            FabricEntityTypeBuilder.<StickyExplosiveEntity>create(SpawnGroup.CREATURE, StickyExplosiveEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f)).trackRangeChunks(4).trackedUpdateRate(20).build());

    public static EntityType<AmethystShardEntity> AMETHYST_SHARD = Registry.register(Registries.ENTITY_TYPE,
           new Identifier(MCCourseMod.MOD_ID, "amethyst_shard"),
            FabricEntityTypeBuilder.<AmethystShardEntity>create(SpawnGroup.MISC, AmethystShardEntity::new)
                    .dimensions(EntityDimensions.changing(0.5f, 0.5f)).build());

    public static EntityType<BombEntity> BOMB = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(MCCourseMod.MOD_ID, "bomb"),
            FabricEntityTypeBuilder.<BombEntity>create(SpawnGroup.MISC, BombEntity::new)
                    .dimensions(EntityDimensions.changing(0.5f, 0.5f)).build());

    public static EntityType<CustomEndCrystalEntity> CUSTOM_CRYSTAL = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(MCCourseMod.MOD_ID, "custom_crystal"),
            FabricEntityTypeBuilder.<CustomEndCrystalEntity>create(SpawnGroup.MISC, CustomEndCrystalEntity::new)
                    .dimensions(EntityDimensions.changing(0.5f, 0.5f)).build());

    public static EntityType<CustomisableBombEntity> CUSTOM_BOMB_ENTITY = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(MCCourseMod.MOD_ID, "custom_bomb_entity"),
            FabricEntityTypeBuilder.<CustomisableBombEntity>create(SpawnGroup.MISC, CustomisableBombEntity::new)
                    .dimensions(EntityDimensions.changing(0.5f, 0.5f)).build());

    public static EntityType<ShardEntity> SHARD = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(MCCourseMod.MOD_ID, "shard"),
            FabricEntityTypeBuilder.<ShardEntity>create(SpawnGroup.MISC, ShardEntity::new)
                    .dimensions(EntityDimensions.changing(0.5f, 0.5f)).build());

    public static EntityType<SoulmellowEntity> SOULMELLOW = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(MCCourseMod.MOD_ID, "soulmellow"),
            FabricEntityTypeBuilder.<SoulmellowEntity>create(SpawnGroup.MISC, SoulmellowEntity::new)
                    .dimensions(EntityDimensions.changing(0.5f, 0.5f)).build());

    public static EntityType<SoulRocketProjectile> SOUL_ROCKET_PROJECTILE = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(MCCourseMod.MOD_ID, "soul_rocket_projectile"),
            FabricEntityTypeBuilder.<SoulRocketProjectile>create(SpawnGroup.MISC, SoulRocketProjectile::new)
                    .dimensions(EntityDimensions.changing(0.5f, 0.5f)).build());

    public static final EntityType<PorcupineEntity> PORCUPINE = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(MCCourseMod.MOD_ID, "porcupine"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, PorcupineEntity::new)
                    .dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build());


    public static void registerModEntities() {
        MCCourseMod.LOGGER.info("Registering Mod Entities for " + MCCourseMod.MOD_ID);

    }
}
