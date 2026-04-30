package net.raptorzizi.wind_spellbooks.spells;

import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import net.minecraft.resources.ResourceLocation;
import net.raptorzizi.wind_spellbooks.WindSpellbooksMod;

public class ModSpellAnimations {
    public static ResourceLocation ANIMATION_RESOURCE = new ResourceLocation(WindSpellbooksMod.MOD_ID, "animations");

    public static final AnimationHolder CHARGE_SLASH = new AnimationHolder(
            WindSpellbooksMod.id("charge_slash"), true);

    public static final AnimationHolder FINISH_SLASH = new AnimationHolder(
            WindSpellbooksMod.id("finish_slash"), true);
}