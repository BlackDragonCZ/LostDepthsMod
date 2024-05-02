package cz.blackdragoncz.lostdepths.recipe;

import com.google.gson.JsonObject;
import cz.blackdragoncz.lostdepths.init.LostDepthsModRecipeType;
import cz.blackdragoncz.lostdepths.init.LostdepthsModRecipeSerializers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemUseRecipe extends LDRecipe<RecipeWrapper> {

    private ItemStack requiredItemInHand;
    private ItemStack useOnItem;

    public ItemUseRecipe(ResourceLocation id) {
        super(id);
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

    public static class Serializer implements RecipeSerializer<ItemUseRecipe> {

        @Override
        public ItemUseRecipe fromJson(ResourceLocation id, JsonObject json) {

            return new ItemUseRecipe(id);
        }

        @Override
        public @Nullable ItemUseRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            return new ItemUseRecipe(id);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ItemUseRecipe recipe) {

        }
    }
}
