package net.raptorzizi.wind_spellbooks.datagen;

import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.raptorzizi.wind_spellbooks.registries.ModItemsRegistry;
import net.raptorzizi.wind_spellbooks.registries.ModSchoolRegistry;

import java.util.concurrent.CompletableFuture;

import static io.redspace.ironsspellbooks.datagen.IronRecipeProvider.schoolArmorSmithing;
import static io.redspace.ironsspellbooks.datagen.IronRecipeProvider.upgradeOrbRecipe;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        schoolArmorSmithing(output, ModSchoolRegistry.WIND.get(), "aeromancer");
        upgradeOrbRecipe(output, ModItemsRegistry.WIND_RUNE.get(), ModItemsRegistry.WIND_UPGRADE_ORB.get());
    }
}