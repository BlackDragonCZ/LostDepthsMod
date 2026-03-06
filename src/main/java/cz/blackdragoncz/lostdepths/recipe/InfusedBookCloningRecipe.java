package cz.blackdragoncz.lostdepths.recipe;

import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import cz.blackdragoncz.lostdepths.init.LostdepthsModRecipeSerializers;
import cz.blackdragoncz.lostdepths.item.book.InfusedWrittenBookItem;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class InfusedBookCloningRecipe extends CustomRecipe {

	public InfusedBookCloningRecipe(ResourceLocation id, CraftingBookCategory category) {
		super(id, category);
	}

	@Override
	public boolean matches(CraftingContainer container, Level level) {
		int writtenCount = 0;
		int writableCount = 0;
		for (int i = 0; i < container.getContainerSize(); i++) {
			ItemStack stack = container.getItem(i);
			if (stack.isEmpty()) continue;
			if (stack.is(LostdepthsModItems.INFUSED_WRITTEN_BOOK.get())) {
				InfusedWrittenBookItem item = (InfusedWrittenBookItem) stack.getItem();
				if (!item.canBeCopied(stack)) return false;
				writtenCount++;
			} else if (stack.is(LostdepthsModItems.INFUSED_WRITABLE_BOOK.get())) {
				writableCount++;
			} else {
				return false;
			}
		}
		return writtenCount == 1 && writableCount == 1;
	}

	@Override
	public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
		ItemStack writtenBook = ItemStack.EMPTY;
		for (int i = 0; i < container.getContainerSize(); i++) {
			ItemStack stack = container.getItem(i);
			if (stack.is(LostdepthsModItems.INFUSED_WRITTEN_BOOK.get())) {
				writtenBook = stack;
				break;
			}
		}
		if (writtenBook.isEmpty()) return ItemStack.EMPTY;

		ItemStack copy = new ItemStack(LostdepthsModItems.INFUSED_WRITTEN_BOOK.get());
		CompoundTag sourceTag = writtenBook.getTag();
		if (sourceTag != null) {
			CompoundTag newTag = sourceTag.copy();
			int gen = newTag.getInt("generation");
			newTag.putInt("generation", Math.min(gen + 1, 2));
			// Do not copy contract_signer
			newTag.remove("contract_signer");
			newTag.remove("contract_allowed");
			copy.setTag(newTag);
		}
		return copy;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
		NonNullList<ItemStack> remaining = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
		for (int i = 0; i < container.getContainerSize(); i++) {
			ItemStack stack = container.getItem(i);
			if (stack.is(LostdepthsModItems.INFUSED_WRITTEN_BOOK.get())) {
				// Return the original written book
				remaining.set(i, stack.copyWithCount(1));
			}
		}
		return remaining;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return LostdepthsModRecipeSerializers.INFUSED_BOOK_CLONING.get();
	}
}
