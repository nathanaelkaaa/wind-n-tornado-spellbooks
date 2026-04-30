package net.raptorzizi.wind_spellbooks.effect;

import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class TailwindEffect extends MagicMobEffect {

    public static final float MAX_AIR_SPEED_PER_LEVEL = 0.15f;

    public TailwindEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
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
    }
}
