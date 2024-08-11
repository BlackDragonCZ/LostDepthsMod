package cz.blackdragoncz.lostdepths.recipe;

import com.google.gson.JsonObject;
import cz.blackdragoncz.lostdepths.init.LostDepthsModRecipeType;
import cz.blackdragoncz.lostdepths.init.LostdepthsModRecipeSerializers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemUseRecipe extends LDRecipe<RecipeWrapper> {

    private ItemStack useItem;
    private ItemStack useOnItem;
    private ItemStack result;
    private String useDescription;

    public ItemUseRecipe(ResourceLocation id, ItemStack useItem, ItemStack useOnItem, ItemStack result, String useDescription) {
        super(id);
        this.useItem = useItem;
        this.useOnItem = useOnItem;
        this.result = result;
        this.useDescription = useDescription;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return LostdepthsModRecipeSerializers.ITEM_USE.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return LostDepthsModRecipeType.ITEM_USE.get();
    }

    @Override
    public boolean isIncomplete() {
        return false;
    }

    public ItemStack getUseItem() {
        return useItem;
    }

    public ItemStack getUseOnItem() {
        return useOnItem;
    }

    public ItemStack getResult() {
        return result;
    }

    public String getUseDescription() {
        return useDescription;
    }

    public static class Serializer implements RecipeSerializer<ItemUseRecipe> {

        @Override
        public ItemUseRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack useItem = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "use_item"));
            ItemStack useOnItem = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "use_on"));
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));

            return new ItemUseRecipe(id, useItem, useOnItem, result, GsonHelper.getAsString(json, "use_description"));
        }

        @Override
        public @Nullable ItemUseRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            return new ItemUseRecipe(id, buf.readItem(), buf.readItem(), buf.readItem(), buf.readUtf());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ItemUseRecipe recipe) {
            buf.writeItem(recipe.useItem);
            buf.writeItem(recipe.useOnItem);
            buf.writeItem(recipe.result);
            buf.writeUtf(recipe.useDescription);
        }
    }
}
