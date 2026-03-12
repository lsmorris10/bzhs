package com.sevendaystominecraft.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;

public class ScaledZombieRenderer extends ZombieRenderer {
    private final float scale;

    public ScaledZombieRenderer(EntityRendererProvider.Context context, float scale) {
        super(context);
        this.scale = scale;
    }

    @Override
    protected void scale(ZombieRenderState state, PoseStack poseStack) {
        poseStack.scale(scale, scale, scale);
        super.scale(state, poseStack);
    }
}
