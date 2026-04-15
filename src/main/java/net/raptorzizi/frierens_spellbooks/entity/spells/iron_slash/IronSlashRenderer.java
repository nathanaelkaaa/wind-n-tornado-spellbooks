package net.raptorzizi.frierens_spellbooks.entity.spells.iron_slash;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.raptorzizi.frierens_spellbooks.FrierensSpellbooksMod;

public class IronSlashRenderer extends EntityRenderer<IronSlashEntity> {

    public IronSlashRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public boolean shouldRender(IronSlashEntity entity, Frustum camera, double camX, double camY, double camZ) {return false;}

    @Override
    public void render(IronSlashEntity entity, float yaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource bufferSource, int light) {}

    @Override
    public ResourceLocation getTextureLocation(IronSlashEntity entity) {
        return FrierensSpellbooksMod.id("textures/entity/iron_slash/slash.png");
    }
}