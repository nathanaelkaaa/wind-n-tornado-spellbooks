package net.raptorzizi.frierens_spellbooks.entity.spells.tornado;

import net.minecraft.resources.ResourceLocation;
import net.raptorzizi.frierens_spellbooks.FrierensSpellbooksMod;
import software.bernie.geckolib.model.GeoModel;

public class TornadoModel extends GeoModel<TornadoEntity> {

    @Override
    public ResourceLocation getModelResource(TornadoEntity entity) {
        return FrierensSpellbooksMod.id("geo/tornado.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TornadoEntity entity) {
        if (entity.isTornadoOnFire()) {
            return FrierensSpellbooksMod.id("textures/entity/tornado/tornado_fire.png");
        }
        return FrierensSpellbooksMod.id("textures/entity/tornado/tornado.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TornadoEntity entity) {
        return null;
    }
}