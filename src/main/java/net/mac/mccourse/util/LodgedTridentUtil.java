package net.mac.mccourse.util;

import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.mixin.TridentEntityAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Unique;


public class LodgedTridentUtil {
    public static final String LODGED_TRIDENTS_KEY = "lodged_tridents";
    public static final String LODGED_TRIDENT_OWNER_KEY = "lodged_trident_owner";

    public static TrackedData<Boolean> HAS_LODGED_TRIDENT;

    public static void getDataTrackerFromInit(TrackedData data){
        HAS_LODGED_TRIDENT = data;
    }


    public static boolean hasLodgedTrident(LivingEntity entity) {
        MCCourseMod.LOGGER.info(((IEntityLodgedTridentSaver) entity).getLodgedTridents().contains(LODGED_TRIDENTS_KEY) + "Has Lodged Trident?");
        return ((IEntityLodgedTridentSaver) entity).getLodgedTridents().contains(LODGED_TRIDENTS_KEY);
    }

    public static void setLodgedTrident(LivingEntity victim, Entity attacker, ItemStack tridentStack) {
        victim.getDataTracker().set(HAS_LODGED_TRIDENT, true);
        ((IEntityLodgedTridentSaver) victim).getLodgedTridents().put(LODGED_TRIDENTS_KEY, attacker.writeNbt(new NbtCompound()));
        ((IEntityLodgedTridentSaver) victim).getLodgedTridents().put(LODGED_TRIDENTS_KEY, tridentStack.writeNbt(new NbtCompound()));

    }

    public static NbtCompound getLodgedTridents(LivingEntity entity){
        return ((IEntityLodgedTridentSaver) entity).getLodgedTridents();
    }

    public static ItemStack getTridentEntityStack(TridentEntity trident){
        return ((TridentEntityAccessor) trident).getTridentStack();
    }

    public static boolean getDataTrackedHasTrident(LivingEntity entity){
        return entity.getDataTracker().get(HAS_LODGED_TRIDENT);
    }









    public static TridentEntity createTridentEntity(LivingEntity entity){
        World world = entity.getWorld();
        MCCourseMod.LOGGER.info("TRING TO GET OWNER");
        LivingEntity owner = ((ILodgedTrident)entity).getLodgedTridentOwner();
        MCCourseMod.LOGGER.info("PASSED");
        ItemStack stack = getLodgedTridentStack(entity);
        MCCourseMod.LOGGER.info("Stack Data: " + stack.getNbt().asString());
        return new TridentEntity(world, owner, stack);
    }

    public static ItemStack getLodgedTridentStack(LivingEntity entity) {
        NbtCompound tridentStackCompound = ((ILodgedTrident)entity).getLodgedTridentData();
        MCCourseMod.LOGGER.info("Data: " + tridentStackCompound);
        if (tridentStackCompound.isEmpty()) return ItemStack.EMPTY;
        ItemStack stack = new ItemStack(Items.TRIDENT, 1);
        stack.setNbt(tridentStackCompound);
        //return ItemStack.fromNbt(tridentStackCompound);
        return stack;

//        return !tridentStackCompound.isEmpty() ? ItemStack.fromNbt(tridentStackCompound) : ItemStack.EMPTY;
    }
}
