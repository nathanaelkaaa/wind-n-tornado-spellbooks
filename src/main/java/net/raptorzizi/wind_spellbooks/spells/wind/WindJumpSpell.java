package net.raptorzizi.wind_spellbooks.spells.wind;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.ImpulseCastData;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import io.redspace.ironsspellbooks.entity.spells.gust.GustCollider;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.raptorzizi.wind_spellbooks.WindSpellbooksMod;
import net.raptorzizi.wind_spellbooks.registries.ModParticleRegistry;
import net.raptorzizi.wind_spellbooks.registries.ModSchoolRegistry;

import java.util.List;

//CHEKK

public class WindJumpSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(WindSpellbooksMod.MOD_ID, "wind_jump");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.distance",
                        Utils.stringTruncation(getJumpHeight(spellLevel, caster), 1)),
                Component.translatable("ui.irons_spellbooks.duration",
                        Utils.timeFromTicks(getFloatDuration(spellLevel, caster), 1))
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setSchoolResource(ModSchoolRegistry.WIND_RESOURCE)
            .setMaxLevel(3)
            .setCooldownSeconds(5)
            .build();

    public WindJumpSpell() {
        this.manaCostPerLevel = 0;
        this.baseSpellPower = 5;
        this.spellPowerPerLevel = 1;
        this.castTime = 0;
        this.baseManaCost = 30;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public ICastDataSerializable getEmptyCastData() {
        return new ImpulseCastData();
    }

    @Override
    public void onClientCast(Level level, int spellLevel, LivingEntity entity, ICastData castData) {
        if (castData instanceof ImpulseCastData data) {
            entity.hasImpulse = data.hasImpulse;
            double y = Math.max(entity.getDeltaMovement().y, data.y);
            entity.setDeltaMovement(data.x, y, data.z);
        }
        super.onClientCast(level, spellLevel, entity, castData);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {

        entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, getFloatDuration(spellLevel, entity), 9, false, false, true));
        entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, getFloatDuration(spellLevel, entity), 0, false, false, true));

        Vec3 strikePos = entity.position();

        GustCollider gust = new GustCollider(level, entity);
        gust.setPos(strikePos);
        gust.range = 5;
        gust.strength = getSpellPower(spellLevel, entity) * 0.2F;
        gust.amplifier = spellLevel - 1;
        level.addFreshEntity(gust);
        gust.setDealDamageActive();
        gust.tick();

        MagicManager.spawnParticles(level, ModParticleRegistry.FEATHER.get(), entity.getX(),  entity.getY(),  entity.getZ(), 5, .3, .3, .3, 0.1, false);

        Vec3 motion = entity.getLookAngle().multiply(1, 0, 1).normalize().add(0, 5, 0).scale(getJumpStrength(spellLevel, entity));
        playerMagicData.setAdditionalCastData(new ImpulseCastData((float) motion.x, (float) motion.y, (float) motion.z, true));
        entity.setDeltaMovement(entity.getDeltaMovement().add(motion));
        entity.hasImpulse = true;

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    public float getJumpStrength(int spellLevel, LivingEntity caster) {
        return (float) (0.2 + (spellLevel * 0.05));
    }

    public float getJumpHeight(int spellLevel, LivingEntity caster) {
        return getJumpStrength(spellLevel, caster) * 36;
    }

    public int getFloatDuration(int spellLevel, LivingEntity caster) {
        return 100 * spellLevel;
    }
}
