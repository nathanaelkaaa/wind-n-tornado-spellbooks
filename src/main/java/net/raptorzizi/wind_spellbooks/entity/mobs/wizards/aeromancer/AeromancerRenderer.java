package net.raptorzizi.wind_spellbooks.entity.mobs.wizards.aeromancer;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class AeromancerRenderer extends AbstractSpellCastingMobRenderer {

    public AeromancerRenderer(EntityRendererProvider.Context context) {
        super(context, new AeromancerModel());
    }
}
