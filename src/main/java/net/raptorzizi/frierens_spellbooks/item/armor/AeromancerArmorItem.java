package net.raptorzizi.frierens_spellbooks.item.armor;

import io.redspace.ironsspellbooks.entity.armor.GenericCustomArmorRenderer;
import io.redspace.ironsspellbooks.item.armor.ImbuableChestplateArmorItem;
import io.redspace.ironsspellbooks.registries.ArmorMaterialRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.raptorzizi.frierens_spellbooks.entity.armor.AeromancerArmorModel;
import net.raptorzizi.frierens_spellbooks.registries.ModAttributeRegistry;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class AeromancerArmorItem extends ImbuableChestplateArmorItem {
    public AeromancerArmorItem(Type type, Properties settings) {
        super(ArmorMaterialRegistry.SCHOOL, type, settings, schoolAttributes(ModAttributeRegistry.WIND_SPELL_POWER));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {
        return new GenericCustomArmorRenderer<>(new AeromancerArmorModel());
    }
}
