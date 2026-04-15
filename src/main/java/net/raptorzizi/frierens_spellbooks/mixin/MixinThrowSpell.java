package net.raptorzizi.frierens_spellbooks.mixin;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.spells.evocation.ThrowSpell;
import net.raptorzizi.frierens_spellbooks.registries.ModSchoolRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ThrowSpell.class)
public class MixinThrowSpell {

    private final DefaultConfig frierenConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setSchoolResource(ModSchoolRegistry.WIND_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(8)
            .build();

    @Inject(method = "getDefaultConfig", at = @At("HEAD"), remap = false, cancellable = true)
    private void overrideDefaultConfig(CallbackInfoReturnable<DefaultConfig> cir) {
        cir.setReturnValue(frierenConfig);
    }
}