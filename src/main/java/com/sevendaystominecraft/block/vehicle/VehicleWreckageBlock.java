package com.sevendaystominecraft.block.vehicle;

import com.mojang.serialization.MapCodec;
import com.sevendaystominecraft.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VehicleWreckageBlock extends Block {

    public static final MapCodec<VehicleWreckageBlock> CODEC = simpleCodec(VehicleWreckageBlock::new);
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 12, 16);

    public VehicleWreckageBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = new ArrayList<>();
        Random random = new Random();
        drops.add(new ItemStack(ModItems.IRON_SCRAP.get(), 2 + random.nextInt(4)));
        drops.add(new ItemStack(ModItems.MECHANICAL_PARTS.get(), 1 + random.nextInt(2)));
        if (random.nextFloat() < 0.3f) {
            drops.add(new ItemStack(ModItems.GAS_CAN.get(), 1));
        }
        if (random.nextFloat() < 0.2f) {
            drops.add(new ItemStack(ModItems.SPRING.get(), 1 + random.nextInt(2)));
        }
        return drops;
    }
}
