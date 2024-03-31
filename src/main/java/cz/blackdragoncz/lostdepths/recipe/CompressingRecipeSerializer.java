package cz.blackdragoncz.lostdepths.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.Nullable;

public class CompressingRecipeSerializer implements RecipeSerializer<CompressingRecipe> {

    private final IFactory<CompressingRecipe> factory;

    public CompressingRecipeSerializer(IFactory<CompressingRecipe> factory) {
        this.factory = factory;
    }

    @Override
    public CompressingRecipe fromJson(ResourceLocation id, JsonObject json) {
        ItemStack input = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "input"));
        ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));

        return factory.create(id, input, output);
    }

    @Override
    public @Nullable CompressingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        return factory.create(id, buf.readItem(), buf.readItem());
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, CompressingRecipe recipe) {
        buffer.writeItem(recipe.getInput());
        buffer.writeItem(recipe.getOutput());
    }

    @FunctionalInterface
    public interface IFactory<RECIPE extends LDRecipe> {
        RECIPE create(ResourceLocation id, ItemStack input, ItemStack output);
    }
}
