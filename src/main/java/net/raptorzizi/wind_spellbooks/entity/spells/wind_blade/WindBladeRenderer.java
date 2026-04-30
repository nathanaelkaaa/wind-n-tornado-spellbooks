package net.raptorzizi.wind_spellbooks.entity.spells.wind_blade;

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
import org.joml.Matrix4f;

//CHEKK
public class WindBladeRenderer extends EntityRenderer<WindBladeProjectile> {

    private static final ResourceLocation[] TEXTURES = {
            new ResourceLocation("textures/particle/sweep_0.png"),
            new ResourceLocation("textures/particle/sweep_1.png"),
            new ResourceLocation("textures/particle/sweep_2.png"),
            new ResourceLocation("textures/particle/sweep_3.png"),
            new ResourceLocation("textures/particle/sweep_4.png"),
            new ResourceLocation("textures/particle/sweep_5.png"),
            new ResourceLocation("textures/particle/sweep_6.png"),
            new ResourceLocation("textures/particle/sweep_7.png")
    };

    public WindBladeRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(WindBladeProjectile entity, float yaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();
        PoseStack.Pose pose = poseStack.last();

        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot())));
        poseStack.mulPose(Axis.XP.rotationDegrees(-Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));

        ++entity.animationTime;

        poseStack.mulPose(Axis.ZP.rotationDegrees(
                (float)(entity.animationSeed % 30 - 15) * (float) Math.sin(entity.animationTime * 0.015)));

        float offsetX = ((entity.animationSeed % 7) - 3) * 0.04f;
        float offsetY = ((entity.animationSeed % 5) - 2) * 0.04f;
        poseStack.translate(offsetX, offsetY, 0);

        float oldWidth = (float) entity.oldBB.getXsize();
        float width = oldWidth + (entity.getBbWidth() - oldWidth) * Math.min(partialTicks, 1f);

        drawSlash(pose, entity, bufferSource, light, width * 1f, 0);

        poseStack.popPose();
        super.render(entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    private void drawSlash(PoseStack.Pose pose, WindBladeProjectile entity,
                           MultiBufferSource bufferSource, int light, float width, int offset) {
        Matrix4f poseMatrix = pose.pose();
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(entity, offset)));
        float h = width * 0.5f;

        consumer.vertex(poseMatrix, -h, -0.1f, -h).color(200, 240, 255, 220)
                .uv(0f, 1f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                .normal(pose.normal(), 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix,  h, -0.1f, -h).color(200, 240, 255, 220)
                .uv(1f, 1f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                .normal(pose.normal(), 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix,  h, -0.1f,  h).color(200, 240, 255, 220)
                .uv(1f, 0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                .normal(pose.normal(), 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, -h, -0.1f,  h).color(200, 240, 255, 220)
                .uv(0f, 0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                .normal(pose.normal(), 0f, 1f, 0f).endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(WindBladeProjectile entity) {
        return TEXTURES[entity.animationTime / 4 % TEXTURES.length];
    }

    private ResourceLocation getTextureLocation(WindBladeProjectile entity, int offset) {
        return TEXTURES[(entity.animationTime / 6 + offset) % TEXTURES.length];
    }
}