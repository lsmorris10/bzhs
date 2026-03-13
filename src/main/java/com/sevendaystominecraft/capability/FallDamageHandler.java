package com.sevendaystominecraft.capability;

import com.sevendaystominecraft.SevenDaysToMinecraft;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;

@EventBusSubscriber(modid = SevenDaysToMinecraft.MOD_ID)
public class FallDamageHandler {

    private static final float SPRAIN_MIN_BLOCKS = 4.0f;
    private static final float FRACTURE_MIN_BLOCKS = 8.0f;
    private static final int SPRAIN_DURATION = 36000;
    private static final int FRACTURE_DURATION = 72000;

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide()) return;
        if (!player.hasData(ModAttachments.PLAYER_STATS.get())) return;

        float distance = event.getDistance();
        if (distance < SPRAIN_MIN_BLOCKS) return;

        SevenDaysPlayerStats stats = player.getData(ModAttachments.PLAYER_STATS.get());

        if (distance >= FRACTURE_MIN_BLOCKS) {
            stats.removeDebuff(SevenDaysPlayerStats.DEBUFF_SPRAIN);
            stats.addDebuff(SevenDaysPlayerStats.DEBUFF_FRACTURE, FRACTURE_DURATION);
        } else {
            if (!stats.hasDebuff(SevenDaysPlayerStats.DEBUFF_FRACTURE)) {
                stats.addDebuff(SevenDaysPlayerStats.DEBUFF_SPRAIN, SPRAIN_DURATION);
            }
        }
    }
}
