package com.sevendaystominecraft.territory;

import com.sevendaystominecraft.SevenDaysToMinecraft;
import com.sevendaystominecraft.block.loot.LootContainerBlock;
import com.sevendaystominecraft.block.loot.LootContainerBlockEntity;
import com.sevendaystominecraft.block.loot.LootContainerType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NBTTemplateLoader {

    private static final Map<VillageBuildingType, List<ResourceLocation>> TEMPLATE_CACHE = new HashMap<>();
    private static boolean initialized = false;

    public static void init(ServerLevel level) {
        if (initialized) return;
        initialized = true;
        TEMPLATE_CACHE.clear();

        StructureTemplateManager templateManager = level.getStructureManager();

        for (VillageBuildingType type : VillageBuildingType.values()) {
            List<ResourceLocation> templates = new ArrayList<>();
            String baseName = type.name().toLowerCase();

            for (int i = 1; i <= 10; i++) {
                ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(
                        "bzhs", "village/" + baseName + "_" + i);
                Optional<StructureTemplate> template = templateManager.get(loc);
                if (template.isPresent()) {
                    templates.add(loc);
                    SevenDaysToMinecraft.LOGGER.info("[BZHS Village] Found NBT template: {}", loc);
                }
            }

            if (!templates.isEmpty()) {
                TEMPLATE_CACHE.put(type, templates);
                SevenDaysToMinecraft.LOGGER.info("[BZHS Village] Loaded {} template(s) for building type {}",
                        templates.size(), type.name());
            }
        }

        SevenDaysToMinecraft.LOGGER.info("[BZHS Village] NBT template scan complete. {} building types have templates.",
                TEMPLATE_CACHE.size());
    }

    public static boolean hasTemplate(VillageBuildingType type) {
        return TEMPLATE_CACHE.containsKey(type) && !TEMPLATE_CACHE.get(type).isEmpty();
    }

    public static VillageBuildingBuilder.BuildingResult placeTemplate(ServerLevel level, BlockPos origin,
                                                                       VillageBuildingType buildingType,
                                                                       TerritoryTier tier, RandomSource random) {
        if (!hasTemplate(buildingType)) return null;

        List<ResourceLocation> templates = TEMPLATE_CACHE.get(buildingType);
        ResourceLocation chosen = templates.get(random.nextInt(templates.size()));

        try {
            StructureTemplateManager templateManager = level.getStructureManager();
            Optional<StructureTemplate> templateOpt = templateManager.get(chosen);
            if (templateOpt.isEmpty()) return null;

            StructureTemplate template = templateOpt.get();

            int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, origin.getX(), origin.getZ());
            BlockPos placePos = new BlockPos(origin.getX(), surfaceY, origin.getZ());

            StructurePlaceSettings settings = new StructurePlaceSettings();
            template.placeInWorld(level, placePos, placePos, settings, random, Block.UPDATE_CLIENTS);

            var size = template.getSize();
            List<BlockPos> zombieSpawns = new ArrayList<>();
            List<BlockPos> lootPositions = new ArrayList<>();
            List<LootContainerType> lootTypes = new ArrayList<>();

            for (int dx = 0; dx < size.getX(); dx++) {
                for (int dy = 0; dy < size.getY(); dy++) {
                    for (int dz = 0; dz < size.getZ(); dz++) {
                        BlockPos checkPos = placePos.offset(dx, dy, dz);
                        BlockState state = level.getBlockState(checkPos);

                        if (state.getBlock() instanceof LootContainerBlock lcb) {
                            lootPositions.add(checkPos);
                            lootTypes.add(lcb.getContainerType());
                            if (level.getBlockEntity(checkPos) instanceof LootContainerBlockEntity be) {
                                be.setTerritoryTier(tier.getTier());
                            }
                        }

                        if (dy >= 1 && dy <= 2 && state.isAir()) {
                            BlockState below = level.getBlockState(checkPos.below());
                            if (below.isSolid()) {
                                BlockState above = level.getBlockState(checkPos.above());
                                if (above.isAir()) {
                                    zombieSpawns.add(checkPos);
                                }
                            }
                        }
                    }
                }
            }

            SevenDaysToMinecraft.LOGGER.info("[BZHS Village] Placed NBT template '{}' at ({}, {}, {}) — {} loot, {} spawns",
                    chosen, placePos.getX(), placePos.getY(), placePos.getZ(),
                    lootPositions.size(), zombieSpawns.size());

            return new VillageBuildingBuilder.BuildingResult(placePos, zombieSpawns, lootPositions, lootTypes,
                    size.getX(), size.getZ());

        } catch (Exception e) {
            SevenDaysToMinecraft.LOGGER.error("[BZHS Village] Failed to load NBT template '{}': {}",
                    chosen, e.getMessage());
            return null;
        }
    }
}
