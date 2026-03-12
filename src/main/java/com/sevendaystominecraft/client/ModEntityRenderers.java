package com.sevendaystominecraft.client;

import com.sevendaystominecraft.SevenDaysToMinecraft;
import com.sevendaystominecraft.entity.ModEntities;

import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = SevenDaysToMinecraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEntityRenderers {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.WALKER.get(), ZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.CRAWLER.get(), ZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.FROZEN_LUMBERJACK.get(), ZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.BLOATED_WALKER.get(), ZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.SPIDER_ZOMBIE.get(), ZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.FERAL_WIGHT.get(), ZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.COP.get(), ZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.SCREAMER.get(), ZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.DEMOLISHER.get(), ctx -> new ScaledZombieRenderer(ctx, 1.2f));
        event.registerEntityRenderer(ModEntities.MUTATED_CHUCK.get(), ZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.NURSE.get(), ZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.SOLDIER.get(), ZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.CHARGED.get(), ZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.INFERNAL.get(), ZombieRenderer::new);

        event.registerEntityRenderer(ModEntities.BEHEMOTH.get(), ctx -> new ScaledZombieRenderer(ctx, 1.8f));
        event.registerEntityRenderer(ModEntities.ZOMBIE_DOG.get(), ctx -> new ScaledZombieRenderer(ctx, 0.5f));
        event.registerEntityRenderer(ModEntities.VULTURE.get(), ctx -> new ScaledZombieRenderer(ctx, 0.4f));
        event.registerEntityRenderer(ModEntities.ZOMBIE_BEAR.get(), ctx -> new ScaledZombieRenderer(ctx, 1.5f));
    }
}
