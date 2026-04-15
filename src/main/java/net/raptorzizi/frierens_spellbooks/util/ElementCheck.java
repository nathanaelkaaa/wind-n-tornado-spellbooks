package net.raptorzizi.frierens_spellbooks.util;

import io.redspace.ironsspellbooks.entity.spells.snowball.FrostField;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Set;

public class ElementCheck {

    private static final Set<ResourceLocation> FIRE_ENTITY_IDS = Set.of(
            ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "firebolt"),
            ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "magma_ball"),
            ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "fire_arrow_projectile"),
            ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "magic_fireball"),
            ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "fire_breath"),
            ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "blaze_storm"),
            ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "fire_field"),
            ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "flaming_strike"),
            ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "heat_surge"),
            ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "wall_of_fire"),
            ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "scorch"),
            ResourceLocation.fromNamespaceAndPath("minecraft", "fireball"),
            ResourceLocation.fromNamespaceAndPath("minecraft", "small_fireball"),
            ResourceLocation.fromNamespaceAndPath("minecraft", "dragon_fireball")
    );

    private static final Set<ResourceLocation> ICE_ENTITY_IDS = Set.of(
            ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "snowball"),
            ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "frost_field")
    );

    public static boolean isFireEntity(Entity e) {
        if (e instanceof Fireball) return true;
        ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(e.getType());
        if (id != null && FIRE_ENTITY_IDS.contains(id)) return true;
        return false;
    }

    public static boolean hasIceConditions(LivingEntity caster) {

        if (!caster.hasEffect(MobEffectRegistry.FROSTBITTEN_STRIKES)) return isInIceEntity(caster);
        return true;
    }

    //Player

    //Ajouter la logique par ID TODO
    private static boolean isInIceEntity(LivingEntity caster) {
        Level level = caster.level();
        List<Entity> nearby = level.getEntities(null,
                new AABB(caster.position(), caster.position()).inflate(8));

        for (Entity e : nearby) {
            if (e instanceof FrostField frostField) {
                double dist = caster.distanceTo(frostField);
                if (dist <= frostField.getRadius()) {
                    return true;
                }
            }
        }
        return false;
    }
}