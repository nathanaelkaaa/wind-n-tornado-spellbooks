package net.raptorzizi.wind_spellbooks.entity.spells.tornado;

import io.redspace.ironsspellbooks.damage.DamageSources;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.Tags;
import net.raptorzizi.wind_spellbooks.registries.ModEntityRegistry;
import net.raptorzizi.wind_spellbooks.registries.ModParticleRegistry;
import net.raptorzizi.wind_spellbooks.registries.ModSoundRegistry;
import net.raptorzizi.wind_spellbooks.registries.ModSpellRegistry;
import net.raptorzizi.wind_spellbooks.util.ElementCheck;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;

public class TornadoEntity extends Projectile implements GeoEntity{

    public static final int SPAWN_ANIM_DURATION = 20;
    public static final int DEATH_ANIM_DURATION = 15;
    private static final int LOOP_SOUND_DURATION = 40;

    private static final EntityDataAccessor<Float> DATA_RADIUS =
            SynchedEntityData.defineId(TornadoEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_DURATION =
            SynchedEntityData.defineId(TornadoEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_ON_FIRE =
            SynchedEntityData.defineId(TornadoEntity.class, EntityDataSerializers.BOOLEAN);

    @OnlyIn(Dist.CLIENT)
    public int fireTransitionTick = 0;

    @OnlyIn(Dist.CLIENT)
    private boolean wasOnFire = false;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private float damage = 0f;
    private int duration = 20 * 10;
    private int attractionPhase = 20 * 3;

    public TornadoEntity(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
    }

    public TornadoEntity(Level level, LivingEntity owner) {
        this(ModEntityRegistry.TORNADO_ENTITY.get(), level);
        setOwner(owner);
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        float radius = this.getRadius();
        return EntityDimensions.scalable(radius * 2.0F, radius * 1.5F);
    }

    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_RADIUS.equals(pKey)) {
            this.refreshDimensions();
        }
        if (DATA_ON_FIRE.equals(pKey) && level().isClientSide) {
            if (isTornadoOnFire() && !wasOnFire) {
                fireTransitionTick = 0;
            }
            wasOnFire = isTornadoOnFire();
        }
        super.onSyncedDataUpdated(pKey);
    }

    // Geo

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {return cache;}

    // Synced data

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_RADIUS, 4f);
        builder.define(DATA_DURATION, 20 * 10);
        builder.define(DATA_ON_FIRE, false);
    }

    // Setters / Getters

    public void setRadius(float radius) {
        if (!level().isClientSide) {
            this.getEntityData().set(DATA_RADIUS, Math.min(radius, 48));
        }
    }

    public float getRadius() {
        return getEntityData().get(DATA_RADIUS);
    }

    public void setDamage(float damage) { this.damage = damage; }

    public float getDamage() { return damage; }

    public void setDuration(int duration) {
        this.duration = duration;
        if (!level().isClientSide) {
            getEntityData().set(DATA_DURATION, duration);
        }
    }
    public int getSyncedDuration() {
        return getEntityData().get(DATA_DURATION);
    }

    public void setTornadoOnFire(boolean onFire) {
        if (!level().isClientSide) {
            getEntityData().set(DATA_ON_FIRE, onFire);
        }
    }

    public boolean isTornadoOnFire() {
        return getEntityData().get(DATA_ON_FIRE);
    }

    // Tick

    List<Entity> trackingEntities = new ArrayList<>();

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide) {
            if ((tickCount - 1) % LOOP_SOUND_DURATION == 0 && tickCount < duration - 10) {
                var soundEvent = this.isTornadoOnFire()
                        ? ModSoundRegistry.TORNADO_FIRE_LOOP.get()
                        : ModSoundRegistry.TORNADO_LOOP.get();

                this.playSound(
                        soundEvent,
                        getRadius() / 2f,
                        0.9f + random.nextFloat() * 0.2f
                );
            }
        }

        if (level().isClientSide) return;

        if (tickCount > duration) {
            discard();
            return;
        }

        BlockPos below = BlockPos.containing(getX(), getY() - 0.1, getZ());
        for (int i = 0; i < 3; i++) {
            if (level().getBlockState(below).isSolid()) {
                double targetY = below.getY() + 1.0;
                if (Math.abs(getY() - targetY) > 0.05) {
                    setPos(getX(), targetY, getZ());
                }
                break;
            }
            below = below.below();
        }

        int update = Math.max((int)(getRadius() / 2.0F), 2);
        if (tickCount % update == 0) {
            updateTrackingEntities();
        }

        if (!isTornadoOnFire() && tickCount % 5 == 0) {
            List<Entity> nearby = level().getEntities(this, getBoundingBox().inflate(0.5));
            for (Entity e : nearby) {
                if (ElementCheck.isFireEntity(e)) {
                    setTornadoOnFire(true);
                    break;
                }
            }
        }

        boolean fireDamageTick = isTornadoOnFire() && tickCount % 20 == 0;

        AABB bb = this.getBoundingBox();
        float radius = (float) bb.getXsize();
        boolean isAttractionPhase = tickCount < attractionPhase;

        for (Entity entity : trackingEntities) {
            if (entity == getOwner()) continue;
            if (DamageSources.isFriendlyFireBetween(getOwner(), entity)) continue;
            if (entity.isSpectator()) continue;

            Vec3 entityPos = entity.position();

            double horizontalDist = Math.sqrt(
                    Math.pow(entityPos.x - getX(), 2) +
                            Math.pow(entityPos.z - getZ(), 2)
            );

            if (horizontalDist > radius) continue;

            float f = 1.0F - (float)(horizontalDist / radius);
            float scale = f * f * f * f * 0.25F;
            float resistance = entity instanceof LivingEntity living2
                    ? Mth.clamp(1 - (float) living2.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE), 0.3f, 1f)
                    : 1f;
            float bossResist = entity.getType().is(Tags.EntityTypes.BOSSES) ? 0.5f : 1f;

            if (isAttractionPhase) {
                applyAttraction(entity, scale, resistance, bossResist);
            } else {
                applyVortex(entity, scale, resistance, bossResist);
            }

            if (fireDamageTick && entity instanceof LivingEntity living) {
                var damageSource = ModSpellRegistry.TORNADO_SPELL.get()
                        .getDamageSource(this, getOwner());
                float fireDamage = 2.0f + (damage > 0 ? damage * 0.5f : 0f);
                if (DamageSources.applyDamage(living, fireDamage, damageSource)) {
                    living.setRemainingFireTicks(60);
                }
            }

            entity.fallDistance = 0;
        }
        spawnSpiralParticles();
    }

    private void updateTrackingEntities() {
        trackingEntities = level().getEntities(this, getBoundingBox().inflate(1));
    }

    private void applyAttraction(Entity entity, float scale, float resistance, float bossResist) {
        Vec3 entityPos = entity.position();
        float str = scale * resistance * bossResist;

        Vec3 toAxis = new Vec3(
                getX() - entityPos.x,
                0,
                getZ() - entityPos.z
        ).normalize().scale(str * 6.0f);

        float tornadoHeight = getRadius() * 2f;
        double tornadoBottom = position().y;
        double heightRatio = (entityPos.y - tornadoBottom) / tornadoHeight;
        heightRatio = Mth.clamp(heightRatio, 0.0, 1.0);
        float liftForce = Math.min(str * (float)(1.0 - heightRatio) * 1.5f, 0.15f);

        entity.push(toAxis.x, liftForce, toAxis.z);
        entity.hurtMarked = true;
    }

    private void applyVortex(Entity entity, float scale,
                             float resistance, float bossResist) {

        Vec3 entityPos = entity.position();
        float tornadoHeight = getRadius() * 2f;

        Vec3 toAxis = new Vec3(getX() - entityPos.x, 0, getZ() - entityPos.z);
        double horizontalDist = toAxis.length();

        if (horizontalDist < 0.01) return;

        Vec3 inward = toAxis.normalize();
        Vec3 tangent = new Vec3(-inward.z, 0, inward.x);

        float str = scale * resistance * bossResist;

        Vec3 rotationForce = tangent.scale(str * 2f);

        float targetRadius = getRadius() * 0.3f;
        float radialCorrection = (float)(horizontalDist - targetRadius);
        Vec3 radialForce = inward.scale(str * radialCorrection * 2f);

        double tornadoBottom = position().y;
        double heightRatio = (entityPos.y - tornadoBottom) / tornadoHeight;
        heightRatio = Mth.clamp(heightRatio, 0.0, 1.0);
        float liftForce = Math.min(str * (float)(1.0 - heightRatio) * 1.5f, 0.15f);

        Vec3 motion = rotationForce
                .add(radialForce)
                .add(0, liftForce, 0);

        entity.push(motion.x, motion.y, motion.z);
        entity.hurtMarked = true;
    }

    private void spawnSpiralParticles() {
        if (!(level() instanceof ServerLevel serverLevel)) return;
        if (tickCount % 3 != 0) return;

        float radius = getRadius();
        double cx = getX();
        double cy = getY();
        double cz = getZ();

        int count = 3;
        for (int i = 0; i < count; i++) {
            double angle = random.nextDouble() * Math.PI * 2.0;
            double r = radius * 0.1 + random.nextDouble() * radius * 0.2;

            double px = cx + Math.cos(angle) * r;
            double pz = cz + Math.sin(angle) * r;
            double py = cy + 0.4 + random.nextDouble() * 0.3;

            if (!isTornadoOnFire()) {
                serverLevel.sendParticles(
                        ModParticleRegistry.TORNADO_GROUND_SMOKE.get(),
                        px, py, pz,
                        0, 0, 0, 0, 1.0
                );
            } else {
                serverLevel.sendParticles(
                        ModParticleRegistry.TORNADO_GROUND_FIRE_SMOKE.get(),
                        px, py, pz,
                        0, 0, 0, 0, 1.0
                );

                double heightProgress = random.nextDouble();
                double distanceOffset = (random.nextDouble() - 0.3) * radius * 0.4;
                double rBody = radius * 0.2 + radius * 0.35 * heightProgress
                        + distanceOffset;
                rBody = Mth.clamp(rBody, radius * 0.08, radius * 0.7);

                double bodyAngleOffset = (random.nextDouble() - 0.5) * 0.4;
                double bodyAngle = angle + bodyAngleOffset;

                double tangentSpeed = 0.15 + random.nextDouble() * 0.1;
                double vx = -Math.sin(bodyAngle) * tangentSpeed;
                double vz =  Math.cos(bodyAngle) * tangentSpeed;

                double px2 = cx + Math.cos(bodyAngle) * rBody;
                double py2 = cy + 0.4 + heightProgress * radius * 1.5;
                double pz2 = cz + Math.sin(bodyAngle) * rBody;
                double vyBody = 0.008 + random.nextDouble() * 0.012;

                serverLevel.sendParticles(
                        ModParticleRegistry.TORNADO_FIRE_SMOKE.get(),
                        px2, py2, pz2,
                        0, vx, vyBody, vz,
                        1.0
                );
            }
        }
    }

    // NBT

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("Radius", getRadius());
        tag.putFloat("Damage", damage);
        tag.putInt("Duration", duration);
        tag.putInt("Age", tickCount);
        tag.putBoolean("OnFire", isTornadoOnFire());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Radius")) setRadius(tag.getFloat("Radius"));
        damage = tag.getFloat("Damage");
        duration = tag.getInt("Duration");
        tickCount = tag.getInt("Age");
        if (tag.contains("OnFire")) setTornadoOnFire(tag.getBoolean("OnFire"));
    }

    @Override
    public boolean displayFireAnimation() { return false; }

    @Override
    public boolean isNoGravity() { return true; }
}