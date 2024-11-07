package net.mac.mccourse.entity.custom;

import com.mojang.logging.LogUtils;
import net.mac.mccourse.MCCourseMod;
import net.mac.mccourse.entity.ModEntities;
import net.mac.mccourse.mixin.FallingBlockEntityAccessor;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.Heightmap;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class UnstableFallingBlockEntity extends FallingBlockEntity {
    private static final Logger LOGGER = LogUtils.getLogger();
    private BlockState block = Blocks.END_STONE.getDefaultState();
    public int timeFalling;
    public boolean dropItem = true;
    private boolean destroyedOnLanding;
    private boolean hurtEntities;
    private int fallHurtMax = 40;
    private float fallHurtAmount;
    @Nullable
    public NbtCompound blockEntityData;
    protected static final TrackedData<BlockPos> BLOCK_POS = DataTracker.registerData(UnstableFallingBlockEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);

    public UnstableFallingBlockEntity(EntityType<? extends FallingBlockEntity> entityType, World world) {
        super(entityType, world);
    }

    public void setBlock(Block block){
        this.block = block.getDefaultState();
    }

    public UnstableFallingBlockEntity(World world, double x, double y, double z, BlockState block) {
        this((EntityType<? extends FallingBlockEntity>) ModEntities.UNSTABLE_BLOCK, world);
        this.block = block;
        MCCourseMod.LOGGER.info("BLOCK: " + this.block);
        this.intersectionChecked = true;
        this.setPosition(x, y, z);
        this.setVelocity(Vec3d.ZERO);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        this.setFallingBlockPos(this.getBlockPos());
    }

    @Override
    public void tick() {
        ++this.timeFalling;
        MCCourseMod.LOGGER.info("BLOCK Tick: " + this.block);
        // Calculate the distance from the current position to the ground below
        int groundLevel = this.getWorld().getTopY(Heightmap.Type.MOTION_BLOCKING, this.getBlockPos().getX(), this.getBlockPos().getZ());
        double heightAboveGround = this.getY() - groundLevel;

        // Check if the block is within 10 blocks of the ground
        //if (heightAboveGround <= 10) {
            // If within 10 blocks, apply upward force (levitation)
            double levitationForce = 0.05; // Adjust as needed
            this.setVelocity(this.getVelocity().add(0.0, levitationForce, 0.0));

            // Move the block according to its current velocity
            this.move(MovementType.SELF, this.getVelocity());
        } //else {
            // If 10 or more blocks above the ground, freeze movement by setting velocity to zero
            //this.setVelocity(Vec3d.ZERO);
            //this.prevX = this.getX();
            //this.prevX = this.getY();
            //this.prevZ = this.getZ();
        //}

        // Handle any additional block ticking behavior
        //super.tick();
    //}

    @Override
    protected void initDataTracker() {
        super.initDataTracker();


    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        this.block = Block.getStateFromRawId(packet.getEntityData());
        this.intersectionChecked = true;
        double d = packet.getX();
        double e = packet.getY();
        double f = packet.getZ();
        this.setPosition(d, e, f);
        this.setFallingBlockPos(this.getBlockPos());
    }
}
