package cz.blackdragoncz.lostdepths.client.jei;

import cz.blackdragoncz.lostdepths.recipe.ItemUseRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Own JEI type for the entity half of {@code lostdepths:item_use}. RecipeTypeWrapper takes its id
 * and title from the vanilla recipe type, which would collide with the item category.
 */
public record EntityUseRecipeType(ResourceLocation id, Component title, Supplier<ItemStack> iconSupplier, int xOffset, int yOffset, int width,
        int height) implements IRecipeViewerRecipeType<ItemUseRecipe> {

    @Override
    public Class<? extends ItemUseRecipe> recipeClass() {
        return ItemUseRecipe.class;
    }

    @Override
    public boolean requiresHolder() {
        return false;
    }

    @Override
    public ItemStack iconStack() {
        return iconSupplier.get();
    }

    @Nullable
    @Override
    public ResourceLocation icon() {
        return null;
    }

    @Override
    public Component getTextComponent() {
        return title;
    }
}
