package cz.blackdragoncz.lostdepths.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;

public abstract class LDCraftingRecipe extends LDRecipe<CraftingContainer> {
    public LDCraftingRecipe(ResourceLocation id) {
        super(id);
    }
}
