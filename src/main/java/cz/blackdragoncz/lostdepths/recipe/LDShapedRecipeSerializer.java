package cz.blackdragoncz.lostdepths.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class LDShapedRecipeSerializer implements RecipeSerializer<LDShapedRecipe> {

    private final IFactory<LDShapedRecipe> factory;

    public LDShapedRecipeSerializer(IFactory<LDShapedRecipe> factory) {
        this.factory = factory;
    }

    @Override
    public LDShapedRecipe fromJson(@NotNull ResourceLocation pRecipeId, @NotNull JsonObject pJson) {
        Map<String, Ingredient> map = LDShapedRecipe.keyFromJson(GsonHelper.getAsJsonObject(pJson, "key"));
        String[] astring = LDShapedRecipe.shrink(LDShapedRecipe.patternFromJson(GsonHelper.getAsJsonArray(pJson, "pattern")));
        int i = astring[0].length();
        int j = astring.length;
        NonNullList<Ingredient> nonnulllist = LDShapedRecipe.dissolvePattern(astring, map, i, j);
        ItemStack itemstack = LDShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pJson, "result"));
        return this.factory.create(pRecipeId, i, j, nonnulllist, itemstack);
    }

    @Override
    public @Nullable LDShapedRecipe fromNetwork(@NotNull ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
        int i = pBuffer.readVarInt();
        int j = pBuffer.readVarInt();
        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);

        nonnulllist.replaceAll(ignored -> Ingredient.fromNetwork(pBuffer));

        ItemStack itemstack = pBuffer.readItem();
        return this.factory.create(pRecipeId, i, j, nonnulllist, itemstack);
    }

    @Override
    public void toNetwork(FriendlyByteBuf pBuffer, LDShapedRecipe pRecipe) {
        pBuffer.writeVarInt(pRecipe.width);
        pBuffer.writeVarInt(pRecipe.height);

        for(Ingredient ingredient : pRecipe.recipeItems) {
            ingredient.toNetwork(pBuffer);
        }

        pBuffer.writeItem(pRecipe.result);
    }

    @FunctionalInterface
    public interface IFactory<RECIPE extends LDRecipe> {
        RECIPE create(ResourceLocation id, int width, int height, NonNullList<Ingredient> recipeItems, ItemStack result);
    }
}
