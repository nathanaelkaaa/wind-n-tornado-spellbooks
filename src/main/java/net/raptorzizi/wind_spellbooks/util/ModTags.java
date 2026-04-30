package net.raptorzizi.wind_spellbooks.util;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.raptorzizi.wind_spellbooks.WindSpellbooksMod;

public class ModTags {

    public static final TagKey<Item> WIND_FOCUS = ItemTags.create(new ResourceLocation(IronsSpellbooks.MODID, "wind_focus"));

    private static TagKey<Item> itemTag(String name) {
        return ItemTags.create(new ResourceLocation(WindSpellbooksMod.MOD_ID, name));
    }

    private static TagKey<EntityType<?>> entityTag(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(WindSpellbooksMod.MOD_ID, name));
    }

    private static TagKey<DamageType> damageTag(String name) {
        return TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(WindSpellbooksMod.MOD_ID, name));
    }

    private static TagKey<Fluid> fluidTag(String name) {
        return TagKey.create(Registries.FLUID, new ResourceLocation(WindSpellbooksMod.MOD_ID, name));
    }
}
