package net.raptorzizi.wind_spellbooks.spells.wind;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.spells.ender.TeleportSpell;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.raptorzizi.wind_spellbooks.WindSpellbooksMod;
import net.raptorzizi.wind_spellbooks.entity.spells.iron_slash.DashStopEntity;
import net.raptorzizi.wind_spellbooks.entity.spells.iron_slash.IronSlashEntity;
import net.raptorzizi.wind_spellbooks.registries.ModSchoolRegistry;
import net.raptorzizi.wind_spellbooks.spells.ModSpellAnimations;
import net.raptorzizi.wind_spellbooks.util.ElementCheck;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class IronSlashSpell extends AbstractSpell {

    private final ResourceLocation spellId =
            ResourceLocation.fromNamespaceAndPath(WindSpellbooksMod.MOD_ID, "iron_slash");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.UNCOMMON)
            .setSchoolResource(ModSchoolRegistry.WIND_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(8)
            .build();

    public IronSlashSpell() {
        this.baseSpellPower     = 6;
        this.spellPowerPerLevel = 2;
        this.baseManaCost       = 30;
        this.manaCostPerLevel   = 15;
        this.castTime           = 20;
    }

    @Override
    public CastType getCastType() { return CastType.LONG; }

    @Override
    public DefaultConfig getDefaultConfig() { return defaultConfig; }

    @Override
    public ResourceLocation getSpellResource() { return spellId; }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundRegistry.GUST_CHARGE.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundRegistry.BLOOD_STEP.get());
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.distance",
                        Utils.stringTruncation(getDistance(spellLevel, caster), 1)),
                Component.translatable("ui.irons_spellbooks.damage",
                        getDamageText(spellLevel, caster))
        );
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity caster,
                       CastSource castSource, MagicData playerMagicData) {

        if (level.isClientSide) {
            super.onCast(level, spellLevel, caster, castSource, playerMagicData);
            return;
        }

        boolean iceMode = ElementCheck.hasIceConditions(caster);

        Vec3 eyeStart = caster.getEyePosition();
        Vec3 lookDir  = caster.getLookAngle();
        float distance = getDistance(spellLevel, caster);

        Vec3 eyeEnd = eyeStart.add(lookDir.scale(distance));
        BlockHitResult blockHit = level.clip(new ClipContext(
                eyeStart, eyeEnd,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                caster
        ));

        Vec3 hitPoint = blockHit.getType() != HitResult.Type.MISS
                ? blockHit.getLocation().subtract(lookDir.scale(1.2))
                : eyeEnd;

        Vec3 dest = TeleportSpell.findTeleportLocation(level, caster, (float) hitPoint.distanceTo(eyeStart));

        IronSlashEntity slash = new IronSlashEntity(
                level, caster, eyeStart, hitPoint,
                getDamage(spellLevel, caster), iceMode);
        level.addFreshEntity(slash);

        if (caster.isPassenger()) caster.stopRiding();
        performDash(level, caster, dest);

        level.playSound(null, dest.x, dest.y, dest.z,
                getCastFinishSound().get(), SoundSource.PLAYERS, 1f, 1f);

        super.onCast(level, spellLevel, caster, castSource, playerMagicData);
    }

    @Override
    public void onServerCastTick(Level level, int spellLevel, LivingEntity caster, @Nullable MagicData playerMagicData) {
        super.onServerCastTick(level, spellLevel, caster, playerMagicData);

        if (!ElementCheck.hasIceConditions(caster)) return;

        if (caster.tickCount % 3 == 0) {
            MagicManager.spawnParticles(level,
                    ParticleHelper.ICY_FOG,
                    caster.getX(), caster.getY(), caster.getZ(),
                    3, 0.4, 0, 0.4, 0.02, false);
        }

        if (caster.tickCount % 2 == 0) {
            MagicManager.spawnParticles(level,
                    ParticleHelper.SNOWFLAKE,
                    caster.getX(),
                    caster.getY() + caster.getBbHeight() * 0.5f,
                    caster.getZ(),
                    5,
                    caster.getBbWidth() * 0.8f,
                    caster.getBbHeight() * 0.5f,
                    caster.getBbWidth() * 0.8f,
                    0.05, false);
        }
    }

    private void performDash(Level level, LivingEntity caster, Vec3 dest) {
        Vec3 dashDir = dest.subtract(caster.position());
        double dashDistance = dashDir.length();

        if (dashDistance > 0.1) {
            int dashTicks = 4;
            Vec3 velocity = dashDir.scale(1.0 / dashTicks);

            double maxSpeed = 5.0;
            if (velocity.length() > maxSpeed) {
                velocity = velocity.normalize().scale(maxSpeed);
            }

            caster.setDeltaMovement(velocity);
            caster.hurtMarked = true;
            caster.resetFallDistance();

            DashStopEntity stopEntity = new DashStopEntity(level, caster, dest, dashTicks + 2);
            level.addFreshEntity(stopEntity);
        } else {
            Utils.handleSpellTeleport(this, caster, dest);
        }
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return ModSpellAnimations.CHARGE_SLASH;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return ModSpellAnimations.FINISH_SLASH;
    }

    private float getDistance(int spellLevel, LivingEntity caster) {
        return 10 + spellLevel * 2f;
    }

    private float getDamage(int spellLevel, LivingEntity caster) {return getSpellPower(spellLevel, caster) + Utils.getWeaponDamage(caster);}

    private String getDamageText(int spellLevel, LivingEntity caster) {
        if (caster != null) {
            float weaponDamage = Utils.getWeaponDamage(caster);
            String plus = weaponDamage > 0
                    ? String.format(" (+%s)", Utils.stringTruncation(weaponDamage, 1))
                    : "";
            return Utils.stringTruncation(getDamage(spellLevel, caster), 1) + plus;
        }
        return "" + getSpellPower(spellLevel, caster);
    }
}