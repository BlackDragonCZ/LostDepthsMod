package cz.blackdragoncz.lostdepths.recipe;

import cz.blackdragoncz.lostdepths.init.LostdepthsModRecipes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

public class CompressingRecipe extends LDRecipe {

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
    public RecipeSerializer<?> getSerializer() {
        return LostdepthsModRecipes.COMPRESSING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return LDRecipeType.COMPRESSING.get();
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
