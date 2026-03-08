package cz.blackdragoncz.lostdepths.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.Nullable;

public class FusionTableRecipeSerializer implements RecipeSerializer<FusionTableRecipe> {

    private final IFactory factory;

    public FusionTableRecipeSerializer(IFactory factory) {
        this.factory = factory;
    }

    @Override
    public FusionTableRecipe fromJson(ResourceLocation id, JsonObject json) {
        ItemStack input1 = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "input1"));
        ItemStack input2 = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "input2"));
        ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
        return factory.create(id, input1, input2, output);
    }

    @Override
    public @Nullable FusionTableRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        return factory.create(id, buf.readItem(), buf.readItem(), buf.readItem());
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, FusionTableRecipe recipe) {
        buffer.writeItem(recipe.getInput1());
        buffer.writeItem(recipe.getInput2());
        buffer.writeItem(recipe.getOutput());
    }

    @FunctionalInterface
    public interface IFactory {
        FusionTableRecipe create(ResourceLocation id, ItemStack input1, ItemStack input2, ItemStack output);
    }
}
