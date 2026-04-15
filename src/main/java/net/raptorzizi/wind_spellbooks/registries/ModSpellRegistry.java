package net.raptorzizi.wind_spellbooks.registries;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.raptorzizi.wind_spellbooks.WindSpellbooksMod;
import net.raptorzizi.wind_spellbooks.spells.wind.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModSpellRegistry {

    private static final DeferredRegister<AbstractSpell> SPELLS =
            DeferredRegister.create(SpellRegistry.SPELL_REGISTRY_KEY, WindSpellbooksMod.MOD_ID);

    public static void register(IEventBus eventBus) {
        SPELLS.register(eventBus);
    }

    public static Supplier<AbstractSpell> registerSpell(AbstractSpell spell) {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }

    public static final Supplier<AbstractSpell> WIND_JUMP_SPELL = registerSpell(new WindJumpSpell());
    public static final Supplier<AbstractSpell> TORNADO_SPELL = registerSpell(new TornadoSpell());
    public static final Supplier<AbstractSpell> IRON_SLASH_SPELL = registerSpell(new IronSlashSpell());
    public static final Supplier<AbstractSpell> ACROBATICS_SPELL = registerSpell(new AeropicSpell());
    public static final Supplier<AbstractSpell> ALMIGHTY_PUSH_SPELL = registerSpell(new AlmightyPushSpell());
    public static final Supplier<AbstractSpell> WIND_BLADE_SPELL = registerSpell(new WindBladeSpell());
    public static final Supplier<AbstractSpell> TAILWIND_SPELL = registerSpell(new TailwindSpell());
}