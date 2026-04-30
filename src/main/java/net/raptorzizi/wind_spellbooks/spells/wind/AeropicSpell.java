package net.raptorzizi.wind_spellbooks.spells.wind;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.ImpulseCastData;
import io.redspace.ironsspellbooks.player.SpinAttackType;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.spells.fire.BurningDashSpell;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.raptorzizi.wind_spellbooks.WindSpellbooksMod;
import net.raptorzizi.wind_spellbooks.registries.ModMobEffectRegistry;
import net.raptorzizi.wind_spellbooks.registries.ModSchoolRegistry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AeropicSpell extends AbstractSpell {

    private final ResourceLocation spellId =
            new ResourceLocation(WindSpellbooksMod.MOD_ID, "aeropic");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.UNCOMMON)
            .setSchoolResource(ModSchoolRegistry.WIND_RESOURCE)
            .setMaxLevel(4)
            .setCooldownSeconds(12)
            .build();

    public AeropicSpell() {
        this.manaCostPerLevel   = 5;
        this.baseSpellPower     = 5;
        this.spellPowerPerLevel = 0;
        this.castTime           = 0;
        this.baseManaCost       = 30;
    }

    @Override
    public CastType getCastType() { return CastType.INSTANT; }

    @Override
    public DefaultConfig getDefaultConfig() { return defaultConfig; }

    @Override
    public ResourceLocation getSpellResource() { return spellId; }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundRegistry.GUST_CAST.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.empty();
    }

    @Override
    public int getRecastCount(int spellLevel, @Nullable LivingEntity entity) {
        return Math.max(0, spellLevel - 1);
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        List<MutableComponent> info = new ArrayList<>();
        info.add(Component.translatable("ui.irons_spellbooks.damage",
                Utils.stringTruncation(getDamage(spellLevel, caster), 1)));
        if (spellLevel > 1) {
            info.add(Component.translatable("ui.irons_spellbooks.blast_count",
                    getRecastCount(spellLevel, caster)));
        }
        return info;
    }

    @Override
    public ICastDataSerializable getEmptyCastData() {
        return new ImpulseCastData();
    }

    @Override
    public void onClientCast(Level level, int spellLevel, LivingEntity entity, ICastData castData) {
        if (castData instanceof ImpulseCastData bdcd) {
            entity.hasImpulse = bdcd.hasImpulse;
            entity.setDeltaMovement(entity.getDeltaMovement().add(bdcd.x, bdcd.y, bdcd.z));
        }
        super.onClientCast(level, spellLevel, entity, castData);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity,
                       CastSource castSource, MagicData playerMagicData) {

        entity.hasImpulse = true;
        float multiplier = (15 + getSpellPower(spellLevel, entity)) / 20f;

        Vec3 forward = entity.getLookAngle();
        Vec3 impulse = forward.scale(3 * multiplier).multiply(1, 0.8, 1); // Simplifié pour l'exemple

        if (entity.onGround()) {
            impulse = impulse.add(0, 0.5, 0);
        }

        playerMagicData.setAdditionalCastData(new ImpulseCastData((float)impulse.x, (float)impulse.y, (float)impulse.z, true));

        entity.setDeltaMovement(new Vec3(
                Mth.lerp(.75f, entity.getDeltaMovement().x, impulse.x),
                Mth.lerp(.75f, entity.getDeltaMovement().y, impulse.y),
                Mth.lerp(.75f, entity.getDeltaMovement().z, impulse.z)
        ));

        entity.hurtMarked = true;
        entity.addEffect(new MobEffectInstance(ModMobEffectRegistry.ACROBATICS.get(), 10, getDamageInt(spellLevel, entity), false, false, false));
        entity.invulnerableTime = 20;

        playerMagicData.getSyncedData().setSpinAttackType(SpinAttackType.RIPTIDE);

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.SELF_CAST_ANIMATION;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return AnimationHolder.pass();
    }

    private float getDamage(int spellLevel, LivingEntity caster) {
        return getSpellPower(spellLevel, caster);
    }

    private int getDamageInt(int spellLevel, LivingEntity caster) {
        return (int) getDamage(spellLevel, caster);
    }
}