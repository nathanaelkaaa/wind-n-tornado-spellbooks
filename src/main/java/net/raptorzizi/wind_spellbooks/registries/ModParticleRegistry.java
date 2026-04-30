package net.raptorzizi.wind_spellbooks.registries;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.raptorzizi.wind_spellbooks.WindSpellbooksMod;

import java.util.function.Supplier;

public class ModParticleRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, WindSpellbooksMod.MOD_ID);

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
