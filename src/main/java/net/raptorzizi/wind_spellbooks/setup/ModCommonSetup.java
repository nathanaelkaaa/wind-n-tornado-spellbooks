package net.raptorzizi.wind_spellbooks.setup;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.raptorzizi.wind_spellbooks.WindSpellbooksMod;
import net.raptorzizi.wind_spellbooks.entity.mobs.wizards.aeromancer.AeromancerEntity;
import net.raptorzizi.wind_spellbooks.registries.ModEntityRegistry;

@Mod.EventBusSubscriber(modid = WindSpellbooksMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCommonSetup {

    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(ModEntityRegistry.AEROMANCER.get(), AeromancerEntity.prepareAttributes().build());
    }

    @SubscribeEvent
    public static void spawnPlacements(SpawnPlacementRegisterEvent event) {
    }
}
