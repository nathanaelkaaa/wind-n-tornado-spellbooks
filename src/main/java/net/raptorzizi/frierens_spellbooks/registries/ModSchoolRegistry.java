package net.raptorzizi.frierens_spellbooks.registries;

import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.raptorzizi.frierens_spellbooks.FrierensSpellbooksMod;
import net.raptorzizi.frierens_spellbooks.damage.ModDamageTypes;
import net.raptorzizi.frierens_spellbooks.util.ModTags;

import java.util.function.Supplier;
import static io.redspace.ironsspellbooks.api.registry.SchoolRegistry.SCHOOL_REGISTRY_KEY;

public class ModSchoolRegistry{

    private static final DeferredRegister<SchoolType> SCHOOLS = DeferredRegister.create(SCHOOL_REGISTRY_KEY, FrierensSpellbooksMod.MOD_ID);

    public static void register(IEventBus eventBus) {
        SCHOOLS.register(eventBus);
    }

    private static Supplier<SchoolType> registerSchool(SchoolType schoolType) {
        return SCHOOLS.register(schoolType.getId().getPath(), () -> schoolType);
    }

    public static final ResourceLocation WIND_RESOURCE = FrierensSpellbooksMod.id("wind");

    public static final Supplier<SchoolType> WIND = registerSchool(new SchoolType(
            WIND_RESOURCE,
            ModTags.WIND_FOCUS,
            Component.translatable("school.frierens_spellbooks.wind").withStyle(ChatFormatting.GRAY),
            ModAttributeRegistry.WIND_SPELL_POWER,
            ModAttributeRegistry.WIND_MAGIC_RESIST,
            SoundRegistry.EVOCATION_CAST,
            ModDamageTypes.WIND_MAGIC
    ));




}