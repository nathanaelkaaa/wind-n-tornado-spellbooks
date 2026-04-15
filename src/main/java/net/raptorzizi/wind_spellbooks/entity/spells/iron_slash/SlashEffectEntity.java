package net.raptorzizi.wind_spellbooks.entity.spells.iron_slash;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import net.raptorzizi.wind_spellbooks.registries.ModEntityRegistry;

public class SlashEffectEntity extends Entity implements IEntityWithComplexSpawn {

    public static final int LIFETIME = 10;
    private float yRot;
    private float randomRotOffset;
    private boolean iceMode = false;

    public SlashEffectEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public SlashEffectEntity(Level level, Entity target, float yRot, boolean iceMode) {
        this(ModEntityRegistry.SLASH_EFFECT_ENTITY.get(), level);
        this.yRot = yRot;
        this.iceMode = iceMode;
        this.randomRotOffset = (float)(Math.random() * 60f - 30f);
        this.setPos(target.getX(), target.getY() + target.getBbHeight() / 2f, target.getZ());
    }

    public boolean isIceMode() { return iceMode; }
    public float getSlashYRot() { return yRot; }
    public float getRandomRotOffset() { return randomRotOffset; }

    @Override
    public void tick() {
        super.tick();
        if (tickCount >= LIFETIME) discard();
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.scalable(0.1f, 0.1f);
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        buffer.writeFloat(yRot);
        buffer.writeFloat(randomRotOffset);
        buffer.writeBoolean(iceMode);
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf buffer) {
        this.yRot = buffer.readFloat();
        this.randomRotOffset = buffer.readFloat();
        this.iceMode = buffer.readBoolean();
    }

    @Override
    public boolean shouldBeSaved() { return false; }

    @Override protected void defineSynchedData(SynchedEntityData.Builder builder) {}
    @Override protected void readAdditionalSaveData(CompoundTag tag) {}
    @Override protected void addAdditionalSaveData(CompoundTag tag) {}
}