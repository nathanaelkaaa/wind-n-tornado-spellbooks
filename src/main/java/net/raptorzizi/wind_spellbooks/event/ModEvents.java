package net.raptorzizi.wind_spellbooks.event;

import io.redspace.ironsspellbooks.api.config.ModifyDefaultConfigValuesEvent;
import io.redspace.ironsspellbooks.api.config.SpellConfigParameter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.raptorzizi.wind_spellbooks.WindSpellbooksMod;
import net.raptorzizi.wind_spellbooks.effect.TailwindEffect;
import net.raptorzizi.wind_spellbooks.registries.ModMobEffectRegistry;
import net.raptorzizi.wind_spellbooks.registries.ModSchoolRegistry;

@Mod.EventBusSubscriber(modid = WindSpellbooksMod.MOD_ID)
public class ModEvents {

    private static final ResourceLocation GUST         = new ResourceLocation("irons_spellbooks", "gust");
    private static final ResourceLocation INVISIBILITY = new ResourceLocation("irons_spellbooks", "invisibility");
    private static final ResourceLocation THROW        = new ResourceLocation("irons_spellbooks", "throw");

    @SubscribeEvent
    public static void onModifySpellConfig(ModifyDefaultConfigValuesEvent event) {
        ResourceLocation spellId = event.getSpell().getSpellResource();
        if (spellId.equals(GUST) || spellId.equals(INVISIBILITY) || spellId.equals(THROW)) {
            event.setDefaultValue(SpellConfigParameter.SCHOOL, ModSchoolRegistry.WIND.get());
        }
    }

    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        LivingEntity entity = event.getEntity();

        if (entity.hasEffect(ModMobEffectRegistry.TAILWIND.get())) {
            int amplifier = entity.getEffect(ModMobEffectRegistry.TAILWIND.get()).getAmplifier();

            double multiplier = TailwindEffect.getJumpMultiplier(amplifier);
            Vec3 motion = entity.getDeltaMovement();

            entity.setDeltaMovement(motion.x, motion.y * (1.0 + multiplier), motion.z);
            entity.hasImpulse = true;
        }
    }
}