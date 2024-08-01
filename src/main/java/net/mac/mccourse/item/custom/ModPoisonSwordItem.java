package net.mac.mccourse.item.custom;

import net.mac.mccourse.util.Ticker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ModPoisonSwordItem extends SwordItem {
    public ModPoisonSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 200, 1), attacker);
        return super.postHit(stack, target, attacker);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (world instanceof ServerWorld serverWorld){
            if (!world.isClient && !(player.getItemCooldownManager().isCoolingDown(this))) {
                Vec3d lookVector = player.getRotationVec(1.0F);
                player.addVelocity(lookVector.x * 2.0D, lookVector.y * 2.0D, lookVector.z * 2.0D);
                player.velocityModified = true;
                player.getItemCooldownManager().set(this, 40);
                serverWorld.spawnParticles(ParticleTypes.EXPLOSION_EMITTER, player.getX(), player.getY(), player.getZ(), 1, 0, 0, 0, 0);
                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.NEUTRAL, 1f, 1);
                return TypedActionResult.success(player.getStackInHand(hand));
            }
        }
        return TypedActionResult.pass(player.getStackInHand(hand));
    }

    private void emitInitialSmokeCircle(ServerWorld world, PlayerEntity player) {
        Vec3d playerPos = player.getPos();
        double radius = 2.0;
        for (int i = 0; i < 360; i += 10) {
            double angle = Math.toRadians(i);
            double xOffset = Math.cos(angle) * radius;
            double zOffset = Math.sin(angle) * radius;
            Vec3d particlePos = playerPos.add(xOffset, 0, zOffset);
            world.spawnParticles(ParticleTypes.EXPLOSION_EMITTER, particlePos.x, particlePos.y, particlePos.z, 1, 0.1, 0.1, 0.1, 0.1);
        }
    }
}
