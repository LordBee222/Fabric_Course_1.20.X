package net.mac.mccourse.enchantment;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.enchantment.ceremonial_blade.BloodTaxEnchantment;
import net.mac.mccourse.enchantment.ceremonial_blade.SacrificeEnchantment;
import net.mac.mccourse.enchantment.trident.*;
import net.mac.mccourse.item.ModItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEnchantments {
    public static final Enchantment SPLASHING = register("splashing",
            new SplashingEnchantment(Enchantment.Rarity.COMMON,
                    EnchantmentTarget.TRIDENT, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND}));

    public static final Enchantment LIGHTWEIGHT = register("lightweight",
            new LightweightEnchantment(Enchantment.Rarity.COMMON,
                    EnchantmentTarget.TRIDENT, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND}));

    public static final Enchantment SCATTER_THROW = register("scatter_throw",
            new ScatterThrowEnchantment(Enchantment.Rarity.COMMON,
                    EnchantmentTarget.TRIDENT, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND}));

    public static final Enchantment RESERVOIR = register("reservoir",
            new ReservoirEnchantment(Enchantment.Rarity.COMMON,
                    EnchantmentTarget.TRIDENT, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND}));

    public static final Enchantment IMPACT = register("impact",
            new ImpactEnchantment(Enchantment.Rarity.COMMON,
                    EnchantmentTarget.TRIDENT, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND}));

    public static final Enchantment OCEANS_WRATH = register("oceans_wrath",
            new OceansWrathEnchantment(Enchantment.Rarity.COMMON,
                    EnchantmentTarget.TRIDENT, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND}));

    public static final Enchantment SACRIFICE = register("sacrifice",
            new SacrificeEnchantment(Enchantment.Rarity.COMMON,
                    EnchantmentTarget.TRIDENT, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND}));
    public static final Enchantment BLOOD_TAX = register("blood_tax",
            new BloodTaxEnchantment(Enchantment.Rarity.COMMON,
                    EnchantmentTarget.TRIDENT, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND}));

    public static final Enchantment GALEFORCE = register("galeforce",
            new GaleforceEnchantment(Enchantment.Rarity.COMMON,
                    EnchantmentTarget.ARMOR_LEGS, new EquipmentSlot[]{EquipmentSlot.LEGS}));

    public static final Enchantment CLOUDWALKER = register("cloudwalker",
            new CloudwalkerEnchantment(Enchantment.Rarity.COMMON,
                    EnchantmentTarget.ARMOR_FEET, new EquipmentSlot[]{EquipmentSlot.FEET}));


    public static final Enchantment LIGHTNING_STRIKER = register("lightning_striker",
            new LightningStrikerEnchantment(Enchantment.Rarity.COMMON,
                    EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND));

    public static final Enchantment TRIGGER_HAPPY = register("trigger_happy",
            new TriggerHappyEnchantment(Enchantment.Rarity.COMMON,
                    EnchantmentTarget.CROSSBOW, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND}));

    public static final Enchantment COMPACITY = register("compacity",
            new TriggerHappyEnchantment(Enchantment.Rarity.COMMON,
                    EnchantmentTarget.CROSSBOW, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND}));

    public static final Enchantment JUMPER = register("jumper",
            new JumperEnchantment(Enchantment.Rarity.COMMON,
                    EnchantmentTarget.CROSSBOW, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND}));

    public static final Enchantment SCOPE = register("scope",
            new ScopeEnchantment(Enchantment.Rarity.COMMON,
                    EnchantmentTarget.CROSSBOW, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND}));

    public static final Enchantment SONIC = register("sonic",
            new SonicEnchantment(Enchantment.Rarity.COMMON,
                    EnchantmentTarget.CROSSBOW, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND}));

    public static final Enchantment FORCEFUL = register("forceful",
            new ForcefulEnchantment(Enchantment.Rarity.COMMON,
                    EnchantmentTarget.CROSSBOW, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND}));

    private static Enchantment register(String name, Enchantment enchantment){
        return Registry.register(Registries.ENCHANTMENT, new Identifier(MCCourseMod.MOD_ID, name), enchantment);
    }

    public static void registerModEnchantments(){
        MCCourseMod.LOGGER.info("Registering ModEnchantments for " + MCCourseMod.MOD_ID);
    }
}
