package net.raptorzizi.wind_spellbooks.entity.armor;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.minecraft.resources.ResourceLocation;
import net.raptorzizi.wind_spellbooks.WindSpellbooksMod;
import net.raptorzizi.wind_spellbooks.item.armor.AeromancerArmorItem;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class AeromancerArmorModel extends DefaultedItemGeoModel<AeromancerArmorItem> {

    public AeromancerArmorModel() {
        super(ResourceLocation.fromNamespaceAndPath(WindSpellbooksMod.MOD_ID, ""));
    }

    @Override
    public ResourceLocation getModelResource(AeromancerArmorItem object) {
        return ResourceLocation.fromNamespaceAndPath(WindSpellbooksMod.MOD_ID, "geo/aeromancer_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AeromancerArmorItem object) {
        return ResourceLocation.fromNamespaceAndPath(WindSpellbooksMod.MOD_ID, "textures/models/armor/aeromancer.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AeromancerArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "animations/wizard_armor_animation.json");
    }
}