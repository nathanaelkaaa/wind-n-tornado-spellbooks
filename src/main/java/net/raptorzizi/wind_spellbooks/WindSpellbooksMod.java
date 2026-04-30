package net.raptorzizi.wind_spellbooks;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.raptorzizi.wind_spellbooks.config.ModClientConfigs;
import net.raptorzizi.wind_spellbooks.config.ModServerConfigs;
import net.raptorzizi.wind_spellbooks.registries.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

@Mod(WindSpellbooksMod.MOD_ID)
public class WindSpellbooksMod {
    public static final String MOD_ID = "wind_spellbooks";
    public static final Logger LOGGER = LogUtils.getLogger();

    public WindSpellbooksMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);

        ModCreativeModeTabs.register(modEventBus);
        ModAttributeRegistry.register(modEventBus);
        ModSchoolRegistry.register(modEventBus);
        ModSpellRegistry.register(modEventBus);
        ModEntityRegistry.register(modEventBus);
        ModItemsRegistry.register(modEventBus);
        ModParticleRegistry.register(modEventBus);
        ModSoundRegistry.register(modEventBus);
        ModMobEffectRegistry.register(modEventBus);

        ModUpgradeOrbTypeRegistry.init();

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ModServerConfigs.SPEC, String.format("%s-server.toml", MOD_ID));
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ModClientConfigs.SPEC, String.format("%s-client.toml", MOD_ID));
    }

    public static ResourceLocation id(@NotNull String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
