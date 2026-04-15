package net.raptorzizi.wind_spellbooks.entity.spells.almighty_push;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.raptorzizi.wind_spellbooks.WindSpellbooksMod;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AlmightyPushRenderer extends GeoEntityRenderer<AlmightyPushEntity> {

    private static final ResourceLocation TEXTURE =
            WindSpellbooksMod.id("textures/entity/almighty_push/almighty_push.png");

    public AlmightyPushRenderer(EntityRendererProvider.Context context) {
        super(context, new AlmightyPushModel());
    }

    private float xOffset(float tickCount) {
        return tickCount * 0.02f;
    }

    @Override
    public void preRender(PoseStack poseStack, AlmightyPushEntity entity, BakedGeoModel model,
                          MultiBufferSource bufferSource, VertexConsumer buffer,
                          boolean isReRender, float partialTick, int packedLight,
                          int packedOverlay, int color) {

        float f = entity.tickCount + partialTick;
        float radius = entity.getRadius();
        float fullScale = radius * 1.4f;

        float spawnProgress = Math.min(f / 5f, 1.0f);
        spawnProgress = spawnProgress * spawnProgress * (3f - 2f * spawnProgress);

        float deathProgress = 1.0f;
        float ticksRemaining = entity.getLifetime() - entity.tickCount;
        if (ticksRemaining <= 8) {
            deathProgress = Math.max(0f, ticksRemaining / 8f);
            deathProgress = deathProgress * deathProgress;
        }

        float scale = fullScale * spawnProgress * deathProgress;

        poseStack.translate(0, radius, 0);
        poseStack.scale(scale, scale, scale);
        poseStack.mulPose(Axis.YP.rotationDegrees(f * 4f));
    }

    @Override
    public RenderType getRenderType(AlmightyPushEntity entity, ResourceLocation texture,
                                    MultiBufferSource bufferSource, float partialTick) {
        float f = entity.tickCount + partialTick;
        return RenderType.breezeWind(TEXTURE, xOffset(f) % 1.0f, 0.0f);
    }

    @Override
    public ResourceLocation getTextureLocation(AlmightyPushEntity entity) {
        return TEXTURE;
    }
}