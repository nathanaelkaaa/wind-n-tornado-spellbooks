package net.raptorzizi.frierens_spellbooks.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.raptorzizi.frierens_spellbooks.FrierensSpellbooksMod;
import net.raptorzizi.frierens_spellbooks.effect.AeropicEffect;
import net.raptorzizi.frierens_spellbooks.effect.TailwindEffect;

public class ModMobEffectRegistry {

    public static final DeferredRegister<MobEffect> MOB_EFFECT_DEFERRED_REGISTER =
            DeferredRegister.create(Registries.MOB_EFFECT, FrierensSpellbooksMod.MOD_ID);

    public static void register(IEventBus eventBus) {
        MOB_EFFECT_DEFERRED_REGISTER.register(eventBus);
    }

    public static final DeferredHolder<MobEffect, MobEffect> ACROBATICS =
            MOB_EFFECT_DEFERRED_REGISTER.register("aeropic",
                    () -> new AeropicEffect(MobEffectCategory.BENEFICIAL, 0x88FF44));
    public static final DeferredHolder<MobEffect, TailwindEffect> TAILWIND =
            MOB_EFFECT_DEFERRED_REGISTER.register("tailwind", () ->
                    new TailwindEffect(MobEffectCategory.BENEFICIAL, 0x88CCFF));
}