package net.raptorzizi.wind_spellbooks.registries;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.raptorzizi.wind_spellbooks.WindSpellbooksMod;

public class ModSoundRegistry {
    private static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, WindSpellbooksMod.MOD_ID);

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }

    public static RegistryObject<SoundEvent> TORNADO_LOOP =
            registerSoundEvent("entity.tornado.loop");
    public static RegistryObject<SoundEvent> TORNADO_FIRE_LOOP =
            registerSoundEvent("entity.tornado.fire.loop");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () ->
                SoundEvent.createVariableRangeEvent(new ResourceLocation(WindSpellbooksMod.MOD_ID, name)));
    }
}
