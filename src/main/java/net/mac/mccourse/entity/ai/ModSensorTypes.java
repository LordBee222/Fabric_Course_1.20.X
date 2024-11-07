package net.mac.mccourse.entity.ai;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.entity.ai.Sensors.SoulmellowAttackablesSensor;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.AxolotlAttackablesSensor;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.function.Supplier;

public class ModSensorTypes {
    public static final SensorType<SoulmellowAttackablesSensor> SOULMELLOW_ATTACKABLES = register("soulmellow_attackables", SoulmellowAttackablesSensor::new);

    private static <U extends Sensor<?>> SensorType<U> register(String id, Supplier<U> factory) {
        return Registry.register(Registries.SENSOR_TYPE, new Identifier(id), new SensorType<U>(factory));
    }


    public static void registerModSensors(){
        MCCourseMod.LOGGER.info("Registering Mod Memory Modules for " + MCCourseMod.MOD_ID);
    }
}
