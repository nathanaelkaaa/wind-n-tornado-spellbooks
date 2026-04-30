package net.raptorzizi.wind_spellbooks.registries;

import io.redspace.ironsspellbooks.api.attribute.MagicPercentAttribute;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.raptorzizi.wind_spellbooks.WindSpellbooksMod;

@Mod.EventBusSubscriber(modid = WindSpellbooksMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModAttributeRegistry {

    private static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(ForgeRegistries.ATTRIBUTES, WindSpellbooksMod.MOD_ID);

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }

    public static final RegistryObject<Attribute> WIND_MAGIC_RESIST = newResistanceAttribute("wind");
    public static final RegistryObject<Attribute> WIND_SPELL_POWER  = newPowerAttribute("wind");

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent e) {
        e.getTypes().forEach(entity ->
                ATTRIBUTES.getEntries().forEach(attribute -> e.add(entity, attribute.get()))
        );
    }

    private static RegistryObject<Attribute> newResistanceAttribute(String id) {
        return ATTRIBUTES.register(id + "_magic_resist", () ->
                new MagicPercentAttribute(
                        "attribute.wind_spellbooks." + id + "_magic_resist",
                        1.0D, -100, 100
                ).setSyncable(true)
        );
    }

    private static RegistryObject<Attribute> newPowerAttribute(String id) {
        return ATTRIBUTES.register(id + "_spell_power", () ->
                new MagicPercentAttribute(
                        "attribute.wind_spellbooks." + id + "_spell_power",
                        1.0D, -100, 100
                ).setSyncable(true)
        );
    }
}
