package net.raptorzizi.frierens_spellbooks.setup;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.raptorzizi.frierens_spellbooks.FrierensSpellbooksMod;

@EventBusSubscriber(modid = FrierensSpellbooksMod.MOD_ID)
// bus = EventBusSubscriber.Bus.MOD)
public class ModCommonSetup {

    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
    }

    @SubscribeEvent
    public static void spawnPlacements(RegisterSpawnPlacementsEvent event) {
    }
}