package net.raptorzizi.wind_spellbooks.entity.spells.iron_slash;

import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.raptorzizi.wind_spellbooks.registries.ModEntityRegistry;

import java.util.ArrayList;
import java.util.List;

public class IronSlashEntity extends Entity implements IEntityAdditionalSpawnData {

    public static final int DELAY_BEFORE_DAMAGE = 15;

    private LivingEntity caster;
    private Vec3 start = Vec3.ZERO;
    private Vec3 end   = Vec3.ZERO;
    private float damage;
    private boolean iceMode = false;

    private final List<LivingEntity> hitEntities = new ArrayList<>();
    private static final float SLASH_WIDTH  = 1.5f;
    private static final float SLASH_HEIGHT = 2.0f;

    public IronSlashEntity(EntityType<? extends IronSlashEntity> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public IronSlashEntity(Level level, LivingEntity caster,
                           Vec3 start, Vec3 end, float damage, boolean iceMode) {
        this(ModEntityRegistry.IRON_SLASH_ENTITY.get(), level);
        this.caster  = caster;
        this.start   = start;
        this.end     = end;
        this.damage  = damage;
        this.iceMode = iceMode;
        this.setPos(start);
        Vec3 dir = end.subtract(start).normalize();
        this.setYRot((float) Math.toDegrees(Math.atan2(dir.x, dir.z)) * -1);
        this.setXRot((float) Math.toDegrees(Math.atan2(dir.y,
                Math.sqrt(dir.x * dir.x + dir.z * dir.z))) * -1);
    }

    public Vec3 getStart() { return start; }
    public Vec3 getEnd()   { return end; }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) return;

        if (tickCount == 1) {
            collectHitEntities();
        }

        if (tickCount >= DELAY_BEFORE_DAMAGE) {
            applyDamageToAll();
            discard();
        }
    }

    private void collectHitEntities() {
        Vec3 dir = end.subtract(start);
        double dashLength = dir.length();
        if (dashLength < 0.01) return;
        Vec3 dirNorm = dir.normalize();

        double radius = Math.max(SLASH_WIDTH, dashLength / 2.0 + SLASH_WIDTH);
        Vec3 center = start.add(dir.scale(0.5));
        List<LivingEntity> candidates = level().getEntitiesOfClass(
                LivingEntity.class,
                new AABB(center, center).inflate(radius + SLASH_HEIGHT),
                e -> e != caster && e.isAlive() && (caster == null || !e.isAlliedTo(caster))
        );

        for (LivingEntity entity : candidates) {
            Vec3 toEntity = entity.position().subtract(start);

            double projection = toEntity.dot(dirNorm);
            if (projection < -SLASH_HEIGHT || projection > dashLength + SLASH_HEIGHT) continue;

            Vec3 projected = dirNorm.scale(projection);
            double perpDist = toEntity.subtract(projected).length();

            if (perpDist <= SLASH_WIDTH + entity.getBbWidth() / 2.0) {
                hitEntities.add(entity);
            }
        }
    }

    private void applyDamageToAll() {
        if (caster == null) return;
        for (LivingEntity target : hitEntities) {
            if (!target.isAlive()) continue;
            target.hurt(target.damageSources().mobAttack(caster), damage);
            target.addDeltaMovement(new Vec3(0, 0.3, 0));
            target.hurtMarked = true;

            level().playSound(null,
                    target.getX(), target.getY(), target.getZ(),
                    SoundRegistry.BLOOD_NEEDLE_IMPACT.get(),
                    target.getSoundSource(),
                    1.0f, 0.8f + (float)(Math.random() * 0.4f));

            if (iceMode) {
                target.addEffect(new MobEffectInstance(
                        MobEffectRegistry.CHILLED.get(),
                        (int)(target.getTicksRequiredToFreeze() * 2),
                        1, false, false, true));

                target.setTicksFrozen(target.getTicksRequiredToFreeze() * 2);

                MagicManager.spawnParticles(
                        level(),
                        io.redspace.ironsspellbooks.util.ParticleHelper.ICY_FOG,
                        target.getX(), target.getY(), target.getZ(),
                        6, 0.3, 0, 0.3, 0.05, false);

                MagicManager.spawnParticles(
                        level(),
                        io.redspace.ironsspellbooks.util.ParticleHelper.SNOWFLAKE,
                        target.getX(),
                        target.getY() + target.getBbHeight() * 0.5f,
                        target.getZ(),
                        50,
                        target.getBbWidth() * 0.5f,
                        target.getBbHeight() * 0.5f,
                        target.getBbWidth() * 0.5f,
                        0.3, false);
            }

            SlashEffectEntity effect = new SlashEffectEntity(
                    level(), target, this.getYRot(), iceMode);
            level().addFreshEntity(effect);
        }
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.scalable(SLASH_WIDTH, SLASH_HEIGHT);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeDouble(start.x);
        buffer.writeDouble(start.y);
        buffer.writeDouble(start.z);
        buffer.writeDouble(end.x);
        buffer.writeDouble(end.y);
        buffer.writeDouble(end.z);
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buffer) {
        this.start = new Vec3(buffer.readDouble(), buffer.readDouble()-0.2, buffer.readDouble());
        this.end   = new Vec3(buffer.readDouble(), buffer.readDouble()-0.2, buffer.readDouble());
        this.setPos(start);
        Vec3 dir = end.subtract(start).normalize();
        this.setYRot((float) Math.toDegrees(Math.atan2(dir.x, dir.z)) * -1);
        this.setXRot((float) Math.toDegrees(Math.atan2(dir.y, Math.sqrt(dir.x * dir.x + dir.z * dir.z))) * -1);
    }

    @Override protected void defineSynchedData() {}
    @Override protected void readAdditionalSaveData(CompoundTag tag) {}
    @Override protected void addAdditionalSaveData(CompoundTag tag) {}
}
