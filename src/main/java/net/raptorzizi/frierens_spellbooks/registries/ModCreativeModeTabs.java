package net.raptorzizi.frierens_spellbooks.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.raptorzizi.frierens_spellbooks.FrierensSpellbooksMod;

import static io.redspace.ironsspellbooks.registries.CreativeTabRegistry.EQUIPMENT_TAB;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FrierensSpellbooksMod.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MATERIALS_TAB = CREATIVE_MODE_TAB.register("spellbook_materials",
            () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + FrierensSpellbooksMod.MOD_ID + ".creative_tab"))
            .icon(() -> new ItemStack(Items.WIND_CHARGE))
            .displayItems((enabledFeatures, entries) -> {
                entries.accept(ModItemsRegistry.AEROMANCER_HELMET.get());
                entries.accept(ModItemsRegistry.AEROMANCER_CHESTPLATE.get());
                entries.accept(ModItemsRegistry.AEROMANCER_LEGGINGS.get());
                entries.accept(ModItemsRegistry.AEROMANCER_BOOTS.get());
            })
            .withTabsBefore(EQUIPMENT_TAB.getKey())
            .build());


    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
