package net.raptorzizi.wind_spellbooks.item.armor;

import io.redspace.ironsspellbooks.entity.armor.GenericCustomArmorRenderer;
import io.redspace.ironsspellbooks.item.armor.ImbuableChestplateArmorItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.raptorzizi.wind_spellbooks.entity.armor.AeromancerArmorModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class AeromancerArmorItem extends ImbuableChestplateArmorItem {
    public AeromancerArmorItem(Type type, Properties settings) {
        super(WindArmorMaterial.AEROMANCER, type, settings);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {
        return new GenericCustomArmorRenderer<>(new AeromancerArmorModel());
    }
}
