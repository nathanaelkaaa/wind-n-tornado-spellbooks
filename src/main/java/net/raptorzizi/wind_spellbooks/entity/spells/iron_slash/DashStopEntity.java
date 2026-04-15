package net.raptorzizi.wind_spellbooks.entity.spells.iron_slash;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.raptorzizi.wind_spellbooks.registries.ModEntityRegistry;

public class DashStopEntity extends Entity {

    private LivingEntity target;
    private Vec3 destination;
    private int lifetime;

    public DashStopEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
        this.setInvisible(true);
    }

    public DashStopEntity(Level level, LivingEntity target, Vec3 destination, int lifetime) {
        this(ModEntityRegistry.DASH_STOP_ENTITY.get(), level);
        this.target      = target;
        this.destination = destination;
        this.lifetime    = lifetime;
        this.setPos(destination);
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) return;

        if (target == null || !target.isAlive() || tickCount >= lifetime) {
            discard();
            return;
        }
        double distToDestination = target.position().distanceTo(destination);

        if (distToDestination < 1.5 || tickCount >= lifetime - 1) {
            target.setDeltaMovement(Vec3.ZERO);
            target.hurtMarked = true;
            target.teleportTo(destination.x, destination.y, destination.z);
            target.resetFallDistance();
            discard();
        }
    }

    @Override
    public boolean shouldBeSaved() { return false; }

    @Override protected void defineSynchedData(SynchedEntityData.Builder builder) {}
    @Override protected void readAdditionalSaveData(CompoundTag tag) {}
    @Override protected void addAdditionalSaveData(CompoundTag tag) {}
}