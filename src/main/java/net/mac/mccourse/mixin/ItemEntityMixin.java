package net.mac.mccourse.mixin;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.entity.ModEntities;
import net.mac.mccourse.entity.custom.PorcupineEntity;
import net.mac.mccourse.entity.custom.SoulmellowEntity;
import net.mac.mccourse.item.ModItems;
import net.mac.mccourse.util.FireworkStarUtil;
import net.mac.mccourse.util.ThrownByEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin implements ThrownByEntity {

    @Inject(method = "tick", at = @At("TAIL"))
    private void initSetThrower(CallbackInfo ci) {
        ItemEntity item = (ItemEntity) (Object) this;
        if (item != null) { // If the item is real
            if (item.getWorld() instanceof ServerWorld server) { // Run on server-side
                if (item.getStack().isOf(ModItems.MARSHMALLOW)) { // If Item has owner
                    if (item.getOwner() != null) { // if Item is of marshmallow
                        //if (isNearSoulSource(item, item.getWorld(), item.getBlockPos())) { // if is near souls
                        if (item.getItemAge() >= 100) {
                            if (item.getItemAge() % 5 == 0)
                                server.spawnParticles(ParticleTypes.SCULK_SOUL, item.getX(), item.getY(), item.getZ(), 1, 0.1, 0.1, 0.1, 0.02);
                            if (item.getItemAge() % 25 == 0)
                                item.playSound(SoundEvents.PARTICLE_SOUL_ESCAPE, 7.0f, 0.8f);
                            if (item.getItemAge() % 35 == 0)
                                item.playSound(SoundEvents.ENTITY_WARDEN_HEARTBEAT, 10.0f, 0.5f);


                            if (item.getItemAge() >= 400) { // if age is of time
                                item.getWorld().playSound(null, item.getX(), item.getY(), item.getZ(), SoundEvents.ENTITY_SHULKER_BULLET_HIT, SoundCategory.NEUTRAL, 1, 2);
                                item.playSound(SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, 20.0f, 3f);
                                item.discard();
                                PorcupineEntity porcupineEntity = ModEntities.PORCUPINE.spawn(server, item.getBlockPos(), SpawnReason.CONVERSION);
                                if (porcupineEntity == null) return;
                                porcupineEntity.setOwner((PlayerEntity) item.getOwner());
                                server.spawnParticles(ParticleTypes.EXPLOSION, item.getX(), item.getY(), item.getZ(), 1, 0, 0, 0, 0);
                                //}
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean isNearSoulSource(ItemEntity item, World world, BlockPos pos) {
        RegistryKey<Biome> biomeKey = world.getBiome(pos).getKey().orElse(null);
        return BiomeKeys.SOUL_SAND_VALLEY.equals(biomeKey);
    }
}
