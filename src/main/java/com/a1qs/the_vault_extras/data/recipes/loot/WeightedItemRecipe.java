package com.a1qs.the_vault_extras.data.recipes.loot;

import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Map;

public class WeightedItemRecipe {
    private final List<ItemStack> outputs;
    private final Map<ItemStack, Integer> weights;
    private final ItemStack displayStack;

    public WeightedItemRecipe(List<ItemStack> outputs, Map<ItemStack, Integer> weights, ItemStack displayStack) {
        this.outputs = outputs;
        this.weights = weights;
        this.displayStack = displayStack;
    }

    public List<ItemStack> getOutputs() {
        return outputs;
    }

    public ItemStack getDisplayStack() {
        return displayStack;
    }

    public int getTotalWeight() {
        int totalWeight = 0;
        for (Map.Entry<ItemStack, Integer> entry : weights.entrySet()) {
            totalWeight += entry.getValue();
        }
        return totalWeight;
    }

    public double getPercentageChance(ItemStack output) {

        double outputWeight = weights.get(output);
        double totalWeight = getTotalWeight();

        //return String.format(" %.2f%%", percentage);
        return Math.round(((outputWeight / totalWeight) * 100.0)*100.0)/100.0;
    }
}
