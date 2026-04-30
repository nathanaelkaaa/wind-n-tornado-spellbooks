package net.raptorzizi.wind_spellbooks.setup;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.render.EnergySwirlLayer;
import io.redspace.ironsspellbooks.render.SpellBookCurioRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import java.util.function.Predicate;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.raptorzizi.wind_spellbooks.WindSpellbooksMod;
import net.raptorzizi.wind_spellbooks.entity.mobs.wizards.aeromancer.AeromancerRenderer;
import net.raptorzizi.wind_spellbooks.entity.spells.almighty_push.AlmightyPushRenderer;
import net.raptorzizi.wind_spellbooks.entity.spells.iron_slash.IronSlashRenderer;
import net.raptorzizi.wind_spellbooks.entity.spells.iron_slash.SlashEffectRenderer;
import net.raptorzizi.wind_spellbooks.entity.spells.tornado.TornadoRenderer;
import net.raptorzizi.wind_spellbooks.entity.spells.wind_blade.WindBladeRenderer;
import net.raptorzizi.wind_spellbooks.particle.FeatherParticle;
import net.raptorzizi.wind_spellbooks.particle.TornadoFireSmokeParticle;
import net.raptorzizi.wind_spellbooks.particle.TornadoGroundSmokeParticle;
import net.raptorzizi.wind_spellbooks.registries.ModEntityRegistry;
import net.raptorzizi.wind_spellbooks.registries.ModItemsRegistry;
import net.raptorzizi.wind_spellbooks.registries.ModMobEffectRegistry;
import net.raptorzizi.wind_spellbooks.registries.ModParticleRegistry;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@Mod.EventBusSubscriber(
        modid = WindSpellbooksMod.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public class ModClientSetup {

    private static final ResourceLocation TAILWIND_TEXTURE =
            new ResourceLocation("wind_spellbooks", "textures/entity/tailwind.png");


    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        addLayerToPlayerSkin(event, "slim");
        addLayerToPlayerSkin(event, "default");
    }

    @SubscribeEvent
    public static void rendererRegister(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityRegistry.WIND_BLADE_PROJECTILE.get(), WindBladeRenderer::new);
        event.registerEntityRenderer(ModEntityRegistry.ALMIGHTY_PUSH_ENTITY.get(), AlmightyPushRenderer::new);
        event.registerEntityRenderer(ModEntityRegistry.SLASH_EFFECT_ENTITY.get(), SlashEffectRenderer::new);
        event.registerEntityRenderer(ModEntityRegistry.IRON_SLASH_ENTITY.get(), IronSlashRenderer::new);
        event.registerEntityRenderer(ModEntityRegistry.TORNADO_ENTITY.get(), TornadoRenderer::new);
        event.registerEntityRenderer(ModEntityRegistry.AEROMANCER.get(), AeromancerRenderer::new);
    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticleRegistry.TORNADO_FIRE_SMOKE.get(), TornadoFireSmokeParticle.Provider::new);
        event.registerSpriteSet(ModParticleRegistry.TORNADO_GROUND_SMOKE.get(), TornadoGroundSmokeParticle.Provider::new);
        event.registerSpriteSet(ModParticleRegistry.TORNADO_GROUND_FIRE_SMOKE.get(), TornadoGroundSmokeParticle.Provider::new);
        event.registerSpriteSet(ModParticleRegistry.FEATHER.get(), FeatherParticle.Provider::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            CuriosRendererRegistry.register(
                    ModItemsRegistry.WIND_SPELL_BOOK.get(),
                    SpellBookCurioRenderer::new
            );
        });
    }

    private static void addLayerToPlayerSkin(EntityRenderersEvent.AddLayers event, String skinName) {
        EntityRenderer<? extends Player> render = event.getSkin(skinName);
        if (render instanceof LivingEntityRenderer livingRenderer) {
            livingRenderer.addLayer(new EnergySwirlLayer.Vanilla(livingRenderer, TAILWIND_TEXTURE, (Predicate<LivingEntity>) e -> e.hasEffect(ModMobEffectRegistry.TAILWIND.get())) {
                @Override
                public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight,
                                   Player pLivingEntity, float pLimbSwing, float pLimbSwingAmount,
                                   float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
                    if (pLivingEntity.hasEffect(ModMobEffectRegistry.TAILWIND.get())) {
                        float f = (float) pLivingEntity.tickCount + pPartialTicks;
                        HumanoidModel<Player> entitymodel = this.model();
                        VertexConsumer vertexconsumer = pBuffer.getBuffer(
                                RenderType.energySwirl(TAILWIND_TEXTURE, f * 0.02F % 1.0F, 0.0F)
                        );
                        this.getParentModel().copyPropertiesTo(entitymodel);
                        entitymodel.renderToBuffer(pMatrixStack, vertexconsumer, pPackedLight,
                                OverlayTexture.NO_OVERLAY, 0.616f, 0.616f, 0.616f, 0.333f);
                    }
                }
            });
        }
    }
}
