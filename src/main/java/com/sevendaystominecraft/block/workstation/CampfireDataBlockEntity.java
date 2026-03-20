package com.sevendaystominecraft.block.workstation;

import com.sevendaystominecraft.block.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class CampfireDataBlockEntity extends WorkstationBlockEntity {

    private final CampfireWorkstationSavedData.CampfireData campfireData;

    public CampfireDataBlockEntity(BlockPos pos, BlockState state, CampfireWorkstationSavedData.CampfireData data) {
        super(pos, state, WorkstationType.CAMPFIRE);
        this.campfireData = data;
    }

    @Override
    public WorkstationType getWorkstationType() {
        return WorkstationType.CAMPFIRE;
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return campfireData.getItems();
    }

    @Override
    public int getContainerSize() {
        return campfireData.getContainerSize();
    }

    @Override
    public ItemStack getItem(int slot) {
        return campfireData.getItem(slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        campfireData.setItem(slot, stack);
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        return campfireData.removeItem(slot, count);
    }

    @Override
    public void setChanged() {
        campfireData.markDirty();
    }

    @Override
    public boolean stillValid(Player player) {
        return player.distanceToSqr(getBlockPos().getX() + 0.5,
                getBlockPos().getY() + 0.5, getBlockPos().getZ() + 0.5) <= 64.0;
    }

    @Override
    public int getBurnTime() { return campfireData.getBurnTime(); }

    @Override
    public int getBurnTimeTotal() { return campfireData.getBurnTimeTotal(); }

    @Override
    public int getCraftProgress() { return campfireData.getCraftProgress(); }

    @Override
    public int getCraftTimeTotal() { return campfireData.getCraftTimeTotal(); }
}
