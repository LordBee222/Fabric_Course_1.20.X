package net.mac.mccourse.enchantment;

import net.mac.mccourse.MCCourseMod;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEnchantments {
    public static final Enchantment LIGHTNING_STRIKER = register("lightning_striker",
            new LightningStrikerEnchantment(Enchantment.Rarity.COMMON,
                    EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND));

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
