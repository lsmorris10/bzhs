package com.sevendaystominecraft.block.workstation;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record WorkstationRecipe(
        List<Ingredient> inputs,
        ItemStack output,
        int processingTicks
) {
    public record Ingredient(Item item, int count) {}

    public boolean matches(java.util.function.Function<Item, Integer> itemCounter) {
        for (Ingredient ing : inputs) {
            if (itemCounter.apply(ing.item()) < ing.count()) return false;
        }
        return true;
    }

    public void consumeInputs(java.util.function.BiConsumer<Item, Integer> itemConsumer) {
        for (Ingredient ing : inputs) {
            itemConsumer.accept(ing.item(), ing.count());
        }
    }
}
