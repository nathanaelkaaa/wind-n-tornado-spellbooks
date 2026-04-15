package net.raptorzizi.frierens_spellbooks.spells.wind;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.raptorzizi.frierens_spellbooks.FrierensSpellbooksMod;
import net.raptorzizi.frierens_spellbooks.entity.spells.tornado.TornadoEntity;
import net.raptorzizi.frierens_spellbooks.registries.ModSchoolRegistry;

import java.util.List;
import java.util.Optional;

public class TornadoSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(FrierensSpellbooksMod.MOD_ID, "tornado");
    private final DefaultConfig defaultConfig;

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.radius", Utils.stringTruncation(getRadius(spellLevel, caster), 1)),
                Component.translatable("ui.irons_spellbooks.duration", Utils.timeFromTicks(getDuration(spellLevel), 1))
        );
    }

    public TornadoSpell() {
        this.defaultConfig = new DefaultConfig()
                .setMinRarity(SpellRarity.EPIC)
                .setSchoolResource(ModSchoolRegistry.WIND_RESOURCE)
                .setMaxLevel(5)
                .setCooldownSeconds(100)
                .build();
        this.manaCostPerLevel = 50;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 0;
        this.castTime = 60;
        this.baseManaCost = 200;
    }

    @Override
    public DefaultConfig getDefaultConfig() { return defaultConfig; }

    @Override
    public ResourceLocation getSpellResource() { return spellId; }

    @Override
    public CastType getCastType() { return CastType.LONG; }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundEvents.BREEZE_IDLE_AIR);
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundEvents.BREEZE_SHOOT);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity,
                       CastSource castSource, MagicData playerMagicData) {

        HitResult raycast = Utils.raycastForEntity(level, entity, 16, true);
        Vec3 spawnPos = raycast.getLocation();

        if (raycast instanceof BlockHitResult blockHit) {
            spawnPos = Vec3.atBottomCenterOf(blockHit.getBlockPos().above());
        }

        TornadoEntity tornado = new TornadoEntity(level, entity);
        tornado.setRadius(getRadius(spellLevel, entity));
        tornado.setDamage(getDamage(spellLevel, entity));
        tornado.setDuration(getDuration(spellLevel));
        tornado.moveTo(spawnPos.x, spawnPos.y, spawnPos.z);
        level.addFreshEntity(tornado);

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.CHARGE_WAVY_ANIMATION;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return SpellAnimations.ANIMATION_LONG_CAST_FINISH;
    }


    private float getDamage(int spellLevel, LivingEntity entity) {
        return this.getSpellPower(spellLevel, entity) * 2.0F;
    }

    public float getRadius(int spellLevel, LivingEntity caster) {
        return 5.0f + spellLevel * 0.8f + 0.125F * this.getSpellPower(spellLevel, caster);
    }

    public int getDuration(int spellLevel) {
        return 15 * (5 + spellLevel * 2);
    }
}