package net.raptorzizi.wind_spellbooks.util;

import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Fireball;

import java.util.Set;

public class ElementCheck {

    private static final Set<ResourceLocation> FIRE_ENTITY_IDS = Set.of(
            new ResourceLocation("irons_spellbooks", "firebolt"),
            new ResourceLocation("irons_spellbooks", "magma_ball"),
            new ResourceLocation("irons_spellbooks", "fire_arrow_projectile"),
            new ResourceLocation("irons_spellbooks", "magic_fireball"),
            new ResourceLocation("irons_spellbooks", "fire_breath"),
            new ResourceLocation("irons_spellbooks", "blaze_storm"),
            new ResourceLocation("irons_spellbooks", "fire_field"),
            new ResourceLocation("irons_spellbooks", "flaming_strike"),
            new ResourceLocation("irons_spellbooks", "heat_surge"),
            new ResourceLocation("irons_spellbooks", "wall_of_fire"),
            new ResourceLocation("irons_spellbooks", "scorch"),
            new ResourceLocation("minecraft", "fireball"),
            new ResourceLocation("minecraft", "small_fireball"),
            new ResourceLocation("minecraft", "dragon_fireball")
    );

    private static final Set<ResourceLocation> ICE_ENTITY_IDS = Set.of(
            new ResourceLocation("irons_spellbooks", "snowball"),
            new ResourceLocation("irons_spellbooks", "frost_field")
    );

    public static boolean isFireEntity(Entity e) {
        if (e instanceof Fireball) return true;
        ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(e.getType());
        if (id != null && FIRE_ENTITY_IDS.contains(id)) return true;
        return false;
    }

    public static boolean hasIceConditions(LivingEntity caster) {
        if (!caster.hasEffect(MobEffectRegistry.FROSTBITTEN_STRIKES.get())) return isInIceEntity(caster);
        return true;
    }

    private static boolean isInIceEntity(LivingEntity caster) {
        return false;
    }
}
