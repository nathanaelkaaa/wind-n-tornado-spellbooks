package net.raptorzizi.frierens_spellbooks.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.raptorzizi.frierens_spellbooks.FrierensSpellbooksMod;


public class ModSoundRegistry {
    private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, FrierensSpellbooksMod.MOD_ID);

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }

    //TODO: bring sound locations into spec

    public static DeferredHolder<SoundEvent, SoundEvent> TORNADO_LOOP = registerSoundEvent("entity.tornado.loop");
    public static DeferredHolder<SoundEvent, SoundEvent> TORNADO_FIRE_LOOP = registerSoundEvent("entity.tornado.fire.loop");


    private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(FrierensSpellbooksMod.MOD_ID, name)));
    }
}
