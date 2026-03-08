package cz.blackdragoncz.lostdepths.recipe;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class FusionTableRecipe extends LDCraftingRecipe {

    private final ItemStack input1;
    private final ItemStack input2;
    private final ItemStack output;

    public FusionTableRecipe(ResourceLocation id, ItemStack input1, ItemStack input2, ItemStack output) {
        super(id);
        this.input1 = input1;
        this.input2 = input2;
        this.output = output;
    }

    public ItemStack getInput1() {
        return input1;
    }

    public ItemStack getInput2() {
        return input2;
    }

    public ItemStack getOutput() {
        return output;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return output;
    }

    @Override
    public boolean isIncomplete() {
        return false;
    }
}
