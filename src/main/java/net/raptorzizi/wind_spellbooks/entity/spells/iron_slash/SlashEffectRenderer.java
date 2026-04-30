package net.raptorzizi.wind_spellbooks.entity.spells.iron_slash;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.raptorzizi.wind_spellbooks.WindSpellbooksMod;
import org.joml.Matrix4f;

public class SlashEffectRenderer extends EntityRenderer<SlashEffectEntity> {

    private static final ResourceLocation SLASH_TEXTURE =
            WindSpellbooksMod.id("textures/entity/iron_slash/slash.png");
    private static final ResourceLocation SLASH_ICE_TEXTURE =
            WindSpellbooksMod.id("textures/entity/iron_slash/slash_ice.png");

    public SlashEffectRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public boolean shouldRender(SlashEffectEntity entity, net.minecraft.client.renderer.culling.Frustum camera, double camX, double camY, double camZ) {return true;}


    @Override
    public void render(SlashEffectEntity entity, float yaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int light) {
        ResourceLocation texture = entity.isIceMode() ? SLASH_ICE_TEXTURE : SLASH_TEXTURE;

        float currentTick = entity.tickCount + partialTicks;
        float lifetime = SlashEffectEntity.LIFETIME;

        float progress = currentTick / lifetime;
        float alpha = progress < 0.3f ? progress / 0.3f : (1.0f - progress) / 0.7f;
        alpha = Mth.clamp(alpha, 0.0f, 1.0f);
        if (alpha < 0.01f) return;

        int alphaInt = (int)(alpha * 255);

        float scale = Mth.clampedLerp(0.3f, 1.5f, Math.min(progress / 0.3f, 1.0f));

        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(-entity.getSlashYRot()));

        float baseRotation = 45f;
        float randomOffset = entity.getRandomRotOffset();
        poseStack.mulPose(Axis.ZP.rotationDegrees(baseRotation + randomOffset));

        poseStack.scale(scale, scale, scale);

        VertexConsumer consumer = buffer.getBuffer(
                RenderType.entityTranslucentEmissive(texture));

        PoseStack.Pose pose = poseStack.last();
        Matrix4f mat = pose.pose();

        float s = 1f;

        consumer.vertex(mat, -s, -s, 0f).color(255, 255, 255, alphaInt)
                .uv(0f, 1f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880)
                .normal(pose.normal(), 0f, 0f, 1f).endVertex();
        consumer.vertex(mat, -s, s, 0f).color(255, 255, 255, alphaInt)
                .uv(0f, 0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880)
                .normal(pose.normal(), 0f, 0f, 1f).endVertex();
        consumer.vertex(mat, s, s, 0f).color(255, 255, 255, alphaInt)
                .uv(1f, 0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880)
                .normal(pose.normal(), 0f, 0f, 1f).endVertex();
        consumer.vertex(mat, s, -s, 0f).color(255, 255, 255, alphaInt)
                .uv(1f, 1f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880)
                .normal(pose.normal(), 0f, 0f, 1f).endVertex();

        poseStack.popPose();

        super.render(entity, yaw, partialTicks, poseStack, buffer, light);
    }

    @Override
    public ResourceLocation getTextureLocation(SlashEffectEntity entity) {return entity.isIceMode() ? SLASH_ICE_TEXTURE : SLASH_TEXTURE;}
}
