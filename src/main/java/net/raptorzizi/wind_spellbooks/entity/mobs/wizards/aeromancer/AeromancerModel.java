package net.raptorzizi.wind_spellbooks.entity.mobs.wizards.aeromancer;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import net.minecraft.resources.ResourceLocation;
import net.raptorzizi.wind_spellbooks.WindSpellbooksMod;

public class AeromancerModel extends AbstractSpellCastingMobModel {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(WindSpellbooksMod.MOD_ID, "textures/entity/aeromancer.png");

    @Override
    public ResourceLocation getAnimationResource(AbstractSpellCastingMob animatable) {
        if (animatable instanceof AeromancerEntity aeromancer) {
            return aeromancer.currentAnimFile;
        }
        return AbstractSpellCastingMob.animationInstantCast;
    }

    @Override
    public ResourceLocation getTextureResource(AbstractSpellCastingMob object) {
        return TEXTURE;
    }

}