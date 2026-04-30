package net.raptorzizi.wind_spellbooks.effect;

import io.redspace.ironsspellbooks.effect.ISyncedMobEffect;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class TailwindEffect extends MagicMobEffect implements ISyncedMobEffect {
    public static final float MAX_AIR_SPEED_PER_LEVEL = 0.15f;
    public static final float JUMP_BASE = 0.50f;
    public static final float JUMP_PER_LEVEL = 0.30f;

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

        Vec3 motion = entity.getDeltaMovement();

        if (!entity.onGround() && motion.y < 0) {
            double slowFallSpeed = -0.12;
            entity.setDeltaMovement(motion.x, Math.max(motion.y, slowFallSpeed), motion.z);
            motion = entity.getDeltaMovement();
        }

        if (!entity.onGround() && entity instanceof Player player && (player.xxa != 0 || player.zza != 0)) {
            double dx = motion.x;
            double dz = motion.z;
            double horizontalSpeed = Math.sqrt(dx * dx + dz * dz);
            float maxAirSpeed = 0.1f + MAX_AIR_SPEED_PER_LEVEL * (amplifier + 1);

            if (horizontalSpeed > 0.01 && horizontalSpeed < maxAirSpeed) {
                double boost = Math.min(0.004 * (amplifier + 1), maxAirSpeed - horizontalSpeed);
                double nx = dx / horizontalSpeed;
                double nz = dz / horizontalSpeed;
                entity.setDeltaMovement(dx + nx * boost, motion.y, dz + nz * boost);
            }
        }
    }

    public static double getJumpMultiplier(int amplifier) {
        return JUMP_BASE + (JUMP_PER_LEVEL * amplifier);
    }
}