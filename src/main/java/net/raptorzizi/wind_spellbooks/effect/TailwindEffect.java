package net.raptorzizi.wind_spellbooks.effect;

import io.redspace.ironsspellbooks.effect.ISyncedMobEffect;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.raptorzizi.wind_spellbooks.WindSpellbooksMod;

public class TailwindEffect extends MagicMobEffect implements ISyncedMobEffect {

    public static final float MAX_AIR_SPEED_PER_LEVEL = 0.15f;
    public static final float JUMP_BASE               = 0.50f;
    public static final float JUMP_PER_LEVEL          = 0.30f;

    private static final ResourceLocation SPEED_ID =
            ResourceLocation.fromNamespaceAndPath(WindSpellbooksMod.MOD_ID, "tailwind_speed");
    private static final ResourceLocation JUMP_ID =
            ResourceLocation.fromNamespaceAndPath(WindSpellbooksMod.MOD_ID, "tailwind_jump");

    public TailwindEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {

        var jumpAttr = entity.getAttribute(Attributes.JUMP_STRENGTH);
        if (jumpAttr != null) {
            jumpAttr.removeModifier(JUMP_ID);
            jumpAttr.addTransientModifier(new AttributeModifier(
                    JUMP_ID,
                    JUMP_BASE + JUMP_PER_LEVEL * amplifier,
                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            ));
        }
    }

    @Override
    public void onEffectRemoved(LivingEntity entity, int amplifier) {
        var speedAttr = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speedAttr != null) speedAttr.removeModifier(SPEED_ID);

        var jumpAttr = entity.getAttribute(Attributes.JUMP_STRENGTH);
        if (jumpAttr != null) jumpAttr.removeModifier(JUMP_ID);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        entity.fallDistance = 0;

        if (!entity.onGround() && entity.getDeltaMovement().y < 0
                && entity.getDeltaMovement().y > -0.5) {
            entity.setDeltaMovement(
                    entity.getDeltaMovement().x,
                    Math.max(entity.getDeltaMovement().y, -0.10),
                    entity.getDeltaMovement().z
            );
        }

        if (!entity.onGround() && entity instanceof Player player
                && (player.xxa != 0 || player.zza != 0)) {

            double dx = entity.getDeltaMovement().x;
            double dz = entity.getDeltaMovement().z;
            double horizontalSpeed = Math.sqrt(dx * dx + dz * dz);
            float maxAirSpeed = 0.1f + MAX_AIR_SPEED_PER_LEVEL * (amplifier + 1);

            if (horizontalSpeed > 0.01 && horizontalSpeed < maxAirSpeed) {
                double boost = Math.min(0.004 * (amplifier + 1), maxAirSpeed - horizontalSpeed);
                double nx = dx / horizontalSpeed;
                double nz = dz / horizontalSpeed;
                entity.setDeltaMovement(
                        dx + nx * boost,
                        entity.getDeltaMovement().y,
                        dz + nz * boost
                );
            }
        }
        return true;
    }
}