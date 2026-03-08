package cz.blackdragoncz.lostdepths.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.Nullable;

public class ShipmentFillerRecipeSerializer implements RecipeSerializer<ShipmentFillerRecipe> {

    private final IFactory factory;

    public ShipmentFillerRecipeSerializer(IFactory factory) {
        this.factory = factory;
    }

    @Override
    public ShipmentFillerRecipe fromJson(ResourceLocation id, JsonObject json) {
        ItemStack item = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "item"));
        int weight = GsonHelper.getAsInt(json, "weight");
        return factory.create(id, item, weight);
    }

    @Override
    public @Nullable ShipmentFillerRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        return factory.create(id, buf.readItem(), buf.readInt());
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, ShipmentFillerRecipe recipe) {
        buffer.writeItem(recipe.getItem());
        buffer.writeInt(recipe.getWeight());
    }

    @FunctionalInterface
    public interface IFactory {
        ShipmentFillerRecipe create(ResourceLocation id, ItemStack item, int weight);
    }
}
