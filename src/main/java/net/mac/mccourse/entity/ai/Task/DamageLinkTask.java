package net.mac.mccourse.entity.ai.Task;



import com.google.common.collect.ImmutableMap;
import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.entity.ai.ModMemoryModuleTypes;
import net.mac.mccourse.entity.ai.PorcupineBrain;
import net.mac.mccourse.entity.custom.PorcupineEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;


public class DamageLinkTask extends MultiTickTask<PorcupineEntity> {
    private final float damageRedirected; // Amount of damage to redirect
    private final int runTime;

    public DamageLinkTask(float damageRedirected, int runTime) {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_PRESENT,
                ModMemoryModuleTypes.DAMAGE_LINK_COOLDOWN, MemoryModuleState.VALUE_ABSENT,
                ModMemoryModuleTypes.ATTACK_ON_COOLDOWN, MemoryModuleState.VALUE_ABSENT), runTime);
        this.damageRedirected = damageRedirected;
        this.runTime = runTime;
    }

    @Override
    protected boolean shouldRun(ServerWorld world, PorcupineEntity entity) {
        LivingEntity owner = entity.getOwner();
        return entity.getBrain().hasMemoryModule(MemoryModuleType.ATTACK_TARGET) &&
                owner != null &&
                owner.isAlive() &&
                entity.distanceTo(owner) <= 10;
    }

    @Override
    protected void run(ServerWorld world, PorcupineEntity entity, long time) {
        super.run(world, entity, time);
        entity.getBrain().remember(ModMemoryModuleTypes.DAMAGE_LINK_TICKS, this.runTime);
        entity.setDamageRedirected(this.damageRedirected); // Set amount of damage to redirect
        entity.setDamageLinkActive(true); // Enable damage link
        MCCourseMod.LOGGER.info("Damage Link Activated");
    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld world, PorcupineEntity entity, long time) {
        LivingEntity owner = entity.getOwner();
        return entity.getBrain().getOptionalMemory(ModMemoryModuleTypes.DAMAGE_LINK_TICKS).orElse(0) > 0 &&
                owner != null &&
                owner.isAlive() &&
                entity.distanceTo(owner) <= 20;
    }

    @Override
    protected void keepRunning(ServerWorld world, PorcupineEntity entity, long time) {
        super.keepRunning(world, entity, time);
        LivingEntity owner = entity.getOwner();
        if (owner != null) {
            // You could add status effects or other visual indicators if desired
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 20, 0));
            owner.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 20, 0));
        }
    }

    @Override
    protected void finishRunning(ServerWorld world, PorcupineEntity entity, long time) {
        super.finishRunning(world, entity, time);
        entity.setDamageLinkActive(false); // Disable damage link
        PorcupineBrain.postAttack(entity, ModMemoryModuleTypes.DAMAGE_LINK_COOLDOWN, 100);
        MCCourseMod.LOGGER.info("Damage Link Deactivated");
    }
}
