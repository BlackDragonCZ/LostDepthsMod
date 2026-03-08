package cz.blackdragoncz.lostdepths.recipe;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

public abstract class ShipmentFillerRecipe extends LDRecipe<RecipeWrapper> {

    private final ItemStack item;
    private final int weight;

    public ShipmentFillerRecipe(ResourceLocation id, ItemStack item, int weight) {
        super(id);
        this.item = item;
        this.weight = weight;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isIncomplete() {
        return false;
    }
}
