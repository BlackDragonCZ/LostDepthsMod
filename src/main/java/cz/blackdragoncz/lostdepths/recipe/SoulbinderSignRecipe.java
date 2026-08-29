package cz.blackdragoncz.lostdepths.recipe;

import cz.blackdragoncz.lostdepths.ability.SoulBinding;
import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import cz.blackdragoncz.lostdepths.init.LostdepthsModRecipeSerializers;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * Soulbinder + signed contract book -> the same Soulbinder carrying that signature. The book is handed
 * back rather than consumed. Rejected if the book was not written by the Soulbinder's owner, if it was
 * self-signed, or if that signature is already on it - see {@link SoulBinding#addSigner}.
 */
public class SoulbinderSignRecipe extends CustomRecipe {

	public SoulbinderSignRecipe(ResourceLocation id, CraftingBookCategory category) {
		super(id, category);
	}

	@Override
	public boolean matches(CraftingContainer container, Level level) {
		return !assemble(container, null).isEmpty();
	}

	@Override
	public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
		ItemStack soulbinder = ItemStack.EMPTY;
		ItemStack book = ItemStack.EMPTY;

		for (int i = 0; i < container.getContainerSize(); i++) {
			ItemStack stack = container.getItem(i);
			if (stack.isEmpty()) continue;
			if (stack.is(LostdepthsModItems.SOULBINDER.get())) {
				if (!soulbinder.isEmpty()) return ItemStack.EMPTY;
				soulbinder = stack;
			} else if (stack.is(LostdepthsModItems.INFUSED_WRITTEN_BOOK.get())) {
				if (!book.isEmpty()) return ItemStack.EMPTY;
				book = stack;
			} else {
				return ItemStack.EMPTY;
			}
		}
		if (soulbinder.isEmpty() || book.isEmpty()) return ItemStack.EMPTY;

		ItemStack result = soulbinder.copy();
		return SoulBinding.addSigner(result, book) ? result : ItemStack.EMPTY;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
		NonNullList<ItemStack> remaining = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
		for (int i = 0; i < container.getContainerSize(); i++) {
			ItemStack stack = container.getItem(i);
			if (stack.is(LostdepthsModItems.INFUSED_WRITTEN_BOOK.get()))
				remaining.set(i, stack.copyWithCount(1));
		}
		return remaining;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return LostdepthsModRecipeSerializers.SOULBINDER_SIGN.get();
	}
}
