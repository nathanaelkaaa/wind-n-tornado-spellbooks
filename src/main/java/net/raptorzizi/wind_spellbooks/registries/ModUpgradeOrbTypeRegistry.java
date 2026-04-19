package net.raptorzizi.wind_spellbooks.registries;

import io.redspace.ironsspellbooks.item.armor.UpgradeOrbType;
import io.redspace.ironsspellbooks.registries.UpgradeOrbTypeRegistry;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.raptorzizi.wind_spellbooks.WindSpellbooksMod;

public class ModUpgradeOrbTypeRegistry {

    public static ResourceKey<UpgradeOrbType> WIND_SPELL_POWER = ResourceKey.create(
            UpgradeOrbTypeRegistry.UPGRADE_ORB_REGISTRY_KEY,
            WindSpellbooksMod.id("wind_power")
    );

    public static void bootstrap(BootstrapContext<UpgradeOrbType> bootstrap) {
        bootstrap.register(WIND_SPELL_POWER,
                new UpgradeOrbType(
                        ModAttributeRegistry.WIND_SPELL_POWER,
                        0.05,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                        ModItemsRegistry.WIND_UPGRADE_ORB
                )
        );
    }
}