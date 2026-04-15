package net.raptorzizi.frierens_spellbooks.registries;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.raptorzizi.frierens_spellbooks.FrierensSpellbooksMod;

import java.util.function.Supplier;

public class ModParticleRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, FrierensSpellbooksMod.MOD_ID);

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }

    public static final Supplier<SimpleParticleType> TORNADO_FIRE_SMOKE =
            PARTICLE_TYPES.register("tornado_fire_smoke", () -> new SimpleParticleType(true));

    public static final Supplier<SimpleParticleType> TORNADO_GROUND_SMOKE =
            PARTICLE_TYPES.register("tornado_ground_smoke", () -> new SimpleParticleType(true));

    public static final Supplier<SimpleParticleType> TORNADO_GROUND_FIRE_SMOKE =
            PARTICLE_TYPES.register("tornado_ground_fire_smoke", () -> new SimpleParticleType(true));

    public static final Supplier<SimpleParticleType> FEATHER =
            PARTICLE_TYPES.register("feather", () -> new SimpleParticleType(true));
}