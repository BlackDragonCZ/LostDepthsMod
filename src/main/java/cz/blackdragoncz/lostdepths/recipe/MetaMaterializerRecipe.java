package cz.blackdragoncz.lostdepths.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import cz.blackdragoncz.lostdepths.init.LostDepthsModRecipeType;
import cz.blackdragoncz.lostdepths.init.LostdepthsModRecipeSerializers;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MetaMaterializerRecipe extends LDRecipe<RecipeWrapper> {

    private NonNullList<ItemStack> items;
    private ItemStack heldItem;
    private ItemStack result;

    public MetaMaterializerRecipe(ResourceLocation id, NonNullList<ItemStack> items, ItemStack heldItem, ItemStack result) {
        super(id);
        this.items = items;
        this.heldItem = heldItem;
        this.result = result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return LostdepthsModRecipeSerializers.META_MATERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return LostDepthsModRecipeType.META_MATERIALIZER.get();
    }

    @Override
    public boolean matches(@NotNull RecipeWrapper inv, @NotNull Level world) {

        return super.matches(inv, world);
    }

    @Override
    public boolean isIncomplete() {
        return false;
    }

    public NonNullList<ItemStack> getItems() {
        return items;
    }

    public ItemStack getHeldItem() {
        return heldItem;
    }

    public ItemStack getResult() {
        return result;
    }

    public static class Serializer implements RecipeSerializer<MetaMaterializerRecipe> {

        @Override
        public MetaMaterializerRecipe fromJson(ResourceLocation id, JsonObject json) {
            JsonArray items = json.getAsJsonArray("items");
            NonNullList<ItemStack> ingredients = NonNullList.withSize(3, ItemStack.EMPTY);

            for (int i = 0; i < 3; i++) {
                ingredients.set(i, LDShapedRecipe.itemStackFromJson(items.get(i).getAsJsonObject()));
            }


            ItemStack heldItem = LDShapedRecipe.itemStackFromJson(json.get("held_item").getAsJsonObject());
            ItemStack result = LDShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));

            return new MetaMaterializerRecipe(id, ingredients, heldItem, result);
        }

        @Override
        public @Nullable MetaMaterializerRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<ItemStack> ingredients = NonNullList.withSize(3, ItemStack.EMPTY);
            ingredients.replaceAll(ignored -> buf.readItem());
            ItemStack heldItem = buf.readItem();
            ItemStack result = buf.readItem();

            return new MetaMaterializerRecipe(id, ingredients, heldItem, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, MetaMaterializerRecipe recipe) {
            for (ItemStack ingredient : recipe.items) {
                buf.writeItem(ingredient);
            }

            buf.writeItem(recipe.heldItem);
            buf.writeItem(recipe.result);
        }
    }
}
