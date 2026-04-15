package net.raptorzizi.wind_spellbooks.entity.spells.wind_blade;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.raptorzizi.wind_spellbooks.registries.ModEntityRegistry;
import net.raptorzizi.wind_spellbooks.registries.ModSpellRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//CHEKK
public class WindBladeProjectile extends Projectile implements AntiMagicSusceptible {

    private static final EntityDataAccessor<Float> DATA_RADIUS =
            SynchedEntityData.defineId(WindBladeProjectile.class, EntityDataSerializers.FLOAT);

    private static final double SPEED = 1.2;
    private static final int EXPIRE_TIME = 50;
    private static final float MAX_RADIUS = 1.5f;

    public final int animationSeed;
    public AABB oldBB;
    public int animationTime;

    private int age;
    private float damage;
    private List<Entity> victims;

    public WindBladeProjectile(EntityType<? extends WindBladeProjectile> entityType, Level level) {
        super(entityType, level);
        this.animationSeed = Utils.random.nextInt(9999);
        this.setRadius(0.6F);
        this.oldBB = this.getBoundingBox();
        this.victims = new ArrayList();
        this.setNoGravity(true);
    }

    public WindBladeProjectile(EntityType<? extends WindBladeProjectile> entityType, Level levelIn, LivingEntity shooter) {
        this(entityType, levelIn);
        this.setOwner(shooter);
        this.setYRot(shooter.getYRot());
        this.setXRot(shooter.getXRot());
    }

    public WindBladeProjectile(Level levelIn, LivingEntity shooter) {
        this((EntityType) ModEntityRegistry.WIND_BLADE_PROJECTILE.get(), levelIn, shooter);
    }

    public void shoot(Vec3 direction) {
        setDeltaMovement(direction.scale(SPEED));
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return damage;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_RADIUS, 0.5f);
    }

    public void setRadius(float radius) {
        if (radius <= MAX_RADIUS && !this.level().isClientSide) {
            this.getEntityData().set(DATA_RADIUS, Mth.clamp(radius, 0f, MAX_RADIUS));
        }
    }

    public float getRadius() {
        return this.getEntityData().get(DATA_RADIUS);
    }

    @Override
    public void refreshDimensions() {
        double x = getX(), y = getY(), z = getZ();
        super.refreshDimensions();
        setPos(x, y, z);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (DATA_RADIUS.equals(key)) refreshDimensions();
        super.onSyncedDataUpdated(key);
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.scalable(getRadius() * 1.4f, 0.25f);
    }

    @Override
    public void tick() {
        super.tick();

        if (++age > EXPIRE_TIME) {
            discard();
            return;
        }

        oldBB = getBoundingBox();
        setRadius(getRadius() + 0.06f);

        if (!level().isClientSide) {
            HitResult hit = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hit.getType() == HitResult.Type.BLOCK) {
                onHitBlock((BlockHitResult) hit);
            }

            for (Entity entity : level().getEntities(this, getBoundingBox())
                    .stream()
                    .filter(e -> canHitEntity(e) && !victims.contains(e))
                    .collect(Collectors.toSet())) {
                damageEntity(entity);
                MagicManager.spawnParticles(level(), ParticleTypes.GUST, entity.getX(), entity.getY(), entity.getZ(), 50, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.5F, true);
            }
        }

        setPos(position().add(getDeltaMovement()));
        this.spawnParticles();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        discard();
    }

    private void damageEntity(Entity entity) {
        if (!victims.contains(entity)) {
            DamageSources.applyDamage(entity, damage,
                    ModSpellRegistry.WIND_BLADE_SPELL.get().getDamageSource(this, getOwner()));
            victims.add(entity);
        }
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return entity != getOwner() && super.canHitEntity(entity);
    }

    public void spawnParticles() {
        if (level().isClientSide) {
            float width = (float) this.getBoundingBox().getXsize();
            float radians = ((float) Math.PI / 180F) * this.getYRot();
            float speed = 0.1F;
            float cursor = 0f;

            while (cursor < width) {
                float step = 0.6f + (float) Math.random() * 0.8f;
                cursor += step;

                double offset = cursor - width / 2.0f;
                double x = this.getX() + offset * Math.cos(radians);
                double y = this.getY();
                double z = this.getZ() - offset * Math.sin(radians);
                double dx = Math.random() * speed * 2f - speed;
                double dy = Math.random() * speed * 2f - speed;
                double dz = Math.random() * speed * 2f - speed;

                level().addParticle(ParticleTypes.SMALL_GUST, false, x + dx, y + dy, z + dz, dx, dy, dz);
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("Damage", damage);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        damage = tag.getFloat("Damage");
    }

    public void onAntiMagic(MagicData playerMagicData) {
        this.discard();
    }
}