package net.raptorzizi.frierens_spellbooks.mixin;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.spells.evocation.GustSpell;
import io.redspace.ironsspellbooks.spells.evocation.InvisibilitySpell;
import net.raptorzizi.frierens_spellbooks.registries.ModSchoolRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InvisibilitySpell.class)
public class MixinInvisibilitySpell {

    private final DefaultConfig frierenConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.RARE)
            .setSchoolResource(ModSchoolRegistry.WIND_RESOURCE)
            .setMaxLevel(6)
            .setCooldownSeconds((double)45.0F)
            .build();

    @Inject(method = "getDefaultConfig", at = @At("HEAD"), remap = false, cancellable = true)
    private void overrideDefaultConfig(CallbackInfoReturnable<DefaultConfig> cir) {
        cir.setReturnValue(frierenConfig);
    }
}