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
import net.raptorzizi.frierens_spellbooks.FrierensSpellbooksMod;
import net.raptorzizi.frierens_spellbooks.entity.spells.wind_blade.WindBladeProjectile;
import net.raptorzizi.frierens_spellbooks.registries.ModSchoolRegistry;

import java.util.List;
import java.util.Optional;

//CHEKK

public class WindBladeSpell extends AbstractSpell {

    private final ResourceLocation spellId =
            ResourceLocation.fromNamespaceAndPath(FrierensSpellbooksMod.MOD_ID, "wind_blade");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setSchoolResource(ModSchoolRegistry.WIND_RESOURCE)
            .setMaxLevel(10)
            .setCooldownSeconds((double)1.0F)
            .build();

    public WindBladeSpell() {
        this.manaCostPerLevel   = 2;
        this.baseSpellPower     = 10;
        this.spellPowerPerLevel = 1;
        this.castTime           = 0;
        this.baseManaCost       = 15;
    }

    @Override
    public CastType getCastType() { return CastType.INSTANT; }

    @Override
    public DefaultConfig getDefaultConfig() { return defaultConfig; }

    @Override
    public ResourceLocation getSpellResource() { return spellId; }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundEvents.BREEZE_WIND_CHARGE_BURST.value());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.empty();
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.damage",
                Utils.stringTruncation(getDamage(spellLevel, caster), 2)));
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity,
                       CastSource castSource, MagicData playerMagicData) {
        WindBladeProjectile blade = new WindBladeProjectile(level, entity);
        blade.setPos(entity.getEyePosition());
        blade.shoot(entity.getLookAngle());
        blade.setDamage(getDamage(spellLevel, entity));
        level.addFreshEntity(blade);

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.SLASH_ANIMATION;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return AnimationHolder.pass();
    }

    private float getDamage(int spellLevel, LivingEntity entity) {
        return getSpellPower(spellLevel, entity) * 0.5f;
    }
}