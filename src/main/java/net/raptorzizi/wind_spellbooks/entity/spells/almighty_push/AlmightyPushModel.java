package net.raptorzizi.wind_spellbooks.entity.spells.almighty_push;

import net.minecraft.resources.ResourceLocation;
import net.raptorzizi.wind_spellbooks.WindSpellbooksMod;
import software.bernie.geckolib.model.GeoModel;

public class AlmightyPushModel extends GeoModel<AlmightyPushEntity> {

    @Override
    public ResourceLocation getModelResource(AlmightyPushEntity entity) {
        return WindSpellbooksMod.id("geo/almighty_push.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AlmightyPushEntity entity) {
        return WindSpellbooksMod.id("textures/entity/almighty_push/almighty_push.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AlmightyPushEntity entity) {
        return null;
    }
}