package net.mac.mccourse.entity.ai;

import com.google.common.collect.ImmutableList;
import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.entity.custom.PorcupineEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;

public class PorcupineUtil {
    public static final ImmutableList<? extends SensorType<? extends Sensor<? super PorcupineEntity>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ADULT, ModSensorTypes.SOULMELLOW_ATTACKABLES);
    public static final ImmutableList<? extends MemoryModuleType<?>> MEMORY_MODULE_TYPES = (ImmutableList<? extends MemoryModuleType<?>>) ImmutableList.of(MemoryModuleType.BREED_TARGET, MemoryModuleType.MOBS, MemoryModuleType.VISIBLE_MOBS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, MemoryModuleType.NEAREST_ATTACKABLE, MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.AVOID_TARGET, MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryModuleType.NEAREST_REPELLENT, MemoryModuleType.PACIFIED, ModMemoryModuleTypes.FIREBALL_COOLDOWN, ModMemoryModuleTypes.UNLEASHED_SOULS_COOLDOWN, ModMemoryModuleTypes.POSSESSION_COOLDOWN, ModMemoryModuleTypes.CALL_OF_THE_DEAD_COOLDOWN, ModMemoryModuleTypes.ATTACK_ON_COOLDOWN, ModMemoryModuleTypes.POSSESSION_TIME_OUT, ModMemoryModuleTypes.POSSESSION_WAVES_FIRED, ModMemoryModuleTypes.LIFEFORCE_TIME, ModMemoryModuleTypes.REDIRECTED_DAMAGE_TAKEN, ModMemoryModuleTypes.REDIRECT_DAMAGE_COOLDOWN);

    public static void initOwner(PorcupineEntity porcupine, PlayerEntity owner){
    }

    public static void initMemories(Brain<PorcupineEntity> brain, PlayerEntity owner){

    }

    public static void onTick(PorcupineEntity porcupine){
    }

    public static void onAttack(){

    }

    public static void onHurt(){

    }

    public static void setOnCooldown(PorcupineEntity porcupine, MemoryModuleType module, int cooldown){
    }

    public static void setProvoked(PorcupineEntity porcupine, PorcupineStages stage){
    }

    public static void setUndisturbed(PorcupineEntity porcupine, PorcupineStages stage){
    }





    public enum PorcupineStages{
        UNDISTURBED,
        PROVOKED
    }
}


