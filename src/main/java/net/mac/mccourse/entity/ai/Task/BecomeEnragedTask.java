package net.mac.mccourse.entity.ai.Task;



import com.google.common.collect.ImmutableMap;
import net.mac.mccourse.entity.ai.PorcupineBrain;
import net.mac.mccourse.entity.custom.PorcupineEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;


public class BecomeEnragedTask extends MultiTickTask<PorcupineEntity> {


    public BecomeEnragedTask() {
        super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_PRESENT));
    }

    @Override
    protected boolean shouldRun(ServerWorld world, PorcupineEntity entity) {
        return entity.getHealth() <= (entity.getMaxHealth() / 2);
    }

    @Override
    protected void run(ServerWorld world, PorcupineEntity entity, long time) {
        super.run(world, entity, time);
        PorcupineBrain.onEnrage(entity);
        world.spawnParticles(ParticleTypes.SONIC_BOOM, entity.getX(), entity.getY(), entity.getZ(), 1, 0, 0, 0, 0);
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_WARDEN_SONIC_BOOM, SoundCategory.NEUTRAL, 1, 1);
        getTarget(entity).sendMessage(Text.literal("You Pissed It Off!"));
     }

    public LivingEntity getTarget(LivingEntity entity){
        return entity.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).get();
    }
}


