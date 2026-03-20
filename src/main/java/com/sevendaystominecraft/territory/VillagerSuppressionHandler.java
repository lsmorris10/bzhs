package com.sevendaystominecraft.territory;

import com.sevendaystominecraft.SevenDaysToMinecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber(modid = SevenDaysToMinecraft.MOD_ID)
public class VillagerSuppressionHandler {

    private static final int VILLAGE_POI_RADIUS = 64;

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof Villager villager)) return;
        if (villager.level().isClientSide()) return;
        if (!(villager.level() instanceof ServerLevel serverLevel)) return;
        if (!serverLevel.dimension().equals(Level.OVERWORLD)) return;

        BlockPos villagerPos = villager.blockPosition();

        if (isNearVanillaVillage(serverLevel, villagerPos)) {
            event.setCanceled(true);
        }
    }

    private static boolean isNearVanillaVillage(ServerLevel level, BlockPos pos) {
        PoiManager poiManager = level.getPoiManager();
        return poiManager.findAll(
                holder -> holder.is(PoiTypes.MEETING),
                p -> true,
                pos,
                VILLAGE_POI_RADIUS,
                PoiManager.Occupancy.ANY
        ).findAny().isPresent();
    }
}
