package net.raptorzizi.wind_spellbooks.registries;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.raptorzizi.wind_spellbooks.WindSpellbooksMod;
import net.raptorzizi.wind_spellbooks.effect.AeropicEffect;
import net.raptorzizi.wind_spellbooks.effect.TailwindEffect;

public class ModMobEffectRegistry {

    public static final DeferredRegister<MobEffect> MOB_EFFECT_DEFERRED_REGISTER =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, WindSpellbooksMod.MOD_ID);

    public static void register(IEventBus eventBus) {
        MOB_EFFECT_DEFERRED_REGISTER.register(eventBus);
    }

    public static final RegistryObject<MobEffect> ACROBATICS =
            MOB_EFFECT_DEFERRED_REGISTER.register("aeropic",
                    () -> new AeropicEffect(MobEffectCategory.BENEFICIAL, 0x88FF44));

    public static final RegistryObject<TailwindEffect> TAILWIND =
            MOB_EFFECT_DEFERRED_REGISTER.register("tailwind",
                    () -> new TailwindEffect(MobEffectCategory.BENEFICIAL, 0x88CCFF));
}
