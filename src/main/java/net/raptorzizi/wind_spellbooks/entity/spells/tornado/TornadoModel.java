package net.raptorzizi.wind_spellbooks.entity.spells.tornado;

import net.minecraft.resources.ResourceLocation;
import net.raptorzizi.wind_spellbooks.WindSpellbooksMod;
import software.bernie.geckolib.model.GeoModel;

public class TornadoModel extends GeoModel<TornadoEntity> {

    @Override
    public ResourceLocation getModelResource(TornadoEntity entity) {
        return WindSpellbooksMod.id("geo/tornado.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TornadoEntity entity) {
        if (entity.isTornadoOnFire()) {
            return WindSpellbooksMod.id("textures/entity/tornado/tornado_fire.png");
        }
        return WindSpellbooksMod.id("textures/entity/tornado/tornado.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TornadoEntity entity) {
        return null;
    }
}