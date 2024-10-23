package net.mac.mccourse.effect;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.util.IEntityRiverStyxDashSaver;
import net.mac.mccourse.util.IPlayerDashSaver;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class UnleashedEffect extends StatusEffect {
    private static final String RIVER_STYX_DASHES_KEY = "river_styx_dashes";


    protected UnleashedEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int i = 100 >> amplifier;
        if (i > 0){
            return duration % i == 0;
        } else {
            return false;
        }
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {

        // Create Dash Charges Int (Not Yet Initialized)
        int riverStyxDashCharges;

        // Convert LivingEntity entity to a IEntityRiverStyxDashSaver, required to getDashData
        IEntityRiverStyxDashSaver riverStyxDashEntity = (IEntityRiverStyxDashSaver) entity;

        // Initialized riverStyxDashCharges By getting from NBT, if null == 0
        riverStyxDashCharges = riverStyxDashEntity.getDashData().get(RIVER_STYX_DASHES_KEY) != null ? riverStyxDashEntity.getDashData().getInt(RIVER_STYX_DASHES_KEY) : 0;

        // If Space for charge, add 1
        if (riverStyxDashCharges < amplifier + 1) riverStyxDashCharges++;

        // Save to the IEntityRiverStyxDashSaver entity
        riverStyxDashEntity.getDashData().putInt(RIVER_STYX_DASHES_KEY, riverStyxDashCharges);

        // Logger (For Debugging)
        MCCourseMod.LOGGER.info(
                "Added +1 Dash to entity, Current Dashes: " + riverStyxDashCharges + ". Extra Data; " +
                "Duration: " + entity.getStatusEffect(ModEffects.RIVER_STYX).getDuration() +
                ", Amplifier: " + entity.getStatusEffect(ModEffects.RIVER_STYX).getAmplifier());
    }
}
