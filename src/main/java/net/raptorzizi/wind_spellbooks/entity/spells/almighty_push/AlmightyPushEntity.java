package net.raptorzizi.wind_spellbooks.entity.spells.almighty_push;

import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractShieldEntity;
import io.redspace.ironsspellbooks.entity.spells.ShieldPart;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;
import net.raptorzizi.wind_spellbooks.registries.ModEntityRegistry;
import net.raptorzizi.wind_spellbooks.registries.ModSpellRegistry;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class AlmightyPushEntity extends AbstractShieldEntity implements GeoEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final EntityDataAccessor<Float> DATA_RADIUS =
            SynchedEntityData.defineId(AlmightyPushEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_LIFETIME =
            SynchedEntityData.defineId(AlmightyPushEntity.class, EntityDataSerializers.INT);
    public float strength  = 1f;
    public float damage    = 0f;
    public int   amplifier = 0;

    private UUID ownerUUID = null;
    private double ownerCenterY = 0;
    private int age = 0;
    private final Set<UUID> pushedThisTick = new HashSet<>();

    protected AlmightyPushPart[] subEntities;
    protected final Vec3[] subPositions;

    public AlmightyPushEntity(EntityType<? extends AlmightyPushEntity> type, Level level) {
        super(type, level);
        this.noPhysics = true;
        this.subEntities = new AlmightyPushPart[1];
        this.subPositions = new Vec3[1];
        this.createShield();
    }

    public AlmightyPushEntity(Level level, LivingEntity owner) {
        this(ModEntityRegistry.ALMIGHTY_PUSH_ENTITY.get(), level);
        this.ownerUUID = owner.getUUID();
        this.ownerCenterY = owner.getY() + owner.getBbHeight() / 2.0;
        this.setPos(owner.getX(), this.ownerCenterY - getRadius(), owner.getZ());
    }

    public void setOwner(LivingEntity owner) {
        this.ownerUUID = owner.getUUID();
    }

    public Entity getOwner() {
        if (ownerUUID == null || this.level().isClientSide) return null;
        return ((ServerLevel) this.level()).getEntity(ownerUUID);
    }

    @Override
    protected void createShield() {
        this.subEntities[0] = new AlmightyPushPart(this, "almighty_push", 1f, 1f, true);
        this.subPositions[0] = Vec3.ZERO;
    }

    @Override
    public PartEntity<?>[] getParts() {
        return this.subEntities;
    }

    @Override
    public void takeDamage(DamageSource source, float amount, @Nullable Vec3 location) {
        Entity direct = source.getDirectEntity();
        if (!(direct instanceof Projectile incoming)) return;

        Entity attacker = source.getEntity();
        if (attacker != null && attacker == this.getOwner()) return;

        incoming.discard();

        if (!this.level().isClientSide) {
            Vec3 hit = location != null ? location : incoming.position();
            MagicManager.spawnParticles(this.level(), ParticleTypes.ELECTRIC_SPARK,
                    hit.x, hit.y, hit.z, 15, 0.1, 0.1, 0.1, 0.5, false);
            this.level().playSound((Player) null, hit.x, hit.y, hit.z,
                    SoundEvents.SHIELD_BLOCK, SoundSource.NEUTRAL, 0.8f, 1.0f);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_RADIUS, 4f);
        this.entityData.define(DATA_LIFETIME, 50);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_RADIUS.equals(pKey)) {
            double x = this.getX(), y = this.getY(), z = this.getZ();
            super.refreshDimensions();
            this.setPos(x, y, z);
        }
        super.onSyncedDataUpdated(pKey);
    }

    public void setRadius(float radius) {
        this.getEntityData().set(DATA_RADIUS, radius);
        if (!this.level().isClientSide) {
            double x = this.getX(), z = this.getZ();
            double y = ownerCenterY - radius;
            this.refreshDimensions();
            this.setPos(x, y, z);
        }
    }

    public float getRadius() {
        return this.getEntityData().get(DATA_RADIUS);
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        float r = getRadius() * 2f;
        return EntityDimensions.scalable(r, r);
    }

    public int getLifetime() {
        return this.getEntityData().get(DATA_LIFETIME);
    }

    public void setLifetime(int lifetime) {
        this.getEntityData().set(DATA_LIFETIME, lifetime);
    }

    @Override
    public void tick() {
        this.hurtThisTick = false;

        if (++age >= getLifetime()) {
            this.discard();
            return;
        }

        float r = getRadius();
        Vec3 pos = this.position();

        AABB zone = new AABB(
                pos.x - r, pos.y, pos.z - r,
                pos.x + r, pos.y + r * 2, pos.z + r
        );

        Vec3 partPos = pos.add(0, r, 0);
        subEntities[0].setPos(partPos);
        subEntities[0].xo   = partPos.x;
        subEntities[0].yo   = partPos.y;
        subEntities[0].zo   = partPos.z;
        subEntities[0].xOld = partPos.x;
        subEntities[0].yOld = partPos.y;
        subEntities[0].zOld = partPos.z;
        subEntities[0].setBoundingBox(zone);

        if (this.level().isClientSide) return;

        Vec3 center = pos.add(0, r * 0.9, 0);
        Entity owner = this.getOwner();

        List<Entity> targets = this.level().getEntities((Entity) null, zone, e ->
                e != this
                        && !(e instanceof Projectile)
                        && !(e instanceof Player)
                        && (owner == null || !(e instanceof LivingEntity living
                        && DamageSources.isFriendlyFireBetween((LivingEntity) owner, living))));

        pushedThisTick.clear();

        for (Entity target : targets) {
            if (pushedThisTick.contains(target.getUUID())) continue;
            pushedThisTick.add(target.getUUID());

            Vec3 targetCenter = target.getBoundingBox().getCenter();
            Vec3 direction = targetCenter.subtract(center);
            double distance = direction.length();
            if (distance < 0.01) continue;

            double forceFactor = Math.max(0.2, 1.0 - (distance / r));

            Vec3 knockback = direction.normalize()
                    .scale(strength * forceFactor)
                    .add(0, strength * 0.15 * forceFactor, 0);

            target.setDeltaMovement(target.getDeltaMovement().add(knockback));
            target.hasImpulse = true;
            target.hurtMarked = true;

            if (target instanceof LivingEntity living) {
                living.addEffect(new MobEffectInstance(MobEffectRegistry.AIRBORNE.get(), 20, amplifier));
                if (damage > 0 && owner instanceof LivingEntity livingOwner) {
                    DamageSources.applyDamage(living, damage * (float) forceFactor,
                            ModSpellRegistry.ALMIGHTY_PUSH_SPELL.get().getDamageSource(livingOwner));
                }
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (ownerUUID != null) tag.putUUID("Owner", ownerUUID);
        tag.putInt("Lifetime", getLifetime());
        tag.putFloat("Radius",   getRadius());
        tag.putFloat("Strength", strength);
        tag.putFloat("Damage",   damage);
        tag.putInt("Amplifier",  amplifier);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID("Owner")) ownerUUID = tag.getUUID("Owner");
        if (tag.contains("Lifetime")) setLifetime(tag.getInt("Lifetime"));
        if (tag.contains("Radius"))    setRadius(tag.getFloat("Radius"));
        if (tag.contains("Strength"))  strength  = tag.getFloat("Strength");
        if (tag.contains("Damage"))    damage    = tag.getFloat("Damage");
        if (tag.contains("Amplifier")) amplifier = tag.getInt("Amplifier");
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() { return cache; }

    @Override public boolean isNoGravity() { return true;  }
    @Override public boolean isPickable()  { return false; }

    public static class AlmightyPushPart extends ShieldPart {

        public AlmightyPushPart(AbstractShieldEntity parent, String name,
                                float width, float height, boolean fixed) {
            super(parent, name, width, height, fixed);
        }

        @Override public boolean isPickable()        { return true;  }
        @Override public boolean isPushable()        { return false; }
        @Override public boolean canBeCollidedWith() { return true;  }
    }
}
