package net.raptorzizi.wind_spellbooks.damage;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.raptorzizi.wind_spellbooks.WindSpellbooksMod;

public class ModDamageTypes {

    private static ResourceKey<DamageType> register(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE,
                ResourceLocation.fromNamespaceAndPath(WindSpellbooksMod.MOD_ID, name));
    }

    public static final ResourceKey<DamageType> WIND_MAGIC = register("wind");

    public static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(WIND_MAGIC, new DamageType(
                WIND_MAGIC.location().getPath(),
                DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER,
                0f
        ));
    }
}