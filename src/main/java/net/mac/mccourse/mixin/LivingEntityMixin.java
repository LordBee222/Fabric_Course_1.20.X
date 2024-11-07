package net.mac.mccourse.mixin;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.entity.ai.ModMemoryModuleTypes;
import net.mac.mccourse.entity.custom.PorcupineEntity;
import net.mac.mccourse.util.IOwnedSoulmellowSaver;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.UUID;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow
    protected abstract void applyDamage(DamageSource source, float amount);

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void redirectDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity owner = (LivingEntity) (Object) this;

        if (owner.getWorld() instanceof ServerWorld serverWorld) {
            PorcupineEntity porcupine = findLinkedPorcupineEntity(serverWorld, owner);

            if (porcupine != null && porcupine.isDamageLinkActive()) {
                if (!porcupine.getBrain().hasMemoryModule(ModMemoryModuleTypes.REDIRECT_DAMAGE_COOLDOWN) && amount >= 4) {

                    // Calculate the percentage of damage to redirect
                    float percentageToRedirect = porcupine.getDamageRedirected() / 100.0f;
                    float redirectedAmount = amount * percentageToRedirect;

                    // Apply the damage to the PorcupineEntity
                    porcupine.damage(porcupine.getDamageSources().generic(), redirectedAmount);

                    // Visual and memory tracking
                    Vec3d startPos = porcupine.getEyePos();
                    Vec3d endPos = owner.getEyePos();
                    Vec3d SelfToOwnerDirection = endPos.subtract(startPos).normalize();
                    castRayFromSelfToOwner(serverWorld, porcupine, startPos, SelfToOwnerDirection, endPos);

                    porcupine.getBrain().remember(ModMemoryModuleTypes.REDIRECTED_DAMAGE_TAKEN, porcupine.getBrain().getOptionalMemory(ModMemoryModuleTypes.REDIRECTED_DAMAGE_TAKEN).orElse(0f) + redirectedAmount);
                    porcupine.getBrain().remember(ModMemoryModuleTypes.REDIRECT_DAMAGE_COOLDOWN, true, 100);


                    float remainingAmount = amount - redirectedAmount;
                    MCCourseMod.LOGGER.info("REDIRECTED_DAMAGE_TAKEN: " + porcupine.getBrain().getOptionalMemory(ModMemoryModuleTypes.REDIRECTED_DAMAGE_TAKEN) + "! Which is +" + redirectedAmount + ". TOTAL AMOUNT SHARED: " + amount + "! REMAINING FOR PLAYER: " + remainingAmount);
                    applyDamage(source, remainingAmount);
                    cir.cancel();
                }
            }
        }
    }

    private void castRayFromSelfToOwner(ServerWorld world, PorcupineEntity entity, Vec3d startPos, Vec3d selfToOwnerDirection, Vec3d endPos) {
        double maxDistance = startPos.distanceTo(endPos);
        Vec3d finalPos = startPos.add(selfToOwnerDirection.multiply(maxDistance));
        BlockHitResult hitResult = world.raycast(new RaycastContext(startPos, finalPos, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity));
        Vec3d hitPos = hitResult.getPos();
        double step = 0.2;
        Vec3d rayDirection = hitPos.subtract(startPos).normalize();
        double distance = startPos.distanceTo(hitPos);
        for (double i = 0; i < distance; i += step) {
            Vec3d particlePos = startPos.add(rayDirection.multiply(i));
            world.spawnParticles(ParticleTypes.SCULK_SOUL, particlePos.getX(), particlePos.getY(), particlePos.getZ(), 1, 0.1, 0.1, 0.1, 0.02);
        }
    }

    // Finds the linked PorcupineEntity with an active DamageLinkTask
    private PorcupineEntity findLinkedPorcupineEntity(ServerWorld world, LivingEntity owner) {
        for (PorcupineEntity porcupine : world.getEntitiesByClass(PorcupineEntity.class, owner.getBoundingBox().expand(10), porcupine -> porcupine.getOwner() == owner)) {
            if (porcupine.isAlive()) {
                return porcupine;
            }
        }
        return null;
    }
}
