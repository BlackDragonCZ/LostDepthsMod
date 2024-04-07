package cz.blackdragoncz.lostdepths.recipe;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class CompressingRecipe extends LDRecipe {

    private final ItemStack input;
    private final ItemStack output;

    public CompressingRecipe(ResourceLocation id, ItemStack input, ItemStack output) {
        super(id);
        this.input = input;
        this.output = output;
    }

    public ItemStack getInput() {
        return input;
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
