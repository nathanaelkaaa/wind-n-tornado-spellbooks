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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.raptorzizi.frierens_spellbooks.FrierensSpellbooksMod;
import net.raptorzizi.frierens_spellbooks.effect.TailwindEffect;
import net.raptorzizi.frierens_spellbooks.registries.ModMobEffectRegistry;
import net.raptorzizi.frierens_spellbooks.registries.ModSchoolRegistry;

import java.util.List;
import java.util.Optional;

//CHEKK

public class TailwindSpell extends AbstractSpell {

    private final ResourceLocation spellId =
            ResourceLocation.fromNamespaceAndPath(FrierensSpellbooksMod.MOD_ID, "tailwind");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.RARE)
            .setSchoolResource(ModSchoolRegistry.WIND_RESOURCE)
            .setMaxLevel(3)
            .setCooldownSeconds(40.0)
            .build();

    public TailwindSpell() {
        this.manaCostPerLevel   = 25;
        this.baseSpellPower     = 45;
        this.spellPowerPerLevel = 15;
        this.castTime           = 0;
        this.baseManaCost       = 50;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.effect_length",
                        Utils.timeFromTicks(getSpellPower(spellLevel, caster) * 20, 1)),
                Component.translatable("attribute.modifier.plus.1",
                        Utils.stringTruncation(getPercentAirSpeed(spellLevel), 0),
                        Component.literal("Air Speed")),
                Component.translatable("attribute.modifier.plus.1",
                        Utils.stringTruncation(getPercentJump(spellLevel), 0),
                        Component.translatable("attribute.name.generic.jump_strength"))
        );
    }

    @Override public CastType getCastType()            { return CastType.INSTANT; }
    @Override public DefaultConfig getDefaultConfig()  { return defaultConfig; }
    @Override public ResourceLocation getSpellResource() { return spellId; }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundEvents.BREEZE_WIND_CHARGE_BURST.value());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.empty();
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity,
                       CastSource castSource, MagicData playerMagicData) {
        entity.addEffect(new MobEffectInstance(
                ModMobEffectRegistry.TAILWIND,
                (int)(getSpellPower(spellLevel, entity) * 20),
                spellLevel - 1,
                false, false, true
        ));
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getPercentAirSpeed(int spellLevel) {
        return spellLevel * TailwindEffect.MAX_AIR_SPEED_PER_LEVEL * 100f;
    }

    private float getPercentJump(int spellLevel) {
        return (TailwindEffect.JUMP_BASE + TailwindEffect.JUMP_PER_LEVEL * (spellLevel - 1)) * 100f;
    }

    @Override public AnimationHolder getCastStartAnimation() { return SpellAnimations.SELF_CAST_ANIMATION; }
    @Override public AnimationHolder getCastFinishAnimation() { return AnimationHolder.pass(); }
}