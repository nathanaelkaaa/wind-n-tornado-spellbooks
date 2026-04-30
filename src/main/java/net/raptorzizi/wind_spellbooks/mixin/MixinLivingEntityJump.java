package net.raptorzizi.wind_spellbooks.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.raptorzizi.wind_spellbooks.effect.TailwindEffect;
import net.raptorzizi.wind_spellbooks.registries.ModMobEffectRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class MixinLivingEntityJump {

    @Inject(method = "jumpFromGround", at = @At("TAIL"))
    private void wind_spellbooks$boostJumpWithTailwind(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        MobEffectInstance effect = self.getEffect(ModMobEffectRegistry.TAILWIND.get());
        if (effect == null) return;

        double multiplier = TailwindEffect.getJumpMultiplier(effect.getAmplifier());
        Vec3 motion = self.getDeltaMovement();
        // On multiplie uniquement la composante Y (hauteur de saut)
        self.setDeltaMovement(motion.x, motion.y * multiplier, motion.z);
    }
}
