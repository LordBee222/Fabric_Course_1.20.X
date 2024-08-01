package net.mac.mccourse.util;

import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.item.ModItems;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class ModModelPredicateProvider {
    public static void registerModModels(){
        ModelPredicateProviderRegistry.register(ModItems.DATA_TABLET, new Identifier(MCCourseMod.MOD_ID, "on"),
                (stack, world, entity, seed) -> stack.hasNbt() ? 1f : 0);

        ModelPredicateProviderRegistry.register(ModItems.SCOPED_CROSSBOW, new Identifier("pull"), ((stack, world, entity, seed) -> {
            if (entity == null) return 0.0F;
            return entity.getActiveItem() != stack ? 0.0F : (stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / 20.0F;
        }));

        ModelPredicateProviderRegistry.register(ModItems.SCOPED_CROSSBOW, new Identifier("pulling"), (stack, world, entity, seed) -> {
            if (entity == null) return 0.0F;
            return entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F;
        });

        ModelPredicateProviderRegistry.register(ModItems.SCOPED_CROSSBOW, new Identifier("charged"), (stack, world, entity, seed) -> CrossbowItem.isCharged(stack) ? 1.0F : 0.0F);

        ModelPredicateProviderRegistry.register(ModItems.SCOPED_CROSSBOW, new Identifier("firework"), (stack, world, entity, seed) -> CrossbowItem.hasProjectile(stack, Items.FIREWORK_ROCKET) ? 1.0F : 0.0F);

    }
}
