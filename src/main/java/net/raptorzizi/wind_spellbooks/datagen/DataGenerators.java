package net.raptorzizi.wind_spellbooks.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.raptorzizi.wind_spellbooks.WindSpellbooksMod;

@Mod.EventBusSubscriber(modid = WindSpellbooksMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();

        ModRegistryDataGenerator datapackProvider = new ModRegistryDataGenerator(output, event.getLookupProvider());
        generator.addProvider(event.includeServer(), datapackProvider);
        generator.addProvider(event.includeServer(), new ModDamageTypeTagGenerator(output, datapackProvider.getRegistryProvider(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new ModRecipeProvider(output));
    }
}