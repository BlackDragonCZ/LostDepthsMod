package cz.blackdragoncz.lostdepths.client.jei;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public record ChestLootRecipe(String chestName, ItemStack chestIcon, int rollsMin, int rollsMax, List<LootEntry> entries) {

    public record LootEntry(ItemStack item, int weight, int minCount, int maxCount) {
    }

    public int totalWeight() {
        return entries.stream().mapToInt(LootEntry::weight).sum();
    }
}
