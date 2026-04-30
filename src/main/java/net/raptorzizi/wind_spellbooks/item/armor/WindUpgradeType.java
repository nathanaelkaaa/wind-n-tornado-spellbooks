package net.raptorzizi.wind_spellbooks.item.armor;

import io.redspace.ironsspellbooks.item.armor.UpgradeType;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.raptorzizi.wind_spellbooks.WindSpellbooksMod;
import net.raptorzizi.wind_spellbooks.registries.ModAttributeRegistry;
import net.raptorzizi.wind_spellbooks.registries.ModItemsRegistry;

import java.util.Optional;

public enum WindUpgradeType implements UpgradeType {
    WIND_SPELL_POWER("wind_power", AttributeModifier.Operation.MULTIPLY_BASE, 0.05f);

    private final ResourceLocation id;
    private final AttributeModifier.Operation operation;
    private final float amountPerUpgrade;

    WindUpgradeType(String key, AttributeModifier.Operation operation, float amountPerUpgrade) {
        this.id = new ResourceLocation(WindSpellbooksMod.MOD_ID, key);
        this.operation = operation;
        this.amountPerUpgrade = amountPerUpgrade;
        UpgradeType.registerUpgrade(this);
    }

    @Override
    public Holder<Attribute> getAttribute() {
        return ModAttributeRegistry.WIND_SPELL_POWER.getHolder().orElseThrow();
    }

    @Override
    public AttributeModifier.Operation getOperation() {
        return operation;
    }

    @Override
    public float getAmountPerUpgrade() {
        return amountPerUpgrade;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public Optional<Holder<Item>> getContainerItem() {
        return ModItemsRegistry.WIND_UPGRADE_ORB.getHolder();
    }
}
