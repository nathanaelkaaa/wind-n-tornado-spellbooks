package net.raptorzizi.wind_spellbooks.spells.wind;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.raptorzizi.wind_spellbooks.WindSpellbooksMod;
import net.raptorzizi.wind_spellbooks.entity.spells.almighty_push.AlmightyPushEntity;
import net.raptorzizi.wind_spellbooks.registries.ModSchoolRegistry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlmightyPushSpell extends AbstractSpell {

    private final ResourceLocation spellId =
            new ResourceLocation(WindSpellbooksMod.MOD_ID, "almighty_push");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.UNCOMMON)
            .setSchoolResource(ModSchoolRegistry.WIND_RESOURCE)
            .setMaxLevel(10)
            .setCooldownSeconds(15)
            .build();

    public AlmightyPushSpell() {
        this.manaCostPerLevel   = 5;
        this.baseSpellPower     = 10;
        this.spellPowerPerLevel = 1;
        this.castTime           = 0;
        this.baseManaCost       = 40;
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
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        List<MutableComponent> info = new ArrayList<>();
        info.add(Component.translatable("ui.irons_spellbooks.radius",
                Utils.stringTruncation(getRange(spellLevel, caster), 1)));
        info.add(Component.translatable("ui.irons_spellbooks.strength",
                String.format("%s%%", (int) (getStrength(spellLevel, caster) * 100
                        / getStrength(1, null)))));
        info.add(Component.translatable("ui.irons_spellbooks.duration", Utils.timeFromTicks(getDuration(spellLevel), 1)));
        return info;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity,
                       CastSource castSource, MagicData playerMagicData) {

        AlmightyPushEntity push = new AlmightyPushEntity(level, entity);
        push.setRadius(getRange(spellLevel, entity));
        push.strength  = getStrength(spellLevel, entity);
        push.amplifier = spellLevel - 1;
        push.setLifetime(getDuration(spellLevel));
        level.addFreshEntity(push);

        level.playSound(null,
                entity.getX(), entity.getY(), entity.getZ(),
                SoundEvents.EVOKER_CAST_SPELL,
                entity.getSoundSource(), 3.0f, 0.7f);

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    public float getRange(int spellLevel, @Nullable LivingEntity caster) {
        return 3f + spellLevel * 0.5f;
    }

    public float getStrength(int spellLevel, @Nullable LivingEntity caster) {
        return getSpellPower(spellLevel, caster) * 0.2f;
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.SELF_CAST_ANIMATION;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return AnimationHolder.pass();
    }

    public int getDuration(int spellLevel) {
        return 25 + (spellLevel * 3);
    }
}