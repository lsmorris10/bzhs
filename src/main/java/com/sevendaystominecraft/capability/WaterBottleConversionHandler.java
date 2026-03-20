package com.sevendaystominecraft.capability;

import com.sevendaystominecraft.SevenDaysToMinecraft;
import com.sevendaystominecraft.item.ModItems;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.core.component.DataComponents;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = SevenDaysToMinecraft.MOD_ID)
public class WaterBottleConversionHandler {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        Inventory inv = player.getInventory();
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (isVanillaWaterBottle(stack)) {
                inv.setItem(i, new ItemStack(ModItems.MURKY_WATER.get(), stack.getCount()));
            }
        }
    }

    private static boolean isVanillaWaterBottle(ItemStack stack) {
        if (!stack.is(Items.POTION)) return false;
        var potionContents = stack.get(DataComponents.POTION_CONTENTS);
        return potionContents != null
                && potionContents.potion().isPresent()
                && potionContents.potion().get().is(Potions.WATER);
    }
}
