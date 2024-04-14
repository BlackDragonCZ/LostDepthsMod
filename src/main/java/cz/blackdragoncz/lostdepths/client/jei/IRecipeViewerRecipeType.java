package cz.blackdragoncz.lostdepths.client.jei;

import cz.blackdragoncz.lostdepths.util.IHasTextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public interface IRecipeViewerRecipeType<RECIPE> extends IHasTextComponent {

    ResourceLocation id();

    Class<? extends RECIPE> recipeClass();

    boolean requiresHolder();

    ItemStack iconStack();

    @Nullable
    ResourceLocation icon();

    int xOffset();

    int yOffset();

    int width();

    int height();

}
