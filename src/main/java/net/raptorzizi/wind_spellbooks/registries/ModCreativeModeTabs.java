package net.raptorzizi.wind_spellbooks.registries;

import io.redspace.ironsspellbooks.registries.CreativeTabRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

public class ModCreativeModeTabs {

    @SubscribeEvent
    public static void fillCreativeTabs(final BuildCreativeModeTabContentsEvent event) {

        if (event.getTabKey() == CreativeTabRegistry.EQUIPMENT_TAB.getKey()) {
            event.accept(ModItemsRegistry.AEROMANCER_HELMET.get());
            event.accept(ModItemsRegistry.AEROMANCER_CHESTPLATE.get());
            event.accept(ModItemsRegistry.AEROMANCER_LEGGINGS.get());
            event.accept(ModItemsRegistry.AEROMANCER_BOOTS.get());
            event.accept(ModItemsRegistry.WIND_STAFF.get());
            event.accept(ModItemsRegistry.WIND_SPELL_BOOK.get());
        }

        if (event.getTabKey() == CreativeTabRegistry.MATERIALS_TAB.getKey()) {
            event.accept(ModItemsRegistry.AEROMANCER_SPAWN_EGG.get());
            event.accept(ModItemsRegistry.WIND_UPGRADE_ORB.get());
            event.accept(ModItemsRegistry.WIND_RUNE.get());
        }
    }

    public static void register(IEventBus eventBus) {
        eventBus.addListener(ModCreativeModeTabs::fillCreativeTabs);
    }
}