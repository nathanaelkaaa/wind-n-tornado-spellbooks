package net.raptorzizi.wind_spellbooks.setup;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.render.EnergySwirlLayer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.raptorzizi.wind_spellbooks.WindSpellbooksMod;
import net.raptorzizi.wind_spellbooks.entity.spells.almighty_push.AlmightyPushRenderer;
import net.raptorzizi.wind_spellbooks.entity.spells.iron_slash.IronSlashRenderer;
import net.raptorzizi.wind_spellbooks.entity.spells.iron_slash.SlashEffectRenderer;
import net.raptorzizi.wind_spellbooks.entity.spells.tornado.TornadoRenderer;
import net.raptorzizi.wind_spellbooks.entity.spells.wind_blade.WindBladeRenderer;
import net.raptorzizi.wind_spellbooks.particle.FeatherParticle;
import net.raptorzizi.wind_spellbooks.particle.TornadoGroundSmokeParticle;
import net.raptorzizi.wind_spellbooks.particle.TornadoFireSmokeParticle;
import net.raptorzizi.wind_spellbooks.registries.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(
        modid = WindSpellbooksMod.MOD_ID,
        //bus = EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public class ModClientSetup {

    private static final ResourceLocation TAILWIND_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("wind_spellbooks", "textures/entity/tailwind.png");


    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        addLayerToPlayerSkin(event, net.minecraft.client.resources.PlayerSkin.Model.SLIM);
        addLayerToPlayerSkin(event, net.minecraft.client.resources.PlayerSkin.Model.WIDE);
    }

    @SubscribeEvent
    public static void rendererRegister(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityRegistry.WIND_BLADE_PROJECTILE.get(), WindBladeRenderer::new);
        event.registerEntityRenderer(ModEntityRegistry.ALMIGHTY_PUSH_ENTITY.get(), AlmightyPushRenderer::new);
        event.registerEntityRenderer(ModEntityRegistry.SLASH_EFFECT_ENTITY.get(), SlashEffectRenderer::new);
        event.registerEntityRenderer(ModEntityRegistry.IRON_SLASH_ENTITY.get(), IronSlashRenderer::new);
        event.registerEntityRenderer(ModEntityRegistry.TORNADO_ENTITY.get(), TornadoRenderer::new);
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
    }

    private static void addLayerToPlayerSkin(EntityRenderersEvent.AddLayers event,
                                             net.minecraft.client.resources.PlayerSkin.Model skinName) {
        var render = event.getSkin(skinName);
        if (render instanceof LivingEntityRenderer livingRenderer) {
            livingRenderer.addLayer(new EnergySwirlLayer.Vanilla(livingRenderer, TAILWIND_TEXTURE, ModMobEffectRegistry.TAILWIND) {
                @Override
                public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight,
                                   Player pLivingEntity, float pLimbSwing, float pLimbSwingAmount,
                                   float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
                    if (pLivingEntity.hasEffect(ModMobEffectRegistry.TAILWIND)) {
                        float f = (float) pLivingEntity.tickCount + pPartialTicks;
                        HumanoidModel<Player> entitymodel = this.model();
                        VertexConsumer vertexconsumer = pBuffer.getBuffer(
                                RenderType.energySwirl(TAILWIND_TEXTURE, f * 0.02F % 1.0F, 0.0F)
                        );
                        this.getParentModel().copyPropertiesTo(entitymodel);
                        entitymodel.renderToBuffer(pMatrixStack, vertexconsumer, pPackedLight,
                                OverlayTexture.NO_OVERLAY, 0x559d9d9d);
                    }
                }
            });
        }
    }
}