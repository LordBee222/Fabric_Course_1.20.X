package net.mac.mccourse.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.mac.mccourse.item.ModItems;
import net.mac.mccourse.util.ModTags;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(ItemTags.TRIMMABLE_ARMOR)
                .add(ModItems.PINK_GARNET_HELMET, ModItems.PINK_GARNET_CHESTPLATE, ModItems.PINK_GARNET_LEGGINGS, ModItems.PINK_GARNET_BOOTS);


        getOrCreateTagBuilder(ModTags.Items.SLINGER_PROJECTILES)
                .forceAddTag(ItemTags.ARROWS)
                .add(Items.SPLASH_POTION)
                .add(Items.LINGERING_POTION)
                .add(Items.END_CRYSTAL);
        }
    }
