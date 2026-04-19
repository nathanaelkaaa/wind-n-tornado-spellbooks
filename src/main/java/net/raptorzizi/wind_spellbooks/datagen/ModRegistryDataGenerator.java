package net.raptorzizi.wind_spellbooks.datagen;

import io.redspace.ironsspellbooks.registries.UpgradeOrbTypeRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.raptorzizi.wind_spellbooks.WindSpellbooksMod;
import net.raptorzizi.wind_spellbooks.damage.ModDamageTypes;
import net.raptorzizi.wind_spellbooks.registries.ModUpgradeOrbTypeRegistry;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModRegistryDataGenerator extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, ModDamageTypes::bootstrap)
            .add(UpgradeOrbTypeRegistry.UPGRADE_ORB_REGISTRY_KEY, ModUpgradeOrbTypeRegistry::bootstrap);

    public ModRegistryDataGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, BUILDER, Set.of(WindSpellbooksMod.MOD_ID));
    }
}