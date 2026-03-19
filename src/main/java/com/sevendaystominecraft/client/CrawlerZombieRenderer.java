package com.sevendaystominecraft.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;

public class CrawlerZombieRenderer extends ScaledZombieRenderer {

    public CrawlerZombieRenderer(EntityRendererProvider.Context context, float scale, float nameTagExtraHeight) {
        super(context, scale, nameTagExtraHeight);
    }

    @Override
    protected void scale(ZombieRenderState state, PoseStack poseStack) {
        super.scale(state, poseStack);
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.translate(0.0F, -0.3F, 0.6F);
    }
}
