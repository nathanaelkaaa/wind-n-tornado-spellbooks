package net.raptorzizi.wind_spellbooks.item.weapons;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.item.weapons.StaffTier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.raptorzizi.wind_spellbooks.registries.ModAttributeRegistry;

public class ModStaffTier {

    public static StaffTier WIND_STAFF = new StaffTier(4, -3,
            new AttributeContainer(AttributeRegistry.CASTING_MOVESPEED, .25, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(ModAttributeRegistry.WIND_SPELL_POWER, .15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(AttributeRegistry.SPELL_POWER, .05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
    );
}
