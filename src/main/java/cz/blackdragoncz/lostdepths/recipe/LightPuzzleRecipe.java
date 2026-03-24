package cz.blackdragoncz.lostdepths.recipe;

import com.google.gson.JsonObject;
import cz.blackdragoncz.lostdepths.init.LostDepthsModRecipeType;
import cz.blackdragoncz.lostdepths.init.LostdepthsModRecipeSerializers;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LightPuzzleRecipe extends LDRecipe<RecipeWrapper> {

	private final ItemStack input;
	private final ItemStack output;

	public LightPuzzleRecipe(ResourceLocation id, ItemStack input, ItemStack output) {
		super(id);
		this.input = input;
		this.output = output;
	}

	public ItemStack getInput() {
		return input;
	}

	public ItemStack getOutput() {
		return output;
	}

	@Override
	public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
		return output.copy();
	}

	@Override
	public boolean isIncomplete() {
		return false;
	}

	@Override
	public @NotNull RecipeType<?> getType() {
		return LostDepthsModRecipeType.LIGHT_PUZZLE.get();
	}

	@Override
	public @NotNull RecipeSerializer<?> getSerializer() {
		return LostdepthsModRecipeSerializers.LIGHT_PUZZLE.get();
	}

	/**
	 * Check if the given item matches this recipe's input (ignoring count).
	 */
	public boolean matches(ItemStack stack) {
		return stack.getItem() == input.getItem();
	}

	public static class Serializer implements RecipeSerializer<LightPuzzleRecipe> {

		@Override
		public @NotNull LightPuzzleRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
			ItemStack input = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "input"));
			ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
			return new LightPuzzleRecipe(id, input, output);
		}

		@Override
		public @Nullable LightPuzzleRecipe fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buf) {
			return new LightPuzzleRecipe(id, buf.readItem(), buf.readItem());
		}

		@Override
		public void toNetwork(@NotNull FriendlyByteBuf buf, @NotNull LightPuzzleRecipe recipe) {
			buf.writeItem(recipe.getInput());
			buf.writeItem(recipe.getOutput());
		}
	}
}
