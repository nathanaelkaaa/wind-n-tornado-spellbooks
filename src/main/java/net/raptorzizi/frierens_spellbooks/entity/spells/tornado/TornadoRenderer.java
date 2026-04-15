package net.raptorzizi.frierens_spellbooks.entity.spells.tornado;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.raptorzizi.frierens_spellbooks.FrierensSpellbooksMod;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TornadoRenderer extends GeoEntityRenderer<TornadoEntity> {

    private static final ResourceLocation WIND_TEXTURE =
            FrierensSpellbooksMod.id("textures/entity/tornado/tornado.png");
    private static final ResourceLocation WIND_FIRE_TEXTURE =
            FrierensSpellbooksMod.id("textures/entity/tornado/tornado_fire.png");

    public static final int FIRE_TRANSITION_DURATION = 40;

    private float currentAlphaOverride = 1f;
    private ResourceLocation currentTextureOverride = null;

    public TornadoRenderer(EntityRendererProvider.Context context) {
        super(context, new TornadoModel());
    }

    private float xOffset(float tickCount) {
        return tickCount * 0.02f;
    }

    @Override
    public void render(TornadoEntity entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        boolean onFire = entity.isTornadoOnFire();
        int transitionTick = entity.fireTransitionTick;

        if (transitionTick < FIRE_TRANSITION_DURATION) {
            entity.fireTransitionTick++;
        }

        boolean inTransition = onFire && transitionTick < FIRE_TRANSITION_DURATION;

        if (!inTransition) {
            super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
            return;
        }

        float progress = (transitionTick + partialTick) / FIRE_TRANSITION_DURATION;
        progress = Mth.clamp(progress, 0f, 1f);
        progress = progress * progress * (3f - 2f * progress);

        currentAlphaOverride = 1f - progress;
        currentTextureOverride = WIND_TEXTURE;
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        currentAlphaOverride = progress;
        currentTextureOverride = WIND_FIRE_TEXTURE;
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        currentAlphaOverride = 1f;
        currentTextureOverride = null;
    }

    @Override
    public void preRender(PoseStack poseStack, TornadoEntity entity, BakedGeoModel model,
                          MultiBufferSource bufferSource, VertexConsumer buffer,
                          boolean isReRender, float partialTick, int packedLight,
                          int packedOverlay, int color) {

        float f = entity.tickCount + partialTick;
        float radius = entity.getRadius();
        float fullScale = radius * 0.6f;

        float spawnProgress = Math.min(f / TornadoEntity.SPAWN_ANIM_DURATION, 1.0f);
        spawnProgress = spawnProgress * spawnProgress * (3f - 2f * spawnProgress);

        int ticksRemaining = entity.getSyncedDuration() - entity.tickCount;
        float deathProgressXZ = 1.0f;

        if (ticksRemaining <= TornadoEntity.DEATH_ANIM_DURATION) {
            deathProgressXZ = Math.max(0f, (float) ticksRemaining / TornadoEntity.DEATH_ANIM_DURATION);
            deathProgressXZ = deathProgressXZ * deathProgressXZ;
        }

        float scaleXZ = fullScale * spawnProgress * deathProgressXZ;
        float scaleY  = fullScale * spawnProgress;

        poseStack.scale(scaleXZ, scaleY, scaleXZ);
        poseStack.mulPose(Axis.YP.rotationDegrees(f * 3f));
    }

    @Override
    public RenderType getRenderType(TornadoEntity entity, ResourceLocation texture,
                                    MultiBufferSource bufferSource, float partialTick) {
        float f = entity.tickCount + partialTick;
        ResourceLocation tex = currentTextureOverride != null
                ? currentTextureOverride
                : (entity.isTornadoOnFire() ? WIND_FIRE_TEXTURE : WIND_TEXTURE);
        return RenderType.breezeWind(tex, xOffset(f) % 1.0f, 0.0f);
    }

    @Override
    public ResourceLocation getTextureLocation(TornadoEntity entity) {
        return WIND_TEXTURE;
    }
}