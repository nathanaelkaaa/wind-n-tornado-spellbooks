package net.raptorzizi.frierens_spellbooks.spells;

import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import net.minecraft.resources.ResourceLocation;
import net.raptorzizi.frierens_spellbooks.FrierensSpellbooksMod;

public class ModSpellAnimations {
    public static ResourceLocation ANIMATION_RESOURCE = ResourceLocation.fromNamespaceAndPath(FrierensSpellbooksMod.MOD_ID, "animations");

    public static final AnimationHolder CHARGE_SLASH = new AnimationHolder(
            FrierensSpellbooksMod.id("charge_slash"), true);

    public static final AnimationHolder FINISH_SLASH = new AnimationHolder(
            FrierensSpellbooksMod.id("finish_slash"), true);
}