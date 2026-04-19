package net.raptorzizi.wind_spellbooks;

import io.redspace.ironsspellbooks.registries.UpgradeOrbTypeRegistry;
import net.minecraft.resources.ResourceLocation;
import net.raptorzizi.wind_spellbooks.config.ModClientConfigs;
import net.raptorzizi.wind_spellbooks.config.ModServerConfigs;
import net.raptorzizi.wind_spellbooks.registries.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(WindSpellbooksMod.MOD_ID)
public class WindSpellbooksMod {
    public static final String MOD_ID = "wind_spellbooks";
    public static final Logger LOGGER = LogUtils.getLogger();

    public WindSpellbooksMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);

        ModCreativeModeTabs.register(modEventBus);
        ModAttributeRegistry.register(modEventBus);
        ModSchoolRegistry.register(modEventBus);
        ModSpellRegistry.register(modEventBus);
        ModEntityRegistry.register(modEventBus);
        ModArmorMaterialRegistry.register(modEventBus);
        ModItemsRegistry.register(modEventBus);
        ModParticleRegistry.register(modEventBus);
        ModSoundRegistry.register(modEventBus);
        ModMobEffectRegistry.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.SERVER, ModServerConfigs.SPEC, String.format("%s-server.toml", WindSpellbooksMod.MOD_ID));
        modContainer.registerConfig(ModConfig.Type.CLIENT, ModClientConfigs.SPEC, String.format("%s-client.toml", WindSpellbooksMod.MOD_ID));
    }

    private void commonSetup(FMLCommonSetupEvent event) {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    public static ResourceLocation id(@NotNull String path) {
        return ResourceLocation.fromNamespaceAndPath(WindSpellbooksMod.MOD_ID, path);
    }

}
