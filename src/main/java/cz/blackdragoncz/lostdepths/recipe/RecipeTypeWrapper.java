package cz.blackdragoncz.lostdepths.recipe;

import cz.blackdragoncz.lostdepths.client.recipe_view.IRecipeViewerRecipeType;
import cz.blackdragoncz.lostdepths.init.LostDepthsModRecipeType;
import cz.blackdragoncz.lostdepths.util.IItemProvider;
import cz.blackdragoncz.lostdepths.util.NothingNullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

@NothingNullByDefault
public record RecipeTypeWrapper<RECIPE extends LDRecipe>(
        ResourceLocation id,
        IItemProvider item,
        Class<? extends RECIPE> recipeClass,
        ILDRecipeTypeProvider<RECIPE> vanillaProvider,
        int xOffset, int yOffset, int width, int height, List<IItemProvider> workstations
) implements IRecipeViewerRecipeType<RECIPE>, ILDRecipeTypeProvider<RECIPE> {

    public RecipeTypeWrapper(ILDRecipeTypeProvider<RECIPE> vanillaProvider, Class<? extends RECIPE> recipeClass, int xOffset, int yOffset,
                               int width, int height, IItemProvider icon, IItemProvider... altWorkstations) {
        this(vanillaProvider.getRegistryName(), icon, recipeClass, vanillaProvider, xOffset, yOffset, width, height, List.of(altWorkstations));
    }

    public RecipeTypeWrapper {
        if (workstations.isEmpty()) {
            workstations = List.of(item);
        } else {
            workstations = Stream.concat(Stream.of(item), workstations.stream()).toList();
        }
    }

    @Override
    public Component getTextComponent() {
        return item.getTextComponent();
    }

    @Override
    public boolean requiresHolder() {
        return true;
    }

    @Override
    public ItemStack iconStack() {
        return item.getItemStack();
    }

    @Nullable
    @Override
    public ResourceLocation icon() {
        //Handled by the icon stack
        return null;
    }

    @Override
    public LostDepthsModRecipeType<RECIPE> getRecipeType() {
        return vanillaProvider.getRecipeType();
    }
}
