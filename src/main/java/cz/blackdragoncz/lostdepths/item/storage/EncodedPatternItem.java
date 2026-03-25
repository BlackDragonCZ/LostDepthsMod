package cz.blackdragoncz.lostdepths.item.storage;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * An encoded pattern storing a recipe definition.
 * NBT structure:
 * - PatternType: "crafting" or "processing"
 * - RecipeId: string (for crafting patterns, the recipe ResourceLocation)
 * - Inputs: ListTag of CompoundTag (each is an ItemStack)
 * - Outputs: ListTag of CompoundTag (each is an ItemStack)
 * - Invalid: boolean (set when recipe validation fails)
 * - InvalidReason: string (human-readable reason)
 */
public class EncodedPatternItem extends Item {

	public static final String TAG_PATTERN_TYPE = "PatternType";
	public static final String TAG_RECIPE_ID = "RecipeId";
	public static final String TAG_INPUTS = "Inputs";
	public static final String TAG_OUTPUTS = "Outputs";
	public static final String TAG_INVALID = "Invalid";
	public static final String TAG_INVALID_REASON = "InvalidReason";

	public static final String TYPE_CRAFTING = "crafting";
	public static final String TYPE_PROCESSING = "processing";

	public EncodedPatternItem() {
		super(new Item.Properties().stacksTo(1));
	}

	// --- NBT helpers ---

	public static boolean isCraftingPattern(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		return tag != null && TYPE_CRAFTING.equals(tag.getString(TAG_PATTERN_TYPE));
	}

	public static boolean isProcessingPattern(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		return tag != null && TYPE_PROCESSING.equals(tag.getString(TAG_PATTERN_TYPE));
	}

	public static boolean isInvalid(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		return tag != null && tag.getBoolean(TAG_INVALID);
	}

	public static String getRecipeId(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		return tag != null ? tag.getString(TAG_RECIPE_ID) : "";
	}

	public static List<ItemStack> getInputs(ItemStack stack) {
		return readStackList(stack, TAG_INPUTS);
	}

	public static List<ItemStack> getOutputs(ItemStack stack) {
		return readStackList(stack, TAG_OUTPUTS);
	}

	public static void markInvalid(ItemStack stack, String reason) {
		CompoundTag tag = stack.getOrCreateTag();
		tag.putBoolean(TAG_INVALID, true);
		tag.putString(TAG_INVALID_REASON, reason);
	}

	public static void clearInvalid(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		if (tag != null) {
			tag.remove(TAG_INVALID);
			tag.remove(TAG_INVALID_REASON);
		}
	}

	/**
	 * Encode a crafting pattern.
	 */
	public static ItemStack encodeCrafting(ItemStack blankPattern, ResourceLocation recipeId,
										   List<ItemStack> inputs, ItemStack output) {
		ItemStack encoded = new ItemStack(blankPattern.getItem() instanceof BlankPatternItem
				? cz.blackdragoncz.lostdepths.init.LostdepthsModItems.NT_ENCODED_PATTERN.get()
				: blankPattern.getItem());
		CompoundTag tag = encoded.getOrCreateTag();
		tag.putString(TAG_PATTERN_TYPE, TYPE_CRAFTING);
		tag.putString(TAG_RECIPE_ID, recipeId.toString());
		writeStackList(tag, TAG_INPUTS, inputs);
		writeStackList(tag, TAG_OUTPUTS, List.of(output));
		return encoded;
	}

	/**
	 * Encode a processing pattern.
	 */
	public static ItemStack encodeProcessing(List<ItemStack> inputs, List<ItemStack> outputs) {
		ItemStack encoded = new ItemStack(
				cz.blackdragoncz.lostdepths.init.LostdepthsModItems.NT_ENCODED_PATTERN.get());
		CompoundTag tag = encoded.getOrCreateTag();
		tag.putString(TAG_PATTERN_TYPE, TYPE_PROCESSING);
		tag.putString(TAG_RECIPE_ID, "");
		writeStackList(tag, TAG_INPUTS, inputs);
		writeStackList(tag, TAG_OUTPUTS, outputs);
		return encoded;
	}

	/**
	 * Validate a crafting pattern against the current recipe manager.
	 * @return true if valid, false if recipe no longer exists.
	 */
	public static boolean validateCraftingPattern(ItemStack stack, RecipeManager recipeManager) {
		if (!isCraftingPattern(stack)) return true; // processing patterns are always "valid"
		String recipeId = getRecipeId(stack);
		if (recipeId.isEmpty()) return false;

		Optional<? extends Recipe<?>> recipe = recipeManager.byKey(new ResourceLocation(recipeId));
		return recipe.isPresent();
	}

	// --- Tooltip ---

	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
		CompoundTag tag = stack.getTag();
		if (tag == null) return;

		if (isInvalid(stack)) {
			list.add(Component.literal("§c§lINVALID PATTERN").withStyle(ChatFormatting.DARK_RED));
			String reason = tag.getString(TAG_INVALID_REASON);
			if (!reason.isEmpty()) {
				list.add(Component.literal("§c" + reason));
			}
			String recipeId = getRecipeId(stack);
			if (!recipeId.isEmpty()) {
				list.add(Component.literal("§7Recipe: §e" + recipeId));
			}
			return;
		}

		String type = isCraftingPattern(stack) ? "Crafting" : "Processing";
		list.add(Component.literal("§7Type: §f" + type));

		List<ItemStack> outputs = getOutputs(stack);
		if (!outputs.isEmpty()) {
			list.add(Component.literal("§7Output: §f" + outputs.get(0).getHoverName().getString()
					+ (outputs.get(0).getCount() > 1 ? " x" + outputs.get(0).getCount() : "")));
		}

		List<ItemStack> inputs = getInputs(stack);
		if (!inputs.isEmpty() && flag.isAdvanced()) {
			list.add(Component.literal("§7Inputs:"));
			for (ItemStack input : inputs) {
				if (!input.isEmpty()) {
					list.add(Component.literal("  §8- " + input.getHoverName().getString()
							+ (input.getCount() > 1 ? " x" + input.getCount() : "")));
				}
			}
		}

		String recipeId = getRecipeId(stack);
		if (!recipeId.isEmpty() && flag.isAdvanced()) {
			list.add(Component.literal("§8Recipe: " + recipeId));
		}
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return !isInvalid(stack);
	}

	// --- Internal ---

	private static void writeStackList(CompoundTag tag, String key, List<ItemStack> stacks) {
		ListTag list = new ListTag();
		for (ItemStack s : stacks) {
			list.add(s.isEmpty() ? new CompoundTag() : s.save(new CompoundTag()));
		}
		tag.put(key, list);
	}

	private static List<ItemStack> readStackList(ItemStack pattern, String key) {
		List<ItemStack> result = new ArrayList<>();
		CompoundTag tag = pattern.getTag();
		if (tag == null || !tag.contains(key)) return result;
		ListTag list = tag.getList(key, 10); // CompoundTag type
		for (int i = 0; i < list.size(); i++) {
			CompoundTag itemTag = list.getCompound(i);
			result.add(itemTag.isEmpty() ? ItemStack.EMPTY : ItemStack.of(itemTag));
		}
		return result;
	}
}
