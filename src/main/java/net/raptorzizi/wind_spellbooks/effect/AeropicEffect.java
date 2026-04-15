package net.raptorzizi.wind_spellbooks.effect;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.effect.ISyncedMobEffect;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.mixin.LivingEntityAccessor;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.raptorzizi.wind_spellbooks.registries.ModParticleRegistry;
import net.raptorzizi.wind_spellbooks.registries.ModSpellRegistry;

import java.util.List;
import java.util.UUID;

public class AeropicEffect extends MagicMobEffect implements ISyncedMobEffect {

    public AeropicEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        var level = livingEntity.level();
        if (level.isClientSide) {
            return true;
        }

        Vec3 movement = livingEntity.getDeltaMovement();
        AABB dashHitbox = livingEntity.getBoundingBox()
                .inflate(0.5, 0.5, 0.5)
                .expandTowards(movement.scale(2.0));
        List<Entity> list = level.getEntities(livingEntity, dashHitbox);

        boolean hitEntity = false;
        UUID ignore = null;

        for (Entity entity : list) {
            if (!(entity instanceof LivingEntity)) continue;
            if (DamageSources.applyDamage(
                    entity, amplifier,
                    ModSpellRegistry.ACROBATICS_SPELL.get().getDamageSource(livingEntity))) {
                entity.invulnerableTime = 20;
                hitEntity = true;
                ignore = entity.getUUID();
                break;
            }
        }

        if (!hitEntity) {
            livingEntity.fallDistance = 0;
            return true;
        }

        livingEntity.setDeltaMovement(0, livingEntity.getDeltaMovement().y, 0);

        float explosionRadius = 4;
        float explosionRadiusSqr = explosionRadius * explosionRadius;
        Vec3 losPoint = Utils.raycastForBlock(
                level,
                livingEntity.position(),
                livingEntity.position().add(0, 1, 0),
                ClipContext.Fluid.NONE
        ).getLocation();

        for (Entity entity : level.getEntities(livingEntity, livingEntity.getBoundingBox().inflate(explosionRadius))) {
            if (ignore != null && entity.getUUID().equals(ignore)) continue;
            double distSqr = entity.distanceToSqr(livingEntity.position());
            if (distSqr < explosionRadiusSqr
                    && entity.canBeHitByProjectile()
                    && Utils.hasLineOfSight(level, losPoint, entity.getBoundingBox().getCenter(), true)) {
                float damage = (float) (amplifier * (1 - distSqr / explosionRadiusSqr) * 0.5);
                DamageSources.applyDamage(entity, damage,
                        ModSpellRegistry.ACROBATICS_SPELL.get().getDamageSource(livingEntity));
            }
        }

        livingEntity.setDeltaMovement(0, 1.2, 0);
        livingEntity.hurtMarked = true;

        if (livingEntity instanceof ServerPlayer serverPlayer) {
            MagicData magicData = MagicData.getPlayerMagicData(serverPlayer);
            var spell = ModSpellRegistry.ACROBATICS_SPELL.get();
            int spellLevel = amplifier;

            int recastsRemaining = serverPlayer.getPersistentData()
                    .getInt("aeropic_recasts_remaining");

            if (spellLevel > 1 && recastsRemaining > 0) {
                serverPlayer.getPersistentData()
                        .putInt("aeropic_recasts_remaining", recastsRemaining - 1);
                serverPlayer.getPersistentData()
                        .putBoolean("aeropic_hit_confirmed", true);

                if (!magicData.getPlayerRecasts().hasRecastForSpell(spell.getSpellId())) {
                    magicData.getPlayerRecasts().addRecast(
                            new RecastInstance(
                                    spell.getSpellId(),
                                    spellLevel,
                                    spell.getRecastCount(spellLevel, serverPlayer),
                                    60,
                                    CastSource.SPELLBOOK,
                                    null
                            ), magicData
                    );
                }
            }
        }

        double x = livingEntity.getX(), y = livingEntity.getY() + 1, z = livingEntity.getZ();
        MagicManager.spawnParticles(level, ModParticleRegistry.FEATHER.get(),         x, y, z, 5, .3, .3, .3, 0.1, false);
        MagicManager.spawnParticles(level, ParticleTypes.GUST,               x, y, z, 1,  .1, .1, .1, 0,   true);
        level.playSound(null, x, y, z,
                SoundEvents.WIND_CHARGE_BURST.value(),
                livingEntity.getSoundSource(), 4, 1.2f);

        return false;
    }


    @Override
    public void clientTick(LivingEntity entity, MobEffectInstance instance) {
        var level = entity.level();
        for (int i = 0; i < 3; i++) {
            Vec3 random = Utils.getRandomVec3(.15);
            level.addParticle(ParticleTypes.SMALL_GUST,
                    entity.getRandomX(0.5), entity.getY() + Utils.getRandomScaled(0.5),
                    entity.getRandomZ(0.5), random.x, random.y, random.z);
        }
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public void onEffectAdded(LivingEntity pLivingEntity, int pAmplifier) {
        super.onEffectAdded(pLivingEntity, pAmplifier);
        ((LivingEntityAccessor) pLivingEntity).setLivingEntityFlagInvoker(4, true);

        if (pLivingEntity instanceof ServerPlayer serverPlayer) {
            serverPlayer.getPersistentData().remove("aeropic_hit_confirmed");

            boolean alreadyExists = serverPlayer.getPersistentData()
                    .contains("aeropic_recasts_remaining");

            if (!alreadyExists) {
                var spell = ModSpellRegistry.ACROBATICS_SPELL.get();
                int maxRecasts = spell.getRecastCount(pAmplifier, serverPlayer) - 1;
                serverPlayer.getPersistentData().putInt("aeropic_recasts_remaining", maxRecasts);
            }
        }
    }

    @Override
    public void onEffectRemoved(LivingEntity pLivingEntity, int pAmplifier) {
        super.onEffectRemoved(pLivingEntity, pAmplifier);
        ((LivingEntityAccessor) pLivingEntity).setLivingEntityFlagInvoker(4, false);

        if (pLivingEntity instanceof ServerPlayer serverPlayer) {
            MagicData magicData = MagicData.getPlayerMagicData(serverPlayer);
            var spell = ModSpellRegistry.ACROBATICS_SPELL.get();

            boolean hitConfirmed = serverPlayer.getPersistentData()
                    .getBoolean("aeropic_hit_confirmed");

            serverPlayer.getPersistentData().remove("aeropic_hit_confirmed");

            if (!hitConfirmed) {
                while (magicData.getPlayerRecasts().hasRecastForSpell(spell.getSpellId())) {
                    magicData.getPlayerRecasts().decrementRecastCount(spell.getSpellId());
                }
                serverPlayer.getPersistentData().remove("aeropic_recasts_remaining");
            }
        }
    }
}