package cz.blackdragoncz.lostdepths.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ModuleRecipe extends LDCraftingRecipe {

    private final NonNullList<Ingredient> recipeItems;
    private final ItemStack result;

    public ModuleRecipe(ResourceLocation id, NonNullList<Ingredient> recipeItems, ItemStack result) {
        super(id);
        this.recipeItems = recipeItems;
        this.result = result;
    }

    @Override
    public boolean isIncomplete() {
        NonNullList<Ingredient> ingredients = this.getIngredients();
        return ingredients.isEmpty() || ingredients.stream()
                .filter((ingredient) -> !ingredient.isEmpty())
                .anyMatch(ForgeHooks::hasNoElements);
    }

    @Override
    public boolean matches(@NotNull CraftingContainer inv, @NotNull Level world) {
        for (int i = 0; i < recipeItems.size(); i++) {
            Ingredient ingredient = this.recipeItems.get(i);

            if (!ingredient.test(inv.getItem(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingContainer inv, @NotNull RegistryAccess registryAccess) {
        return getResultItem(registryAccess).copy();
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return result;
    }

    public @NotNull NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    public ItemStack getResult() {
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return super.canCraftInDimensions(width, height);
    }

    public static class Serializer implements RecipeSerializer<ModuleRecipe> {

        private final IFactory<ModuleRecipe> factory;

        public Serializer(IFactory<ModuleRecipe> factory) {
            this.factory = factory;
        }

        @Override
        public @NotNull ModuleRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
            JsonArray items = json.getAsJsonArray("items");

            NonNullList<Ingredient> ingredients = NonNullList.withSize(4, Ingredient.EMPTY);

            for (int i = 0; i < items.size(); i++) {
                ingredients.set(i, Ingredient.fromJson(items.get(i), false));
            }

            ItemStack itemstack = LDShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));

            return this.factory.create(recipeId, ingredients, itemstack);
        }

        @Override
        public @Nullable ModuleRecipe fromNetwork(@NotNull ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            NonNullList<Ingredient> ingredients = NonNullList.withSize(4, Ingredient.EMPTY);

            ingredients.replaceAll(ignored -> Ingredient.fromNetwork(pBuffer));
            ItemStack itemstack = pBuffer.readItem();

            return this.factory.create(pRecipeId, ingredients, itemstack);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf pBuffer, ModuleRecipe pRecipe) {
            for(Ingredient ingredient : pRecipe.recipeItems) {
                ingredient.toNetwork(pBuffer);
            }

            pBuffer.writeItem(pRecipe.result);
        }

        @FunctionalInterface
        public interface IFactory<RECIPE extends LDRecipe> {
            RECIPE create(ResourceLocation id, NonNullList<Ingredient> recipeItems, ItemStack result);
        }
    }
}
