package net.raptorzizi.wind_spellbooks.setup;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.raptorzizi.wind_spellbooks.WindSpellbooksMod;
import net.raptorzizi.wind_spellbooks.entity.mobs.wizards.aeromancer.AeromancerEntity;
import net.raptorzizi.wind_spellbooks.registries.ModEntityRegistry;

@EventBusSubscriber(modid = WindSpellbooksMod.MOD_ID)
// bus = EventBusSubscriber.Bus.MOD)
public class ModCommonSetup {

    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(ModEntityRegistry.AEROMANCER.get(), AeromancerEntity.prepareAttributes().build());
    }

    @SubscribeEvent
    public static void spawnPlacements(RegisterSpawnPlacementsEvent event) {
    }
}